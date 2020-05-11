package com.li.knowledgefarm.notify;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.Notification;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NotifyPopUpWindow extends PopupWindow {

    private Button delete;
    private Notification notification;
    private TextView not_title;
    private TextView not_content;
    private TextView not_time;
    private Handler if_delete_handler;
    private OkHttpClient okHttpClient;


    public NotifyPopUpWindow(final Context context,Notification notification) {
        super(context);
        this.notification = notification;
        this.setHeight(800);
        this.setWidth(1200);
        this.setOutsideTouchable(false);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        final View contentView = LayoutInflater.from(context).inflate(R.layout.notify_message_pop,
                null, false);
        this.setContentView(contentView);
        okHttpClient = OkHttpUtils.getInstance(context);
        getViews(contentView);
        setText();
    }

    private void getViews(View view){
        not_title = view.findViewById(R.id.not_title);
        not_time = view.findViewById(R.id.not_time);
        not_content = view.findViewById(R.id.not_content);
    }

    /**
     * @Description 删除通知
     * @Author 孙建旺
     * @Date 上午10:09 2020/04/28
     * @Param []
     * @return void
     */
    private void deleteThisNotify(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                final Request request = new Request.Builder()
                        .url("").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("Error","网络错误");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String messages = response.body().string();
                        Message message = Message.obtain();
                        message.obj = messages;
                        if_delete_handler.sendMessage(message);
                    }
                });
            }
        }.start();
        if_delete_handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;

            }
        };
    }

    private void setText(){
        not_title.setText(notification.getTitle());
        not_content.setText("内容："+notification.getContent());
        not_time.setText(new SimpleDateFormat("yyyy-MM-dd").format(notification.getCreateTime()));
    }
}
