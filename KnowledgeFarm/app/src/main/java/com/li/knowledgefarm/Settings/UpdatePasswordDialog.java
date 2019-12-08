package com.li.knowledgefarm.Settings;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdatePasswordDialog extends PopupWindow {
    private View view;
    private Context context;
    private ImageView iv_return;
    private EditText edtOldPassword,edtNemPassword,edtNewPasswordTest;
    /** 保存*/
    private TextView tv_save;//ll
    /** OKHttpClient*/
    private OkHttpClient okHttpClient;
    private String ip = "10.7.87.220";
    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1://修改密码判断
                    switch ((String)msg.obj){
                        case "0":
                            Toast.makeText(context,"旧密码输入错误",Toast.LENGTH_SHORT).show();
                            break;
                        case "1":
                            Toast.makeText(context,"密码修改成功",Toast.LENGTH_SHORT).show();
                            dismiss();
                            break;
                        case "2":
                            Toast.makeText(context,"密码修改失败",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };

    public UpdatePasswordDialog(final Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.update_password, null);

        /** 设置设置popupWindow样式*/
        setpopupWndow();

        /** 初始化页面*/
        iv_return = view.findViewById(R.id.iv_return);
        edtOldPassword = view.findViewById(R.id.edtOldPassword);
        edtNemPassword = view.findViewById(R.id.edtNewPassword);
        edtNewPasswordTest = view.findViewById(R.id.edtNewPasswordTest);
        tv_save = view.findViewById(R.id.iv_return);
        okHttpClient = new OkHttpClient();

        /** 点击保存*/
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        /** 点击返回*/
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void save(){
        final String oldPassword = edtOldPassword.getText().toString().trim();
        final String newPassword = edtNemPassword.getText().toString().trim();
        final String newPasswordTest = edtNewPasswordTest.getText().toString().trim();
        if(oldPassword.equals("") || newPassword.equals("") || newPasswordTest.equals("")){
            Toast.makeText(context,"您还有没填写的内容！",Toast.LENGTH_SHORT).show();
        }
        if(!newPassword.equals(newPasswordTest)){
            Toast.makeText(context,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
        }else{
            new Thread(){
                @Override
                public void run() {
                    FormBody formBody = new FormBody.Builder().add("accout","71007839").add("oldPassword",oldPassword).add("newPassword",newPassword).build();
                    final Request request = new Request.Builder().post(formBody).url("http://"+ip+":8080/FarmKnowledge/user/updateUserPassword").build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("lww","请求失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            sendMessage(1,result);
                        }
                    });
                }
            }.start();
        }
    }

    /**
     * 设置popupWindow样式
     */
    private void setpopupWndow(){
        this.setContentView(view);
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        //this.setAnimationStyle(R.style.pop_animation);
        //ColorDrawable dw = new ColorDrawable(0xb0000000);//背景半透明
        ColorDrawable d = new ColorDrawable(Color.parseColor("#f5f5f5"));
        this.setBackgroundDrawable(d);
    }

    /**
     * handler发送message
     */
    private void sendMessage(int what ,Object obj){
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        handler.sendMessage(message);
    }

}
