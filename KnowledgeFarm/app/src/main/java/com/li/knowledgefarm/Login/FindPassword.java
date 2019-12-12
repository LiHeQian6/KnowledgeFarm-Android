package com.li.knowledgefarm.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.li.knowledgefarm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindPassword extends AppCompatActivity {
    private String email;
    private String account;
    private String code;
    private String identyCode;
    private EditText emailEdt;
    private EditText accountEdt;
    private EditText codeEdt;
    private Button getCode;
    private Button btnSure;
    private LinearLayout linearCode;
    private LinearLayout linearPwd;
    private EditText newPwdEdt;
    private EditText configPwdEdt;
    private String newPwd;
    private String configPwd;
    private Button resetPwd;
    private findPwdListener listener;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 5:
                    Toast.makeText(getApplicationContext(),"验证码已发送，请在邮箱查收",Toast.LENGTH_SHORT).show();
                    identyCode = (String) msg.obj;
                    startCode();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(),"未绑定邮箱！",Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(getApplicationContext(),"邮箱错误！",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(),"账号不存在！",Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(getApplicationContext(),"密码修改成功！",Toast.LENGTH_SHORT).show();
                    Intent intentToAccountLogin = new Intent(FindPassword.this,LoginByAccountActivity.class);
                    startActivity(intentToAccountLogin);
                    finish();
                    break;
                case 9:
                    Toast.makeText(getApplicationContext(),"密码修改失败！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        //设置横屏
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        getViews();
        registListener();
    }

    private void registListener() {
        listener = new findPwdListener();
        getCode.setOnClickListener(listener);
        btnSure.setOnClickListener(listener);
        resetPwd.setOnClickListener(listener);
    }

    private void getViews() {
        accountEdt = findViewById(R.id.findPwdAccount);

        emailEdt = findViewById(R.id.findPwdEmail);

        codeEdt = findViewById(R.id.identifyCode);

        getCode = findViewById(R.id.getCode);
        btnSure = findViewById(R.id.btnSubmit);
        newPwdEdt = findViewById(R.id.newPwd);
        configPwdEdt = findViewById(R.id.configNewPwd);
        linearCode = findViewById(R.id.linearCode);
        linearPwd = findViewById(R.id.linearFindPwd);
        resetPwd = findViewById(R.id.btnFindPwd);

    }
    class findPwdListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            account = accountEdt.getText().toString();
            email = emailEdt.getText().toString();
            code = codeEdt.getText().toString();
            newPwd = newPwdEdt.getText().toString();
            configPwd = configPwdEdt.getText().toString();
            switch (view.getId()){
                case R.id.getCode:
                    if(account.equals("")||email.equals("")){
                        Toast.makeText(getApplicationContext(),"账号或邮箱为空！",Toast.LENGTH_SHORT).show();
                        return;
                    }else if(!isEmail(email)){
                        Toast.makeText(getApplicationContext(),"邮箱格式输入错误！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getCodeFromServer(getResources().getString(R.string.URL)+"/user/sendTestCodePassword");
                    break;
                case R.id.btnSubmit:
                    if(!identyCode.equals("")&&code.equals(identyCode)){
                        linearCode.setVisibility(View.GONE);
                        linearPwd.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btnFindPwd:
                    if(newPwd.equals("")){
                        Toast.makeText(getApplicationContext(),"密码输入为空！",Toast.LENGTH_SHORT).show();
                    }else if(!newPwd.equals(configPwd)){
                        Toast.makeText(getApplicationContext(),"密码输入不一致！",Toast.LENGTH_SHORT).show();
                    }else {
                        resetPwdToServer(getResources().getString(R.string.URL)+"/user/resetUserPassword");
                    }
                    break;
            }
        }
    }

    private void resetPwdToServer(String s) {

        FormBody formBody = new FormBody.Builder()
                .add("accout", account)
                .add("password",newPwd)
                .build();
        Request request = new Request.Builder().post(formBody).url(s).build();
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
                String result = response.body().string();
                Log.e("rs",result);
                Message message = new Message();
                if(result.equals("true")){
                    message.what = 8;
                    handler.sendMessage(message);
                }else {
                    message.what = 9;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private void getCodeFromServer(final String urlPath) {
        FormBody formBody = new FormBody.Builder()
                .add("accout", account)
                .add("email", email)
                .build();
        Request request = new Request.Builder().post(formBody).url(urlPath).build();
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
                String result = response.body().string();
                Log.e("rs",result);
                Message message = new Message();
                if(result.equals("notExistAccount")){
                    message.what = 4;
                    message.obj = result;
                    handler.sendMessage(message);
                }else if(result.equals("notBindingEmail")){
                    message.what = 6;
                    message.obj = result;
                    handler.sendMessage(message);
                }else if(result.equals("EmailError")){
                    message.what = 7;
                    message.obj = result;
                    handler.sendMessage(message);
                } else {
                    message.what = 5;
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //判断是否邮箱
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 执行倒计时操作
     */
    @SuppressLint("StaticFieldLeak")
    private void startCode() {
        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected void onPreExecute() {
                // 准备执行前调用，用于界面初始化操作
                getCode.setEnabled(false);
                getCode.setText("60s");
                getCode.setTextColor(Color.parseColor("#1E90FF"));
            }

            @Override
            protected Integer doInBackground(Integer... params) {
                // 子线程，耗时操作
                int start = 0;
                int end = 59;
                int result = 0;
                for (int i = end; i >= start; i--) {
                    SystemClock.sleep(1000);
                    result = i;
                    publishProgress(result);//把进度推出去，推给onProgressUpdate参数位置
                }
                return result;
            }

            @Override
            protected void onProgressUpdate(Integer[] values) {
                int progress = values[0];
                getCode.setText(progress+"s");
            };

            @Override
            protected void onPostExecute(Integer result) {
                getCode.setEnabled(true);
                identyCode = "";
                getCode.setText("获取验证码");
                getCode.setTextColor(Color.parseColor("#1E90FF"));
            }

        }.execute(0,100);
    }
}
