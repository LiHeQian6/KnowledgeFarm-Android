package com.li.knowledgefarm.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.li.knowledgefarm.Login.dialog.NotifyAccountDialog;
import com.li.knowledgefarm.MainActivity;
import com.li.knowledgefarm.Shop.ShopActivity;
import com.li.knowledgefarm.entity.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import android.os.Bundle;

import com.li.knowledgefarm.R;

public class LoginActivity extends AppCompatActivity {
    /**
     * 知识农场logo
     */
    private TextView logo;
    /**
     * 用户昵称
     */
    private TextView tvUserName;
    /**
     * 用户头像
     */
    private ImageView ivUserImage;
    /**
     * 点击第三方登陆按钮
     */
    private Button btnQQ;
    private Button btnAccount;
    /**
     * 点击注销按钮
     */
    private Button btnRegout;
    private LinearLayout linearUser;
    private LinearLayout linearQQ;
    private LinearLayout linearStart;
    private Button btnStart;
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
    private Tencent mTencent;
    /**
     * 其中mAppId是分配给第三方应用的appid，类型为String
     */
    public String mAppId = "101827462";//101827370
    public static String serverURL = "http://10.7.87.220:8080/FarmKnowledge/";
    /**
     * 授权登录监听器
     */
    private IUiListener loginListener;
    /**
     * 获取用户信息监听器
     */
    private IUiListener userInfoListener;

    /*
        用户表
     */
    public static User user;
    private String openID;
    private String accessToken;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 1:       //登录成功
                    logo.setVisibility(View.INVISIBLE);
                    linearUser.setVisibility(View.VISIBLE);
                    linearQQ.setVisibility(View.INVISIBLE);
                    linearStart.setVisibility(View.VISIBLE);
                    user = (User) msg.obj;
                    tvUserName.setText(user.getNickName());
                    urlToImgBitmap(user.getPhoto());
                    showNotifyDialog();
                    break;
                case 2:     //返回头像的Bitmap
                    Bitmap bitmap = (Bitmap) msg.obj;
                    ivUserImage.setImageBitmap(bitmap);
                    break;
                case 0:     //登录失败
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = null;
        //设置横屏
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        getViews();
        autoLogin();
        registListener();
    }
    /**
     * 初始化视图
     */
    private void getViews() {
        logo = findViewById(R.id.logo);
        btnRegout = findViewById(R.id.btnRegout);
        btnStart = findViewById(R.id.btnStart);
        btnQQ = findViewById(R.id.btnQQ);
        btnAccount = findViewById(R.id.btnAccount);
        tvUserName = findViewById(R.id.tvUserName);
        ivUserImage = findViewById(R.id.ivUserImage);
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
        btnRegout.setOnClickListener(listener);
        btnQQ.setOnClickListener(listener);
        btnStart.setOnClickListener(listener);
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
                case R.id.btnQQ:
                    onClickLogin();
                    break;
                /** 点击注销*/
                case R.id.btnRegout:
                    regout();
                    break;
                case R.id.btnStart:
                    Intent intentStartGame = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intentStartGame);
                    break;
                case R.id.btnAccount:
                    Intent intentLoginByAccount = new Intent(LoginActivity.this, LoginByAccountActivity.class);
                    startActivity(intentLoginByAccount);
                    break;
                case R.id.registAccount:
                    break;
            }
        }
    }

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
                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
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
                    updateUserInfo();
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
                doJsonPost(serverURL+"user/loginByOpenId",
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
                    String nickname = null;
                    try {
                        nickname = ((JSONObject) response).getString("nickname");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /** 头像*/
                    String path = null;
                    try {
                        path = json.getString("figureurl_qq_2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String Nickname = nickname;
                    final String Path = URLEncoder.encode(path);
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
                            doJsonPost(serverURL+"user/loginByOpenId",
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

    private String doJsonPost(String urlPath, String Json) {
        // HttpClient 6.0被抛弃了
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept", "application/json");
            // 往服务器里面发送数据
            if (Json != null && !TextUtils.isEmpty(Json)) {
                byte[] writebytes = Json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream out = conn.getOutputStream();
                out.write(Json.getBytes());
                out.flush();
                out.close();
            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
                Log.e("re", result);
                Log.e("result", URLDecoder.decode(result));
                Message message = new Message();
                if (result.equals("false")) {
                    message.what = 0;
                    mHandler.sendMessage(message);
                } else {
                    message.what = 1;
                    message.obj = parsr(URLDecoder.decode(result), User.class);
                    user = (User) message.obj;
                    mHandler.sendMessage(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * 注销
     */
    private void regout() {
        //注销登陆
        mTencent.logout(getApplicationContext());

        SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        user = null;
        //更新视图
        logo.setVisibility(View.VISIBLE);
        linearUser.setVisibility(View.INVISIBLE);
        linearQQ.setVisibility(View.VISIBLE);
        linearStart.setVisibility(View.INVISIBLE);
    }


    /**
     * 开启线程，根据头像的url获取bitmap
     */
    public void urlToImgBitmap(final String imageUrl) {
        new Thread(){
            @Override
            public void run() {
                //显示网络上的图片
                Bitmap bitmap = null;
                HttpURLConnection conn = null;
                InputStream is = null;
                try {
                    URL myFileUrl = new URL(imageUrl);
                    conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    Message message = new Message();
                    message.obj = bitmap;
                    message.what = 2;
                    mHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    conn.disconnect();
                }
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
        Log.i("lww", "onActivityResult1");
        mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        if (requestCode == Constants.REQUEST_API) {
            Log.i("lww", "onActivityResult2");
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                Log.i("lww", "onActivityResult3");
                mTencent.handleResultData(data, loginListener);
            }
        }
    }
    private void showNotifyDialog() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NotifyAccountDialog notifyAccountDialog = new NotifyAccountDialog();
        if(!notifyAccountDialog.isAdded()){
            transaction.add(notifyAccountDialog,"notify");
        }
        transaction.show(notifyAccountDialog);
        transaction.commit();
    }
}
