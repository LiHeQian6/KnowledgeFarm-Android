package com.li.knowledgefarm.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import com.li.knowledgefarm.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.li.knowledgefarm.entity.User;
import java.io.IOException;


import androidx.annotation.NonNull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import static com.li.knowledgefarm.Login.LoginActivity.mTencent;
import static com.li.knowledgefarm.Login.LoginActivity.parsr;
import static com.li.knowledgefarm.Login.LoginActivity.user;

public class QQFirstActivity extends AppCompatActivity {
    private String grade;
    private String password;
    private String email;
    private String openId;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 4:
                    user = (User) msg.obj;
                    Toast.makeText(getApplicationContext(),"注册成功！",Toast.LENGTH_SHORT).show();
                    Intent autoToStart = new Intent(QQFirstActivity.this,StartActivity.class);
                    autoToStart.setAction("QQFirstLogin");
                    startActivity(autoToStart);
                    finish();
                    break;
            }
        }
    };



    private String config;
    private String Nickname;
    private String Path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_qqpwd);
        //设置横屏
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setStatusBar();
        Intent intent = getIntent();
        openId = intent.getStringExtra("opId");
        Nickname = intent.getStringExtra("nick");
        Path = intent.getStringExtra("photo");
        Spinner spinner = findViewById(R.id.spiner);
        spinner.setOnItemSelectedListener(new ProvOnItemSelectedListener());

        Button button = findViewById(R.id.btnRegist);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText pwd = findViewById(R.id.registPwd);
                password = pwd.getText().toString();
                EditText configPwd = findViewById(R.id.configPwd);
                config = configPwd.getText().toString();
                EditText emails = findViewById(R.id.boundQQ);
                email = emails.getText().toString();
                if(password.equals("")||config.equals("")||email.equals("")){
                    Toast.makeText(getApplicationContext(),"请完善注册信息！"+password+config+email,Toast.LENGTH_SHORT).show();
                }else if(!password.equals(config)){
                    Toast.makeText(getApplicationContext(),"密码输入不一致！",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(){
                        @Override
                        public void run() {
                            registToServer();
                        }
                    }.start();
                }
            }
        });
    }

    private void registToServer() {
        //Request对象(Post、FormBody)
        FormBody formBody = new FormBody.Builder()
                .add("grade",grade)
                .add("pwd",password)
                .add("email",email)
                .add("openId",openId)
                .add("photo",Path)
                .add("nickName",Nickname)
                .build();
        Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/addQQUser").build();
        //Call
        Call call = new OkHttpClient().newCall(request);
        //异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("jing", "请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.what = 4;
                message.obj = parsr(response.body().string(), User.class);
                mHandler.sendMessage(message);

            }
        });
    }


    private class ProvOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    grade = "1";
                    break;
                case 1:
                    grade = "2";
                    break;
                case 2:
                    grade = "3";
                    break;
                case 3:
                    grade = "4";
                    break;
                case 4:
                    grade = "5";
                    break;
                case 5:
                    grade = "6";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            grade = "1";
        }
    }

    /**
     * @Description 设置通知栏状态
     * @Auther 孙建旺
     * @Date 下午 2:29 2019/12/10
     * @Param []
     * @return void
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }
}
