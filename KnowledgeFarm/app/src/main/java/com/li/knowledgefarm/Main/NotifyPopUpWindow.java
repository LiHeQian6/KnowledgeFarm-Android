package com.li.knowledgefarm.Main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.Chinese;
import com.li.knowledgefarm.entity.Notification;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
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

public class NotifyPopUpWindow extends PopupWindow {

    private Context context;
    private ListView listView;
    private List<Notification> system_list;
    private List<Notification> friend_notify_list;
    private List<Notification> mess_notify_list;
    private Handler get_system_notify;
    private NotifyListAdapter listAdapter;
    private String type;
    private Button system_notify;
    private Button friend_notify;
    private Button mess_notify;
    private Gson gson;

    public NotifyPopUpWindow(Context context) {
        super(context);
        this.context = context;
        Init();
        getNotify("1");
    }

    /**
     * @Description PopUpWindow初始化
     * @Author 孙建旺
     * @Date 下午6:14 2020/04/21
     * @Param []
     * @return void
     */
    private void Init(){
        this.setHeight(800);
        this.setWidth(1400);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.notify_popup_layout,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
    }

    /**
     * @Description 获取控件Id
     * @Author 孙建旺
     * @Date 下午6:14 2020/04/21
     * @Param [view]
     * @return void
     */
    private void getViews(View view){
        system_notify = view.findViewById(R.id.system_btn);
        friend_notify = view.findViewById(R.id.friend_btn);
        mess_notify = view.findViewById(R.id.message_btn);
        listView = view.findViewById(R.id.notify_list_view);
        gson = new Gson();
        system_notify = view.findViewById(R.id.system_btn);
        friend_notify = view.findViewById(R.id.friend_btn);
        mess_notify = view.findViewById(R.id.message_btn);
        registListener();
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
    }

    /**
     * @Description 获取指定类型消息
     * @Author 孙建旺
     * @Date 下午6:13 2020/04/21
     * @Param [type]
     * @return void
     */
    private void getNotify(final String type){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("userId", LoginActivity.user.getId()+"")
                        .add("typeId",type).build();
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(context.getResources().getString(R.string.URL)+"/userfriend/findReceivedNotificationByType?userId&typeId&pageNumber&pageSize").build();
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
                Type list_type = new TypeToken<List<Notification>>() {
                }.getType();
                if(!message.equals("") && !message.contains("html")) {
                    switch (type) {
                        case "1":
                            system_list = gson.fromJson(message, list_type);
                            listAdapter = new NotifyListAdapter(system_list,R.layout.notify_item_layout,context);
                            break;
                        case "2":
                            friend_notify_list = gson.fromJson(message,list_type);
                            listAdapter = new NotifyListAdapter(friend_notify_list,R.layout.notify_item_layout,context);
                            break;
                    }
                    listView.setAdapter(listAdapter);
                }
            }
        };
    }

    class CustomerOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.system_btn:
                    getNotify("1");
                    break;
                case R.id.friend_btn:
                    getNotify("2");
                    break;
                case R.id.message_btn:
                    break;
            }
        }
    }
}
