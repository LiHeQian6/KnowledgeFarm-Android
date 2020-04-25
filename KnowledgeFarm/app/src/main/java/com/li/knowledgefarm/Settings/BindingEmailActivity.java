package com.li.knowledgefarm.Settings;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.AsyncTask;
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
import com.li.knowledgefarm.Util.FullScreen;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BindingEmailActivity extends AppCompatActivity {
    /** 返回*/
    private ImageView iv_return;
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
    /** 异步线程*/
    private TestCodeEffectAsyncTask testAsync;
    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0: //邮箱是否已被其他账号绑定判断
                    if(msg.obj.equals("already")){ //邮箱已被其他账号绑定
                        endAsync1(); //停止异步任务
                        endAsync2(); //停止异步任务
                        Toast.makeText(getApplicationContext(),"该邮箱已被其它账号绑定",Toast.LENGTH_SHORT).show();
                    }else if(msg.obj.equals("fail")){ //发送邮箱失败
                        endAsync1(); //停止异步任务
                        endAsync1(); //停止异步任务
                        Toast.makeText(getApplicationContext(),"发送验证码失败",Toast.LENGTH_SHORT).show();
                    }else{ //获取到验证码
                        tv_testCode.setText((String)msg.obj);
                    }
                    break;
                case 1: // 绑定邮箱判断
                    if(msg.obj.equals("true")){ //绑定成功
                        endAsync1(); //停止异步任务
                        endAsync2(); //停止异步任务
                        LoginActivity.user.setEmail(edtEmail.getText().toString().trim());
                        EventBus.getDefault().post("绑定邮箱成功");
                        Toast.makeText(getApplicationContext(),"绑定邮箱成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else{ //绑定失败
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
                if(!edtEmail.getText().toString().trim().equals("")){ //邮箱不为空
                    if(isEmail(edtEmail.getText().toString().trim())){ //邮箱格式正确
                        getTestCode();
                    }else{ //邮箱格式错误
                        Toast.makeText(getApplicationContext(),"邮箱格式错误",Toast.LENGTH_SHORT).show();
                    }
                }else { //邮箱为空
                    Toast.makeText(getApplicationContext(),"邮箱不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /** 点击完成*/
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtTestCode.getText().toString().trim().equals("")){ //验证码不为空
                    if(edtTestCode.getText().toString().trim().equals(tv_testCode.getText())){ //验证码正确
                        over();
                    }else{ //验证码错误
                        Toast.makeText(getApplicationContext(),"验证码输入错误",Toast.LENGTH_SHORT).show();
                    }
                }else{ //验证码为空
                    Toast.makeText(getApplicationContext(),"验证码不能为空",Toast.LENGTH_SHORT).show();
                }

            }
        });

        /** 点击返回*/
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endAsync1(); //停止异步任务
                endAsync2(); //停止异步任务
                finish();
            }
        });

    }

    /**
     * 初始化
     */
    private void init(){
        iv_return = findViewById(R.id.iv_return);
        edtEmail = findViewById(R.id.edtEmail);
        edtTestCode = findViewById(R.id.edtTestCode);
        tv_getTestCode = findViewById(R.id.tv_getTestCode);
        tv_testCode = findViewById(R.id.tv_testCode);
        btnTest = findViewById(R.id.btnTest);

        okHttpClient = new OkHttpClient();

        //去掉顶部状态栏和底部导航栏
        FullScreen.NavigationBarStatusBar(BindingEmailActivity.this,true);
    }

    /**
     * 获取验证码
     */
    private void getTestCode(){
        tv_getTestCode.setClickable(false);
        endAsync2();

        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("email", edtEmail.getText().toString().trim()).build();
                final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/sendTestCodeBingEmail").build();
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

        /** 1分钟倒计时*/
        asyncTask = new GetTestCodeAsyncTask(getApplicationContext(),tv_getTestCode);
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        /** 2分钟倒计时*/
        testAsync = new TestCodeEffectAsyncTask(tv_testCode);
        testAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 完成
     */
    private void over(){
        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("accout", LoginActivity.user.getAccount()).add("email", edtEmail.getText().toString().trim()).build();
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
                        sendMessage(1,result);
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
     * 结束异步任务1分钟倒计时
     */
    private void endAsync1(){
        if(asyncTask != null && !asyncTask.isCancelled() && asyncTask.getStatus() == AsyncTask.Status.RUNNING){
            asyncTask.cancel(true);
            asyncTask = null;
        }
    }

    /**
     * 结束异步任务2分钟倒计时
     */
    private void endAsync2(){
        if(testAsync != null && !testAsync.isCancelled() && testAsync.getStatus() == AsyncTask.Status.RUNNING){
            testAsync.cancel(true);
            testAsync = null;
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
