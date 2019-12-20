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

import com.li.knowledgefarm.entity.User;
import java.io.IOException;
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
import static com.li.knowledgefarm.Login.LoginActivity.user;

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
                    user = (User) msg.obj;
                    Toast.makeText(getApplicationContext(),"注册成功！",Toast.LENGTH_SHORT).show();
                    Intent autoToStart = new Intent(QQFirstActivity.this,StartActivity.class);
                    autoToStart.setAction("QQFirstLogin");
                    startActivity(autoToStart);
                    finish();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
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
                if(password.equals("")||config.equals("")){
                    Toast.makeText(getApplicationContext(),"请完善注册信息！",Toast.LENGTH_SHORT).show();
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
                .add("password",stringMD5(password))
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
                String result = response.body().string();
                if(result.equals("fail")){
                    message.what = 5;
                    message.obj = result;
                    mHandler.sendMessage(message);
                }else {
                    message.what = 4;
                    message.obj = parsr(result, User.class);
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
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

    //MD5加密
    public String stringMD5(String input) {
        try {
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest =MessageDigest.getInstance("MD5");
            // 输入的字符串转换成字节数组
            byte[] inputByteArray = input.getBytes();
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
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
