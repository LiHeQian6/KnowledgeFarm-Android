package com.li.knowledgefarm.Settings;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;

import java.io.IOException;

public class UpdatePasswordActivity extends AppCompatActivity {
    /** 返回*/
    private ImageView iv_return;
    /** 旧密码、新密码、确认密码输入框*/
    private EditText edtOldPassword,edtNemPassword,edtNewPasswordTest;
    /** 保存*/
    private TextView tv_save;
    /** OKHttpClient*/
    private OkHttpClient okHttpClient;
    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1://修改密码判断
                    switch ((String)msg.obj){
                        case "true":
                            LoginActivity.user.setPassword(edtNemPassword.getText().toString().trim());
                            finish();
                            Toast.makeText(getApplicationContext(),"密码修改成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(getApplicationContext(),"密码修改失败",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        /** 初始化*/
        init();

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
                finish();
            }
        });

    }

    /**
     * 初始化
     */
    private void init(){
        iv_return = findViewById(R.id.iv_return);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNemPassword = findViewById(R.id.edtNewPassword);
        edtNewPasswordTest = findViewById(R.id.edtNewPasswordTest);
        tv_save = findViewById(R.id.tv_save);
        okHttpClient = new OkHttpClient();

        /** 关闭状态栏*/
        setStatusBar();
    }

    /**
     * 保存
     */
    private void save(){
        final String oldPassword = edtOldPassword.getText().toString().trim();
        final String newPassword = edtNemPassword.getText().toString().trim();
        final String newPasswordTest = edtNewPasswordTest.getText().toString().trim();
        if(oldPassword.equals("") || newPassword.equals("") || newPasswordTest.equals("")){
            Toast.makeText(getApplicationContext(),"您还有没填写的内容！",Toast.LENGTH_SHORT).show();
        }else {
            if (!newPassword.equals(newPasswordTest)) {
                Toast.makeText(getApplicationContext(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            } else {
                if(!oldPassword.equals(LoginActivity.user.getPassword())){
                    Toast.makeText(getApplicationContext(), "旧密码输入错误", Toast.LENGTH_SHORT).show();
                }else{
                    new Thread() {
                        @Override
                        public void run() {
                            FormBody formBody = new FormBody.Builder().add("accout", LoginActivity.user.getAccout()).add("oldPassword", oldPassword).add("newPassword", newPassword).build();
                            final Request request = new Request.Builder().post(formBody).url(getApplicationContext().getResources().getString(R.string.URL) + "/user/updateUserPassword").build();
                            Call call = okHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.i("lww", "请求失败");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String result = response.body().string();
                                    sendMessage(1, result);
                                }
                            });
                        }
                    }.start();
                }
            }
        }
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

    /**
     * 关闭状态栏
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }

}
