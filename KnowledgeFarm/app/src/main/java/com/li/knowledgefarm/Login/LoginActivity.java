package com.li.knowledgefarm.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.entity.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import com.li.knowledgefarm.R;

public class LoginActivity extends AppCompatActivity {
    /**
     * 知识农场logo
     */
    private TextView logo;
    /**
     * 点击第三方登陆按钮
     */
    private Button btnQQFirst;
    private Button btnAccount;
    /**
     * 点击注销按钮
     */
    private LinearLayout linearUser;
    private LinearLayout linearQQ;
    private LinearLayout linearStart;
    /**
     * 用户信息
     */
    private UserInfo mInfo;
    /**
     * 点击事件监听器
     */
    private CustomerListener listener;
    /**
     * Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI
     */
    public static Tencent mTencent;
    /**
     * 其中mAppId是分配给第三方应用的appid，类型为String
     */
    public String mAppId = "101827462";//101827370
    /**
     * 授权登录监听器
     */
    private IUiListener loginListener;
    /**
     * 获取用户信息监听器
     */
    private IUiListener userInfoListener;

    /**
     * 用户表
     */
    public static User user;
    private String openID;
    private String accessToken;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("WrongConstant")
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 1:       //第一次登录
                    Toast.makeText(getApplicationContext(),"欢迎你，新用户",Toast.LENGTH_SHORT).show();
                    Intent intentFirst = new Intent(LoginActivity.this,QQFirstActivity.class);
                    intentFirst.putExtra("opId",openID);
                    intentFirst.putExtra("nick",Nickname);
                    intentFirst.putExtra("photo",Path);
                    startActivity(intentFirst);
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),"账号已失效！",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    //自动登录
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    user = (User) msg.obj;
                    Intent autoToStart = new Intent(LoginActivity.this,StartActivity.class);
                    autoToStart.setAction("autoLogin");
                    startActivity(autoToStart);
                    finish();
                    break;
                case 0:
                    finish();
                    break;
                case 4:
                    onClickLogin();
                    break;
            }
        }
    };
    private String Nickname;
    private String Path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = null;
        autoLogin();
        setStatusBar();
        getViews();
        registListener();
    }

    /**
     * 初始化视图
     */
    private void getViews() {
        logo = findViewById(R.id.logo);
        btnQQFirst = findViewById(R.id.btnQQFirst);
        btnAccount = findViewById(R.id.btnAccount);
        linearUser = findViewById(R.id.linearUser);
        linearQQ = findViewById(R.id.linearQQ);
        linearStart = findViewById(R.id.linearStart);
        mTencent = Tencent.createInstance(mAppId, getApplicationContext());
    }

    /**
     * 注册点击事件监听器
     */
    private void registListener() {
        listener = new CustomerListener();
        btnQQFirst.setOnClickListener(listener);
        btnAccount.setOnClickListener(listener);
    }

    /**
     * 点击事件的处理
     */
    class CustomerListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                /** 点击登录*/
                case R.id.btnQQFirst:
                    new Thread(){
                        @Override
                        public void run() {
                            if(!isConnByHttp()){
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(),"未连接服务器",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                return;
                            }
                            Message message = new Message();
                            message.what = 4;
                            mHandler.sendMessage(message);
                        }
                    }.start();
                    break;
                case R.id.btnAccount:
                    Intent intentLoginByAccount = new Intent(LoginActivity.this, LoginByAccountActivity.class);
                    startActivity(intentLoginByAccount);
                    break;
            }
        }
    }
    //退出时的时间
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //退出
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(LoginActivity.this, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    //自动登录
    private void autoLogin(){
        SharedPreferences sp = getSharedPreferences("token", MODE_PRIVATE);
        String opId = sp.getString("opId",null);
        String token = sp.getString("tk",null);
        Long start = sp.getLong("start",0);
        Long end = new Date().getTime();
        int expires = sp.getInt("expires",0);
        Log.e("time",start+":"+end+":"+expires);
        if(end - start < expires && token != null && opId != null){
            judgeAuths(opId);
        }
    }
    /**
     * 登陆
     */
    public void onClickLogin() {

        //登陆
        if (!mTencent.isSessionValid()) {
            Log.i("lww", "onClickLogin session无效");

            loginListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject response = (JSONObject) o;
                    Log.i("lww", "返回登录消息：" + response.toString());
                    try {
                        int ret = response.getInt("ret");
                        if (ret == 0) {
                            openID = response.getString("openid");
                            Log.i("lww", "Openid:" + openID);
                            accessToken = response.getString("access_token");
                            String expires = response.getString("expires_in");
                            SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("opId",openID);
                            editor.putString("tk",accessToken);
                            editor.putLong("start",new Date().getTime());
                            editor.putInt("expires", Integer.parseInt(expires));
                            editor.commit();
                            mTencent.setOpenId(openID);
                            mTencent.setAccessToken(accessToken, expires);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //更新用户信息
                    new Thread(){
                        @Override
                        public void run() {
                            updateUserInfo();
                        }
                    }.start();
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            };

            mTencent.login(this, "all", loginListener, true);//（true）设置允许扫码

        } else {
            Log.i("lww", "onClickLogin session有效");
        }
    }
    private void judgeAuths(final String openId){
        new Thread() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("openId",openId);
                    jsonObject.put("nickName","");
                    jsonObject.put("photo","");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                asyncByJson2(getResources().getString(R.string.URL)+"/user/loginByOpenId",
                        jsonObject.toString());

            }
        }.start();
    }

    /**
     * 更新用户信息
     */
    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            Log.i("lww", "updateUserInfo1");
            userInfoListener = new IUiListener() {

                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(Object response) {
                    JSONObject json = (JSONObject) response;
                    Log.i("lww", "返回用户消息：" + response.toString());
                    /** 昵称*/
                    String nkname = null;
                    try {
                        nkname = ((JSONObject) response).getString("nickname");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /** 头像*/
                    String npath = null;
                    try {
                        npath = json.getString("figureurl_qq_2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Nickname = nkname;
                    Path = URLEncoder.encode(npath);
                    new Thread() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("openId",openID);
                                jsonObject.put("nickName",Nickname);
                                jsonObject.put("photo",Path);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            asyncByJson(getResources().getString(R.string.URL)+"/user/loginByOpenId",
                                    jsonObject.toString());

                        }
                    }.start();
                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(userInfoListener);
        } else {
            Log.i("lww", "updateUserInfo2");
        }
    }

    private void asyncByJson(final String urlPath, final String Json) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                MediaType type = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(Json,type);
                Request request = new Request.Builder()
                        .url(urlPath)
                        .post(body)
                        .build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("jing", "请求失败");
                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result =  response.body().string();
                        Message message = new Message();
                        if (result.equals("notEffect")) {
                            message.what = 2;
                            mHandler.sendMessage(message);
                        } else if(result.equals("notExist")){//第一次登录
                            message.what = 1;
                            mHandler.sendMessage(message);
                        }else {
                            message.what = 3;
                            message.obj = parsr(URLDecoder.decode(result), User.class);
                            user = (User) message.obj;
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
        }.start();

    }

    private void asyncByJson2(final String urlPath, final String Json) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                if(!isConnByHttp()){
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),"未连接服务器",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                MediaType type = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(Json,type);
                Request request = new Request.Builder()
                        .url(urlPath)
                        .post(body)
                        .build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("jing", "请求失败");
                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result =  response.body().string();
                        Message message = new Message();
                        if (result.equals("notEffect")) {
                            message.what = 2;
                            mHandler.sendMessage(message);
                        } else if(result.equals("notExist")){//第一次登录
                            ;
                        }else {
                            message.what = 3;
                            message.obj = parsr(URLDecoder.decode(result), User.class);
                            user = (User) message.obj;
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
        }.start();

    }


    /**
     * Json转换为对象
     */
    public static <T> T parsr(String json, Class<T> tClass) {
        //判读字符串是否为空
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, tClass);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                mTencent.handleResultData(data, loginListener);
            }
        }
    }


    /**
     * @Description 设置状态栏
     * @Auther 孙建旺
     * @Date 下午 2:28 2019/12/09
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

    private boolean isConnByHttp(){
        boolean isConn = false;
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(getResources().getString(R.string.URL));
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(1000*5);
            if(conn.getResponseCode()==200){
                isConn = true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            conn.disconnect();
        }
        return isConn;
    }
}
