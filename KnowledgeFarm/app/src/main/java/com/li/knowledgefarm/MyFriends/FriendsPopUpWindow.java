package com.li.knowledgefarm.MyFriends;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.User;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FriendsPopUpWindow extends PopupWindow {

    private int displayWidth;
    private int displayHeight;
    private Context context;
    private EditText searchAccount;
    private ListView friendsListView;
    private RadioGroup searchSelected;
    private FriendsPage<User> friendsPage;
    private int searchSelectedItem=0;
    private OkHttpClient okHttpClient;
    private Handler friendsMessagesHandler;
    private Gson gson;
    private TextView now;
    private ImageView per;
    private ImageView next;
    private Button search;
    private long lastClickTime = 0;
    private long FAST_CLICK_DELAY_TIME = 500;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public FriendsPopUpWindow(Context context) {
    public FriendsDialog(Context context) {
        super(context);
        this.context = context;
        this.setOutsideTouchable(true);
//        this.setFocusable(false);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        this.setAnimationStyle(R.style.pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.friends_dialog,
                null, false);

        this.setContentView(contentView);
        getViews(contentView);
//        setText();
        registListener();
        Init(contentView);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setFocusable(false);
        super.showAtLocation(parent, gravity, x, y);
        final View view = getContentView();
        FullScreen.hideBottomUIMenu(view);
        setFocusable(true);
        update();
    }

    //        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void Init(View view){
//        WindowManager.LayoutParams attrs = context.getWindow().getAttributes();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayHeight = ds.heightPixels;
        displayWidth = ds.widthPixels;
        setFriendSize(view);
        getFriendsInfo(1);
        searchSelected.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.my:
                        if(friendsPage != null) {
                            friendsPage.setPrePageNum(1);
                            friendsPage.setNextPageNum(1);
                            searchSelectedItem = 0;
                            getFriendsInfo(1);
                        }else{
                            Toast.makeText(context,"获取好友列表失败",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.all:
                        if(friendsPage != null) {
                            friendsPage.setPrePageNum(1);
                            friendsPage.setNextPageNum(1);
                            searchSelectedItem = 1;
                            getAllInfo(1);
                        }else{
                            Toast.makeText(context,"获取好友列表失败",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setFriendSize(View view) {
        LinearLayout layout_search = view.findViewById(R.id.layout_search);
        Button search=view.findViewById(R.id.search);
        ImageView pre = view.findViewById(R.id.pre);
        ImageView next = view.findViewById(R.id.next);
        TextView now = view.findViewById(R.id.now);

        LinearLayout.LayoutParams params_search = new LinearLayout.LayoutParams((int)(displayWidth*0.3),(int)(displayHeight*0.08));
        params_search.gravity = Gravity.CENTER_HORIZONTAL;
        layout_search.setLayoutParams(params_search);

        LinearLayout.LayoutParams params_edit = new LinearLayout.LayoutParams((int)(displayWidth*0.24),(int)(displayHeight*0.1));
        params_edit.gravity = Gravity.CENTER;
        searchAccount.setLayoutParams(params_edit);
        searchAccount.setTextColor(ContextCompat.getColor(context,R.color.ShopTextColor));
        searchAccount.setHintTextColor(ContextCompat.getColor(context,R.color.ShopTextColor));
        searchAccount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        LinearLayout.LayoutParams params_button = new LinearLayout.LayoutParams((int)(displayWidth*0.06),(int)(displayHeight*0.07));
        params_button.gravity = Gravity.CENTER;
        search.setLayoutParams(params_button);
        search.setTextColor(ContextCompat.getColor(context,R.color.ShopTextColor));
        search.setTextSize((int)(displayHeight*0.02));
        search.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        LinearLayout.LayoutParams params_select = new LinearLayout.LayoutParams((int)(displayWidth*0.3),(int)(displayHeight*0.06));
        params_select.setMargins((int)(displayWidth*0.02),0,0,0);
        searchSelected.setLayoutParams(params_select);

        LinearLayout.LayoutParams params_listview = new LinearLayout.LayoutParams((int)(displayWidth*0.3),(int)(displayHeight*0.6));
        params_listview.gravity = Gravity.CENTER_HORIZONTAL;
        params_listview.setMargins(0,(int)(displayHeight*0.018),0,(int)(displayHeight*0.018));
        friendsListView.setLayoutParams(params_listview);
        friendsListView.setDividerHeight((int)(displayHeight*0.015));

        LinearLayout.LayoutParams params_pre = new LinearLayout.LayoutParams((int)(displayWidth*0.1),(int)(displayHeight*0.06));
        pre.setLayoutParams(params_pre);
        next.setLayoutParams(params_pre);
        now.setLayoutParams(params_pre);
        now.setTextSize((int)(displayHeight*0.02));
    }

    private void getViews(View view) {
        friendsListView=view.findViewById(R.id.friends_lv);
        searchAccount=view.findViewById(R.id.search_account);
        searchSelected=view.findViewById(R.id.searchSelected);
        search=view.findViewById(R.id.search);
        per = view.findViewById(R.id.pre);
        next = view.findViewById(R.id.next);
        okHttpClient = new OkHttpClient();
        now = view.findViewById(R.id.now);
        gson = new Gson();
    }

    private void registListener(){
        search.setOnClickListener(new CustomerOnclickListener());
        per.setOnClickListener(new CustomerOnclickListener());
        next.setOnClickListener(new CustomerOnclickListener());
    }

    /**
     * 获得所有人分页
     * @param pageNumber
     */
    private void getAllInfo(final int pageNumber){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(context.getResources().getString(R.string.URL)+"/userfriend/findAllUser?userId="+LoginActivity.user.getId()+"&pageNumber="+pageNumber).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj ="Fail";
                        friendsMessagesHandler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj =response.body().string();
                        friendsMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * 获得好友分页
     * @param pageNumber
     */
    private void getFriendsInfo(final int pageNumber) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(context.getResources().getString(R.string.URL)+"/userfriend/findUserFriend?userId="+ LoginActivity.user.getId()+"&pageNumber="+pageNumber).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj ="Fail";
                        friendsMessagesHandler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj =response.body().string();
                        friendsMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
        friendsMessagesHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String messages = (String)msg.obj;
                Log.e("好友",messages);
                if(!messages.equals("Fail") && !messages.contains("html")){
                    Type type = new TypeToken<FriendsPage<User>>(){}.getType();
                    friendsPage = gson.fromJson(messages,type);
                    now.setText(friendsPage.getCurrentPageNum()+"/"+friendsPage.getTotalPageNum());
                    FriendsCustomerAdapter customerAdapter = new FriendsCustomerAdapter(context,friendsPage.getList(),R.layout.friends_list_item,searchSelectedItem);
                    friendsListView.setAdapter(customerAdapter);
                    customerAdapter.notifyDataSetChanged();
                }else{
                    Toast toast = Toast.makeText(context,"获取数据失败！",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
    }

    /**
     * 根据账号查询好友
     */
    private void findFriendInfo(final String account) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/userfriend/findUserFriend?userId=" + LoginActivity.user.getId() + "&account=" + account).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        friendsMessagesHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        friendsMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * 根据账号在所有人中查询
     *
     * @param account
     */
    private void findPeopleByAccount(final String account) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/userfriend/findAllUser?userId=" + LoginActivity.user.getId() +"&account=" + account).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        friendsMessagesHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        friendsMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    class CustomerOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.pre:
                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                    if (searchSelectedItem == 0)
                        getFriendsInfo(friendsPage.getPrePageNum());
                    else
                        getAllInfo(friendsPage.getPrePageNum());
                    break;
                case R.id.next:
                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                    if (searchSelectedItem == 0)
                        getFriendsInfo(friendsPage.getNextPageNum());
                    else
                        getAllInfo(friendsPage.getNextPageNum());
                    break;
                case R.id.search:
                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                    Log.e("searchSelectedItem", searchSelectedItem + "");
                    if (searchSelectedItem == 0)
                        findFriendInfo(searchAccount.getText().toString());
                    else
                        findPeopleByAccount(searchAccount.getText().toString());
                    break;
            }
        }
    }
}
