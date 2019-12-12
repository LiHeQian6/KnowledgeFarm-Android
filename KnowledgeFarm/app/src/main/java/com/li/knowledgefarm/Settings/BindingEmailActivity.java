package com.li.knowledgefarm.Settings;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BindingEmailActivity extends AppCompatActivity {
    private ImageView iv_return;
    /** 邮箱输入框上面的文字*/
    private TextView tv_email;
    /** 邮箱输入框*/
    private EditText edtEmail;
    /** 验证码输入框*/
    private EditText edtTestCode;
    /** 获取验证码*/
    private TextView tv_getTestCode;
    /** 验证码*/
    private TextView tv_testCode;
    /** 完成*/
    private Button btnTest;
    /** OKHttpClient*/
    private OkHttpClient okHttpClient;
    /** 异步线程*/
    private GetTestCodeAsyncTask asyncTask;
    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0: // 绑定邮箱判断
                    if(msg.obj.equals("true")){
                        LoginActivity.user.setEmail(edtEmail.getText().toString().trim());
                        asyncTask.cancel(true);
                        EventBus.getDefault().post("绑定邮箱成功");
                        finish();
                        Toast.makeText(getApplicationContext(),"绑定邮箱成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"绑定邮箱失败",Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_email);

        /** 初始化*/
        init();

        /** 点击获取验证码*/
        tv_getTestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtEmail.getText().toString().trim().equals("")){
                    if(isEmail(edtEmail.getText().toString().trim())){
                        getTestCode();
                    }else{
                        Toast.makeText(getApplicationContext(),"邮箱格式错误",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"邮箱不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /** 点击完成*/
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtTestCode.getText().toString().trim().equals("")){
                    if(edtTestCode.getText().toString().trim().equals(tv_testCode.getText())){
                        over();
                    }else{
                        Toast.makeText(getApplicationContext(),"验证码输入错误",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"验证码不能为空",Toast.LENGTH_SHORT).show();
                }

            }
        });

        /** 点击返回*/
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncTask.cancel(true);
                finish();
            }
        });

    }

    /**
     * 初始化
     */
    private void init(){
        iv_return = findViewById(R.id.iv_return);
        tv_email = findViewById(R.id.tv_email);
        edtEmail = findViewById(R.id.edtEmail);
        edtTestCode = findViewById(R.id.edtTestCode);
        tv_getTestCode = findViewById(R.id.tv_getTestCode);
        tv_testCode = findViewById(R.id.tv_testCode);
        btnTest = findViewById(R.id.btnTest);

        okHttpClient = new OkHttpClient();

        /** 关闭状态栏*/
        setStatusBar();
    }

    /**
     * 获取验证码
     */
    private void getTestCode(){
        asyncTask = new GetTestCodeAsyncTask(getApplicationContext(),tv_email,tv_getTestCode,tv_testCode,edtEmail.getText().toString().trim());
        asyncTask.execute();
    }

    /**
     * 完成
     */
    private void over(){
        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("accout", LoginActivity.user.getAccout()).add("email", edtEmail.getText().toString().trim()).build();
                final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/bindingEmail").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        sendMessage(0,result);
                    }
                });
            }
        }.start();
    }

    /**
     * 判断邮箱格式是否正确
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
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
