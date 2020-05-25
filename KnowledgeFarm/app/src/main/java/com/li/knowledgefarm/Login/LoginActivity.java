package com.li.knowledgefarm.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import cn.jpush.android.api.JPushInterface;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.li.knowledgefarm.Study.Util.AppUtil;
import com.li.knowledgefarm.Study.Util.setDensityLand;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.jetbrains.annotations.NotNull;
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
    public String mAppId = "1110065654";//101827370 1110127580
    /**
     * 授权登录监听器
     */
    private IUiListener loginListener;
    /**
     * 获取用户信息监听器
     */
    private IUiListener userInfoListener;

    private String openID;
    private String accessToken;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("WrongConstant")
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 1:       //第一次登录
                    CustomerToast.getInstance(getApplicationContext(),"欢迎你，新用户",Toast.LENGTH_SHORT).show();
                    Intent intentFirst = new Intent(LoginActivity.this,QQFirstActivity.class);
                    intentFirst.putExtra("opId",openID);
                    intentFirst.putExtra("nick",Nickname);
                    intentFirst.putExtra("photo",Path);
                    startActivity(intentFirst);
                    break;
                case 2:       //账号失效
                    CustomerToast.getInstance(getApplicationContext(),"账号已失效！",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    //自动登录
                    CustomerToast.getInstance(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    UserUtil.setUser((User) msg.obj);
                    Intent autoToStart = new Intent(LoginActivity.this,StartActivity.class);
                    autoToStart.setAction("autoLogin");
                    startActivity(autoToStart);
                    finish();
                    break;
                case 0:
                    // 其他情况
                    finish();
                    break;
                case 4:
                    //  授权登录
                    onClickLogin();
                    break;
            }
        }
    };
    private String Nickname;
    private String Path;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDensityLand.setDensity(getApplication());
        setDensityLand.setOrientation(this, AppUtil.HEIGHT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        okHttpClient = OkHttpUtils.getInstance(this);
        UserUtil.setUser(null);
        autoLogin();
        FullScreen.NavigationBarStatusBar(LoginActivity.this,true);
        getViews();
        registListener();
//        XXPermissions.with(this)
//            // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
//            //.constantRequest()
//            // 支持请求6.0悬浮窗权限8.0请求安装权限
//            //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES)
//            // 不指定权限则自动获取清单中的危险权限
//            .request(new OnPermission() {
//
//                @Override
//                public void hasPermission(List<String> granted, boolean all) {
//
//                }
//
//                @Override
//                public void noPermission(List<String> denied, boolean quick) {
//
//                }
//            });
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
            CustomerToast.getInstance(LoginActivity.this, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
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
                            editor.apply();
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
    //自动登录->访问服务器
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
    //授权（点击）登录
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
                Call call = okHttpClient.newCall(request);
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
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String result =  response.body().string();
                        Message message = new Message();
                        if (result.equals("notEffect")) {
                            message.what = 2;             //账号失效
                            mHandler.sendMessage(message);
                        } else if(result.equals("notExist")){//第一次登录
                            message.what = 1;
                            mHandler.sendMessage(message);
                        }else {                          //非第一次登录，自动
                            message.what = 3;
                            message.obj = parsr(URLDecoder.decode(result), User.class);
                            UserUtil.setUser((User) message.obj);
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
        }.start();

    }

    //自动登录
    private void asyncByJson2(final String urlPath, final String Json) {
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
                Call call =okHttpClient.newCall(request);
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
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result =  response.body().string();
                        Message message = new Message();
                        if (result.equals("notEffect")) { //账号失效
                            message.what = 2;
                            mHandler.sendMessage(message);
                        } else if(result.equals("notExist")){//第一次登录
                            ;
                        }else {
                            message.what = 3;        //非第一次登录，自动登录
                            message.obj = parsr(URLDecoder.decode(result), User.class);
                            UserUtil.setUser((User) message.obj);
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
