package com.li.knowledgefarm.notify;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.NavigationBarUtil;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.EventBean;
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.Notification;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;

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

    private ListView listView; // 展示通知ListView
    private FriendsPage<Notification> notify_list;
    private Handler get_system_notify;
    private Handler get_mysend_notify;
    private String current_type = "1"; //当前通知类型
    private Button system_notify; //系统通知按钮
    private Button friend_notify; //好友通知按钮
    private Button mess_notify; // 其他通知按钮
    private Button add_notify; // 发送的通知按钮
    private ImageView returns; //返回图标
    private Gson gson; // gson
    private TextView none_notify; //没有通知提示
    private Button delete_all_btn; //删除已读按钮
    private Button all_have_read; //一键已读按钮
    private Handler if_delete_all;
    private SendNotifyAdapter sendNotifyAdapter; //数据绑定器
    private ImageView system_notify_red; //系统通知提示红点
    private ImageView friend_notify_red; //好友通知提示红点
    public ImageView send_notify_red; //我发送的通知提示红点
    private ImageView other_notify_red; //其他通知提示红点
    private OkHttpClient okHttpClient;
    private OtherNotifyAdapter otherNotifyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_layout);
        okHttpClient = OkHttpUtils.getInstance(this);
        getViews();
        showRed();
        registListener();
        FullScreen.NavigationBarStatusBar(NotifyActivity.this,true);
        getNotify("1",1,6);
        getNotifyHandler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateRed(EventBean eventBean){
        if(eventBean.getIfRead()) {
            if(current_type.equals("2"))
                send_notify_red.setVisibility(View.GONE);
            else
                other_notify_red.setVisibility(View.GONE);
        }
    }

    /**
     * @Description 提示新消息
     * @Author 孙建旺
     * @Date 下午6:14 2020/05/03
     * @Param []
     * @return void
     */
    private void showRed() {
        if(MainActivity.notifyStatus.get(0))
            system_notify_red.setVisibility(View.VISIBLE);
        if(MainActivity.notifyStatus.get(1))
            friend_notify_red.setVisibility(View.VISIBLE);
        if(MainActivity.notifyStatus.get(2))
            send_notify_red.setVisibility(View.VISIBLE);
        if(MainActivity.notifyStatus.get(3))
            other_notify_red.setVisibility(View.VISIBLE);
    }

    /**
     * @Description 处理返回信息
     * @Author 孙建旺
     * @Date 下午6:14 2020/05/03
     * @Param []
     * @return void
     */
    private void getNotifyHandler() {
        get_system_notify = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                int code = msg.arg1;
                Log.e("message",message);
                Type list_type = new TypeToken<FriendsPage<Notification>>() {
                }.getType();
                if(!message.equals("") && code == 200) {
                    notify_list = gson.fromJson(message, list_type);
                    if(notify_list.getList().size() == 0){
                        listView.setVisibility(View.GONE);
                        switch (current_type){
                            case "1":
                                none_notify.setText("暂时没有系统通知哦");
                                break;
                            case "2":
                                none_notify.setText("暂时没有好友申请，快去加好友吧");
                                break;
                            case "3":
                                none_notify.setText("暂时没有通知哦");
                                break;
                        }
                        none_notify.setVisibility(View.VISIBLE);
                        return;
                    }else {
                        listView.setVisibility(View.VISIBLE);
                        none_notify.setVisibility(View.GONE);
                    }
                    switch (current_type) {
                        case "1":
                            MainActivity.notifyStatus.set(0,false);
                            SystemNotifyAdapter listAdapter = new SystemNotifyAdapter(notify_list,R.layout.notify_item_layout,getApplicationContext());
                            listView.setAdapter(listAdapter);
                            OnclickItem();
                            system_notify_red.setVisibility(View.INVISIBLE);
                            break;
                        case "2":
                            MainActivity.notifyStatus.set(1,false);
                            FriendNotifyAdapter listAdapter1 = new FriendNotifyAdapter(notify_list,R.layout.friend_notify_item,getApplicationContext());
                            listView.setAdapter(listAdapter1);
                            friend_notify_red.setVisibility(View.INVISIBLE);
                            break;
                        case "3":
                            MainActivity.notifyStatus.set(3,false);
                            otherNotifyAdapter = new OtherNotifyAdapter(notify_list,R.layout.other_notify_item,getApplicationContext());
                            listView.setAdapter(otherNotifyAdapter);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.notify_pop_out);
    }

    /**
     * @Description 注册ListView Item点击事件监听器
     * @Author 孙建旺
     * @Date 下午6:15 2020/05/03
     * @Param []
     * @return void
     */
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
        none_notify = findViewById(R.id.none_notify);
        all_have_read = findViewById(R.id.all_have_read);
        delete_all_btn = findViewById(R.id.delete_all_btn);
        returns = findViewById(R.id.goBack_notify);
        system_notify_red = findViewById(R.id.system_notify_red);
        friend_notify_red = findViewById(R.id.friend_notify_red);
        send_notify_red = findViewById(R.id.send_notify_red);
        other_notify_red = findViewById(R.id.other_notify_red);
        delete_all_btn.setVisibility(View.INVISIBLE);
        all_have_read.setVisibility(View.INVISIBLE);
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
        delete_all_btn.setOnClickListener(new CustomerOnclickListener());
        returns.setOnClickListener(new CustomerOnclickListener());
        all_have_read.setOnClickListener(new CustomerOnclickListener());
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
                        .add("typeId",type)
                        .add("pageNumber",pageNumber+"")
                        .add("pageSize",pageSize+"").build();
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(getResources().getString(R.string.URL)+"/notification/findReceivedNotificationByType").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("通知信息", "请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        message.arg1 = response.code();
                        get_system_notify.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * @Description 查询我发送到好友请求
     * @Author 孙建旺
     * @Date 下午6:15 2020/05/03
     * @Param [type, pageNumber, pageSize]
     * @return void
     */
    private void getMySendNotify(final String type,final int pageNumber,final int pageSize){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("typeId",type)
                        .add("pageNumber",pageNumber+"")
                        .add("pageSize",pageSize+"").build();
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(getResources().getString(R.string.URL)+"/notification/findSendNotificationByType").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("通知信息", "请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.arg1 = response.code();
                        message.obj = notify_message;
                        get_mysend_notify.sendMessage(message);
                    }
                });
            }
        }.start();
        get_mysend_notify = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                Log.e("message",message);
                Type list_type = new TypeToken<FriendsPage<Notification>>() {
                }.getType();
                if(!message.equals("") && msg.arg1 == 200) {
                    notify_list = gson.fromJson(message, list_type);
                    if(notify_list.getList().size() == 0){
                        listView.setVisibility(View.GONE);
                        none_notify.setText("没有添加好友请求呢，快去加好友吧");
                        none_notify.setVisibility(View.VISIBLE);
                        return;
                    }else {
                        listView.setVisibility(View.VISIBLE);
                        none_notify.setVisibility(View.GONE);
                    }
                    MainActivity.notifyStatus.set(2,false);
                    sendNotifyAdapter = new SendNotifyAdapter(notify_list,R.layout.send_notify_item,getApplicationContext());
                    listView.setAdapter(sendNotifyAdapter);
                    send_notify_red.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    /**
     * @Description 删除所有已读信息
     * @Author 孙建旺
     * @Date 下午6:16 2020/05/03
     * @Param [type, userId]
     * @return void
     */
    private void delete_All_Notify(final String type,final int userId){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.URL)+"/notification/deleteNotificationByType?typeId="+type).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String messages = response.body().string();
                        Message message = Message.obtain();
                        message.obj = messages;
                        if_delete_all.sendMessage(message);
                    }
                });
            }
        }.start();
        if_delete_all = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                if(message.equals("true") && !message.equals("")){
                    Iterator<Notification> it = notify_list.getList().iterator();
                    while (it.hasNext()){
                        Notification n = it.next();
                        if(n.isHaveRead()!=0){
                            it.remove();
                        }
                    }
                    if (current_type.equals("2")) {
                        if(sendNotifyAdapter != null) {
                            sendNotifyAdapter.notifyDataSetChanged();
                            CustomerToast.getInstance(NotifyActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if(otherNotifyAdapter != null) {
                            otherNotifyAdapter.notifyDataSetChanged();
                            CustomerToast.getInstance(NotifyActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    CustomerToast.getInstance(NotifyActivity.this,"网络出了点问题",Toast.LENGTH_SHORT).show();
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
                    delete_all_btn.setVisibility(View.INVISIBLE);
                    all_have_read.setVisibility(View.INVISIBLE);
                    getNotify("1",1,4);
                    break;
                case R.id.friend_btn:
                    current_type = "2";
                    delete_all_btn.setVisibility(View.INVISIBLE);
                    all_have_read.setVisibility(View.INVISIBLE);
                    getNotify("2",1,4);
                    break;
                case R.id.add_btn:
                    current_type = "2";
                    delete_all_btn.setVisibility(View.VISIBLE);
                    all_have_read.setVisibility(View.VISIBLE);
                    getMySendNotify("2",1,4);
                    break;
                case R.id.message_btn:
                    current_type = "3";
                    delete_all_btn.setVisibility(View.VISIBLE);
                    all_have_read.setVisibility(View.VISIBLE);
                    getNotify(current_type,1,4);
                    break;
                case R.id.delete_all_btn:
                    delete_All_Notify(current_type,UserUtil.getUser().getId());
                    break;
                case R.id.goBack_notify:
                    finish();
                    break;
                case R.id.all_have_read:
                    if (current_type.equals("2")) {
                        if (sendNotifyAdapter != null)
                            sendNotifyAdapter.AllHaveRead();
                    }
                    else {
                        if (otherNotifyAdapter != null)
                            otherNotifyAdapter.changeStatus();
                    }
                    break;
            }
        }
    }

}
