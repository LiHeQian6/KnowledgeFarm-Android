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
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private TextView getCode;
    private Button btnSure;
    private LinearLayout linearCode;
    private LinearLayout linearPwd;
    private EditText newPwdEdt;
    private EditText configPwdEdt;
    private String newPwd;
    private String configPwd;
    private Button resetPwd;
    private findPwdListener listener;
    private LinearLayout layout_account;
    private LinearLayout layout_email;
    private LinearLayout layout_code;
    private LinearLayout layout_button;
    private ImageView returnPwd;
    private int displayWidth;
    private int displayHeight;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 5:
                    CustomerToast.getInstance(getApplicationContext(),"验证码已发送，请在邮箱查收",Toast.LENGTH_SHORT).show();
                    identyCode = (String) msg.obj;
                    availableCode();
                    startCode();
                    break;
                case 6:
                    CustomerToast.getInstance(getApplicationContext(),"未绑定邮箱！",Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    CustomerToast.getInstance(getApplicationContext(),"邮箱错误！",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    CustomerToast.getInstance(getApplicationContext(),"账号不存在！",Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    CustomerToast.getInstance(getApplicationContext(),"密码修改成功！",Toast.LENGTH_SHORT).show();
                    Intent intentToAccountLogin = new Intent(FindPassword.this,LoginByAccountActivity.class);
                    startActivity(intentToAccountLogin);
                    finish();
                    break;
                case 9:
                    CustomerToast.getInstance(getApplicationContext(),"密码修改失败！",Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    CustomerToast.getInstance(getApplicationContext(),"发送失败！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        //设置横屏
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        okHttpClient = OkHttpUtils.getInstance(this);
        FullScreen.NavigationBarStatusBar(FindPassword.this,true);
        getViews();
        registListener();
        setSizeForView();
    }

    /**
     * @Description 控件适配屏幕
     * @Auther 孙建旺
     * @Date 下午 6:30 2019/12/13
     * @Param []
     * @return void
     */
    private void setSizeForView() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int)(displayWidth*0.35), 120);
        params.setMargins(0,(int)(displayHeight*0.08),0,0);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        layout_account.setLayoutParams(params);

        LinearLayout.LayoutParams paramsEmail = new LinearLayout.LayoutParams(
                (int)(displayWidth*0.35), 120);
        paramsEmail.setMargins(0,(int)(displayHeight*0.05),0,0);
        paramsEmail.gravity = Gravity.CENTER_HORIZONTAL;
        layout_email.setLayoutParams(paramsEmail);

        LinearLayout.LayoutParams paramsCode = new LinearLayout.LayoutParams(
                (int)(displayWidth*0.35), 120);
        paramsCode.setMargins(0,(int)(displayHeight*0.05),0,0);
        paramsCode.gravity = Gravity.CENTER_HORIZONTAL;
        layout_code.setLayoutParams(paramsCode);

        LinearLayout.LayoutParams paramsSure = new LinearLayout.LayoutParams(
                (int)(displayWidth*0.25), 120);
        paramsSure.setMargins(0,(int)(displayHeight*0.08),0,0);
        paramsSure.gravity = Gravity.CENTER_HORIZONTAL;
        layout_button.setLayoutParams(paramsSure);

        accountEdt.setTextSize(TypedValue.COMPLEX_UNIT_SP,(int)(displayWidth*0.009));
        emailEdt.setTextSize(TypedValue.COMPLEX_UNIT_SP,(int)(displayWidth*0.009));
        codeEdt.setTextSize(TypedValue.COMPLEX_UNIT_SP,(int)(displayWidth*0.009));
        getCode.setTextSize(TypedValue.COMPLEX_UNIT_SP,(int)(displayWidth*0.008));

        LinearLayout.LayoutParams params_back = new LinearLayout.LayoutParams((int)(displayWidth*0.07),(int)(displayHeight*0.14));
        params_back.setMargins((int)(displayWidth*0.02),(int)(displayHeight*0.03),0,0);
        returnPwd.setLayoutParams(params_back);
    }

    private void registListener() {
        listener = new findPwdListener();
        getCode.setOnClickListener(listener);
        btnSure.setOnClickListener(listener);
        resetPwd.setOnClickListener(listener);
        returnPwd.setOnClickListener(listener);
    }

    private void getViews() {
        accountEdt = findViewById(R.id.findPwdAccount);

        emailEdt = findViewById(R.id.findPwdEmail);

        codeEdt = findViewById(R.id.identifyCode);
        returnPwd = findViewById(R.id.findPwdReturn);
        getCode = findViewById(R.id.getCode);
        btnSure = findViewById(R.id.btnSubmit);
        newPwdEdt = findViewById(R.id.newPwd);
        configPwdEdt = findViewById(R.id.configNewPwd);
        linearCode = findViewById(R.id.linearCode);
        linearPwd = findViewById(R.id.linearFindPwd);
        resetPwd = findViewById(R.id.btnFindPwd);
        layout_account = findViewById(R.id.layout_account);
        layout_email = findViewById(R.id.layout_email);
        layout_code = findViewById(R.id.layout_code);
        layout_button = findViewById(R.id.layout_button);
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
                        CustomerToast.getInstance(getApplicationContext(),"账号或邮箱为空！",Toast.LENGTH_SHORT).show();
                        return;
                    }else if(!isEmail(email)){
                        CustomerToast.getInstance(getApplicationContext(),"邮箱格式输入错误！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getCodeFromServer(getResources().getString(R.string.URL)+"/user/sendTestCodePassword");

                    break;
                case R.id.btnSubmit:
                    if(account.equals("")||email.equals("")||code.equals("")){
                        CustomerToast.getInstance(getApplicationContext(),"请完善所有信息！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!identyCode.equals("")&&code.equals(identyCode)){
                        linearCode.setVisibility(View.GONE);
                        linearPwd.setVisibility(View.VISIBLE);
                    }else if (!code.equals(identyCode)&&!identyCode.equals("")){
                        CustomerToast.getInstance(getApplicationContext(),"验证码输入错误",Toast.LENGTH_SHORT).show();
                    }else if(identyCode.equals("")){
                        CustomerToast.getInstance(getApplicationContext(),"验证码已失效",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnFindPwd:
                    if(newPwd.equals("")){
                        CustomerToast.getInstance(getApplicationContext(),"密码输入为空！",Toast.LENGTH_SHORT).show();
                    }else if(!newPwd.equals(configPwd)){
                        CustomerToast.getInstance(getApplicationContext(),"密码输入不一致！",Toast.LENGTH_SHORT).show();
                    }else if(newPwd.length()<8||newPwd.length()>14){
                        CustomerToast.getInstance(getApplicationContext(),"请输入8-14位字符",Toast.LENGTH_SHORT).show();
                        return;
                    }else if(newPwd.contains(" ")){
                        CustomerToast.getInstance(getApplicationContext(),"含有非法字符",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        resetPwdToServer(getResources().getString(R.string.URL)+"/user/resetUserPassword");
                    }
                    break;
                case R.id.findPwdReturn:
                    finish();
                    break;
            }
        }
    }

    //访问服务器，重置密码
    private void resetPwdToServer(String s) {

        FormBody formBody = new FormBody.Builder()
                .add("password",stringMD5(newPwd))
                .build();
        Request request = new Request.Builder().post(formBody).url(s).build();
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
                String result = response.body().string();
                Log.e("rs",result);
                Message message = new Message();
                if(result.equals("true")){
                    message.what = 8;                 //修改成功
                    handler.sendMessage(message);
                }else {
                    message.what = 9;                //修改失败
                    handler.sendMessage(message);
                }
            }
        });
    }

    //访问服务器，获取验证码
    private void getCodeFromServer(final String urlPath) {
        FormBody formBody = new FormBody.Builder()
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                String result = response.body().string();
                Log.e("rs",result);
                Message message = new Message();
                if(result.equals("notExistAccount")){             //账号不存在
                    message.what = 4;
                    handler.sendMessage(message);
                }else if(result.equals("notBindingEmail")){       //账号未绑定邮箱
                    message.what = 6;
                    handler.sendMessage(message);
                }else if(result.equals("EmailError")){           //邮箱错误
                    message.what = 7;
                    handler.sendMessage(message);
                }else if(result.equals("fail")){                // 验证码发送失败
                    message.what = 10;
                    handler.sendMessage(message);
                } else {
                    message.what = 5;                         //验证码发送成功
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
                getCode.setText("获取验证码");
                getCode.setTextColor(Color.parseColor("#1E90FF"));
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    private void availableCode() {
        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected void onPreExecute() {
                // 准备执行前调用，用于界面初始化操作

            }

            @Override
            protected Integer doInBackground(Integer... params) {
                // 子线程，耗时操作
                int start = 0;
                int end = 299;
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

            };

            @Override
            protected void onPostExecute(Integer result) {
                identyCode = "";
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
