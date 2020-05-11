package com.li.knowledgefarm.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.tencent.tauth.Tencent;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IfDoPop extends PopupWindow {
    private Context context;
    private String type;
    private TextView show_text;
    private Button cancel;
    private Button sure;
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch ((String)msg.obj){
                case "true":
                    LoginActivity.user.setEmail("");
                    Toast.makeText(context,"解绑邮箱成功",Toast.LENGTH_SHORT).show();
                    break;
                case "false":
                    Toast.makeText(context,"解绑邮箱失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public IfDoPop(Context context,String type) {
        super(context);
        this.context = context;
        this.type = type;
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.if_exit,
                null, false);
        this.setContentView(contentView);
        okHttpClient = OkHttpUtils.getInstance(context);
        getViews(contentView);
        showType();
    }

    /**
     * @Description 展示类型
     * @Author 孙建旺
     * @Date 上午11:12 2020/05/05
     * @Param []
     * @return void
     */
    private void showType() {
        if(type.equals("LogOut")){
            show_text.setText("你确定要退出吗？");
        }else if(type.equals("UnBindEmail")){
            show_text.setText("你确定要解绑邮箱吗？");
        }else {
            show_text.setText("你确定要解绑QQ吗？");
        }
    }

    /**
     * @Description 获取控件Id
     * @Author 孙建旺
     * @Date 上午11:11 2020/05/05
     * @Param [contentView]
     * @return void
     */
    private void getViews(View contentView) {
        show_text  = contentView.findViewById(R.id.request);
        cancel = contentView.findViewById(R.id.cancel_btn);
        sure = contentView.findViewById(R.id.sure_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case "LogOut":
                        regout();
                        break;
                    case "UnBindEmail":
                        unBindingEmail();
                        break;
                }
            }
        });
    }

    /**
     * 解绑邮箱
     */
    private void unBindingEmail(){
        new Thread() {
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().build();
                final Request request = new Request.Builder().post(formBody).url(context.getResources().getString(R.string.URL)+"/user/unBindingEmail").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        message.what = response.code();
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * 切换账号
     */
    private void regout() {
        /** 其中mAppId是分配给第三方应用的appid，类型为String*/
        String mAppId = "1110065654";//101827370
        /** Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI*/
        Tencent mTencent = Tencent.createInstance(mAppId, context);;
        mTencent.logout(context);

        /** 删除SharedPreferences内Token信息*/
        SharedPreferences sp = context.getSharedPreferences("token",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        /** 跳转到登录页面*/
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
        dismiss();
    }
}
