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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.Md5Encode;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.User;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import androidx.annotation.NonNull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.Response;


import static com.li.knowledgefarm.Login.LoginActivity.parsr;

public class QQFirstActivity extends AppCompatActivity {
    private String grade;
    private String password;
    private String openId;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 4:
                    CustomerToast.getInstance(getApplicationContext(),"注册成功！",Toast.LENGTH_SHORT).show();
                    Intent autoToStart = new Intent(QQFirstActivity.this,StartActivity.class);
                    autoToStart.setAction("QQFirstLogin");
                    startActivity(autoToStart);
                    finish();
                    break;
                case 5:
                    CustomerToast.getInstance(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private String config;
    private String Nickname;
    private String Path;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_qqpwd);
        okHttpClient = OkHttpUtils.getInstance(this);
        //设置横屏
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        FullScreen.NavigationBarStatusBar(QQFirstActivity.this,true);
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
                if(password.equals("")||config.equals("")){
                    CustomerToast.getInstance(getApplicationContext(),"请完善注册信息！",Toast.LENGTH_SHORT).show();
                }else if(!password.equals(config)){
                    CustomerToast.getInstance(getApplicationContext(),"密码输入不一致！",Toast.LENGTH_SHORT).show();
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

    //首次登陆后注册信息
    private void registToServer() {
        //Request对象(Post、FormBody)
        FormBody formBody = new FormBody.Builder()
                .add("grade",grade)
                .add("password", Md5Encode.getMD5(password.getBytes()))
                .add("openId",openId)
                .add("photo", Path)
                .add("nickName",Nickname)
                .build();
        Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/addQQUser").build();
        //Call
        Call call = okHttpClient.newCall(request);
        //异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("jing", "请求失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                Message message = new Message();
                String result = response.body().string();
                if(result.equals("fail")){
                    message.what = 5;
                    message.obj = result;
                    mHandler.sendMessage(message);
                }else {
                    message.what = 4;
                    message.obj = parsr(URLDecoder.decode(result), User.class);
                    UserUtil.setUser((User) message.obj);
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    //年级选择

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

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        finish();
        System.exit(0);
    }

    //将字节数组换成成16进制的字符串
    public String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray =new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b& 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

}
