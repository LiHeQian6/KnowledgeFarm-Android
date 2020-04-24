package com.li.knowledgefarm.Main;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.Notification;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 孙建旺
 * @description
 * @date 2020/04/15 下午2:51
 */

public class NotifyActivity extends AppCompatActivity {

    private ListView listView;
    private FriendsPage<Notification> notify_list;
    private Handler get_system_notify;
    private String current_type = "1";
    private Button system_notify;
    private Button friend_notify;
    private Button mess_notify;
    private Button add_notify;
    private ImageView next_notify;
    private ImageView pre_nofify;
    private ImageView pre;
    private ImageView next;
    private Gson gson;
    private TextView none_notify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_layout);
        getViews();
        registListener();
        setStatusBar();
        getNotify("2",1,6);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.notify_pop_out);
    }

    private void OnclickItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(current_type.equals("1")) {
                    NotifyPopUpWindow popUpWindow = new NotifyPopUpWindow(getApplicationContext(), notify_list.getList().get(position));
                    popUpWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
            }
        });
    }

    /**
     * @Description 获取控件Id
     * @Author 孙建旺
     * @Date 下午6:14 2020/04/21
     * @Param [view]
     * @return void
     */
    private void getViews(){
        system_notify = findViewById(R.id.system_btn);
        friend_notify = findViewById(R.id.friend_btn);
        mess_notify = findViewById(R.id.message_btn);
        listView = findViewById(R.id.notify_list_view);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh_mm_ss").create();
        system_notify = findViewById(R.id.system_btn);
        friend_notify = findViewById(R.id.friend_btn);
        mess_notify = findViewById(R.id.message_btn);
        add_notify = findViewById(R.id.add_btn);
        pre = findViewById(R.id.pre_notify);
        next = findViewById(R.id.next_notify);
        next_notify = findViewById(R.id.next_notify);
        pre_nofify = findViewById(R.id.pre_notify);
        none_notify = findViewById(R.id.none_notify);
    }

    /**
     * @Description 注册点击事件监听器
     * @Author 孙建旺
     * @Date 下午6:13 2020/04/21
     * @Param []
     * @return void
     */
    private void registListener(){
        system_notify.setOnClickListener(new CustomerOnclickListener());
        friend_notify.setOnClickListener(new CustomerOnclickListener());
        mess_notify.setOnClickListener(new CustomerOnclickListener());
        add_notify.setOnClickListener(new CustomerOnclickListener());
        pre.setOnClickListener(new CustomerOnclickListener());
        next.setOnClickListener(new CustomerOnclickListener());
        next_notify.setOnClickListener(new CustomerOnclickListener());
        pre_nofify.setOnClickListener(new CustomerOnclickListener());
    }

    /**
     * @Description 获取指定类型消息
     * @Author 孙建旺
     * @Date 下午6:13 2020/04/21
     * @Param [type]
     * @return void
     */
    private void getNotify(final String type, final int pageNumber, final int pageSize){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("userId", 109+"")
                        .add("typeId",type)
                        .add("pageNumber",pageNumber+"")
                        .add("pageSize",pageSize+"").build();
                Request request = new Request.Builder()
                        .post(formBody)
                        .url("http://39.106.18.238:8081"+"/notification/findReceivedNotificationByType").build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("通知信息", "请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        get_system_notify.sendMessage(message);
                    }
                });
            }
        }.start();
        get_system_notify = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                Log.e("message",message);
                Type list_type = new TypeToken<FriendsPage<Notification>>() {
                }.getType();
                if(!message.equals("") && !message.contains("html")) {
                    notify_list = gson.fromJson(message, list_type);
                    if(notify_list.getList().size() == 0){
                        listView.setVisibility(View.GONE);
                        none_notify.setVisibility(View.VISIBLE);
                        return;
                    }else {
                        listView.setVisibility(View.VISIBLE);
                        none_notify.setVisibility(View.GONE);
                    }
                    switch (type) {
                        case "1":
                            SystemNotifyAdapter listAdapter = new SystemNotifyAdapter(notify_list,R.layout.notify_item_layout,getApplicationContext());
                            listView.setAdapter(listAdapter);
                            OnclickItem();
                            break;
                        case "2":
                            FriendNotifyAdapter listAdapter1 = new FriendNotifyAdapter(notify_list,R.layout.friend_notify_item,getApplicationContext());
                            listView.setAdapter(listAdapter1);
                            break;
                    }
                }
            }
        };
    }

    class CustomerOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.system_btn:
                    current_type = "1";
                    getNotify("1",1,6);
                    break;
                case R.id.friend_btn:
                    current_type = "2";
                    getNotify("2",1,6);
                    break;
                case R.id.message_btn:
                    current_type = "3";
                    break;
                case R.id.next_notify:
                    break;
                case R.id.pre_notify:

                    break;
            }
        }
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }
}
