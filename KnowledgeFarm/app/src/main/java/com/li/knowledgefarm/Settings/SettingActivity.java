package com.li.knowledgefarm.Settings;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

public class SettingActivity extends AppCompatActivity {
    /** 返回*/
    private ImageView iv_return;
    /** 修改昵称*/
    private Button btnUpdateNickName;
    /** 修改年级*/
    private Button btnUpdateGrade;
    /** 修改密码*/
    private Button btnUpdatePassword;
    /** 绑定QQ*/
    private Button btnBindingQQ;
    /** 解绑QQ*/
    private Button btnUnBindingQQ;
    /** 切换账号*/
    private Button btnRegout;
    /** OKHttpClient*/
    private OkHttpClient okHttpClient;
    /** 自定义点击事件监听器*/
    private CustomerListener listener;

    /** Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI*/
    private Tencent mTencent;
    /** 其中mAppId是分配给第三方应用的appid，类型为String*/
    public String mAppId = "101827462";//101827370
    /** 用户信息*/
    private UserInfo mInfo;
    /** 授权登录监听器*/
    private IUiListener loginListener;
    /** 获取用户信息监听器*/
    private IUiListener userInfoListener;
    private String openId;

    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0: //账号是否已被绑定判断
                    switch ((String)msg.obj){
                        case "true":
                            btnBindingQQ.setVisibility(View.GONE);
                            btnUnBindingQQ.setVisibility(View.VISIBLE);
                            break;
                        case "false":

                            break;
                    }
                    break;
                case 1: //绑定QQ判断
                    switch ((String)msg.obj){
                        case "true":
                            btnBindingQQ.setVisibility(View.GONE);
                            btnUnBindingQQ.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"绑定成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(getApplicationContext(),"绑定失败",Toast.LENGTH_SHORT).show();
                            break;
                        case "already":
                            Toast.makeText(getApplicationContext(),"该QQ号已被绑定",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 2: //解绑QQ判断
                    switch ((String)msg.obj){
                        case "true":
                            btnBindingQQ.setVisibility(View.VISIBLE);
                            btnUnBindingQQ.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"解绑成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(getApplicationContext(),"解绑失败",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /** 初始化*/
        getViews();

        /** 判断该账号是否已绑定QQ*/
        isBindingQQ();

    }

    class CustomerListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_return:
                    Intent intent = new Intent();
                    intent.setClass(SettingActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnUpdateNickName:
                    popupWindow_update_nickName();
                    break;
                case R.id.btnUpdateGrade:
                    popupWindow_update_grade();
                    break;
                case R.id.btnUpdatePassword:
                    popupWindow_update_password();
                    break;
                case R.id.btnBindingQQ:
                    bindingQQ();
                    break;
                case R.id.btnUnBindingQQ:

                    break;
                case R.id.btnRegout:
                    Toast.makeText(getApplicationContext(),"功能尚在开放",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * 初始化
     */
    private void getViews(){
        iv_return = findViewById(R.id.iv_return);
        btnUpdateNickName = findViewById(R.id.btnUpdateNickName);
        btnUpdateGrade = findViewById(R.id.btnUpdateGrade);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnBindingQQ = findViewById(R.id.btnBindingQQ);
        btnUnBindingQQ = findViewById(R.id.btnUnBindingQQ);
        btnRegout = findViewById(R.id.btnRegout);

        listener = new CustomerListener();
        iv_return.setOnClickListener(listener);
        btnUpdateNickName.setOnClickListener(listener);
        btnUpdateGrade.setOnClickListener(listener);
        btnUpdatePassword.setOnClickListener(listener);
        btnBindingQQ.setOnClickListener(listener);
        btnUnBindingQQ.setOnClickListener(listener);
        btnRegout.setOnClickListener(listener);

        /** 关闭状态栏*/
        setStatusBar();

        okHttpClient = new OkHttpClient();
        mTencent = Tencent.createInstance(mAppId, getApplicationContext());
    }

    /**
     * 弹出修改昵称窗口
     */
    private void popupWindow_update_nickName(){
        UpdateNickNameDialog popupDialogShopCar = new UpdateNickNameDialog(getApplicationContext(),"71007839");
        popupDialogShopCar.showAtLocation(findViewById(R.id.btnUpdateNickName), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        backgroundAlpha(0.3f);
        //监听弹出框关闭时，屏幕透明度变回原样
        popupDialogShopCar.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 弹出修改年级窗口
     */
    private void popupWindow_update_grade(){
        UpdateGradeDialog popupDialogShopCar = new UpdateGradeDialog(getApplicationContext(),"1");
        popupDialogShopCar.showAtLocation(findViewById(R.id.btnUpdateGrade), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        backgroundAlpha(0.3f);
        //监听弹出框关闭时，屏幕透明度变回原样
        popupDialogShopCar.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 弹出修改密码窗口
     */
    private void popupWindow_update_password(){
        UpdatePasswordDialog popupDialogShopCar = new UpdatePasswordDialog(getApplicationContext());
        popupDialogShopCar.showAtLocation(findViewById(R.id.btnUpdatePassword), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        backgroundAlpha(0.3f);
        //监听弹出框关闭时，屏幕透明度变回原样
        popupDialogShopCar.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 判断该账号是否已绑定QQ
     */
    private void isBindingQQ(){
        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("accout","").build();
                final Request request = new Request.Builder().post(formBody).url("http://"+getResources().getString(R.string.IP)+":8080/FarmKnowledge/user/isBindingQQ").build();
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
     * 拉起QQ登陆
     */
    private void bindingQQ(){
        if (!mTencent.isSessionValid()) {
            Log.i("lww", "onClickLogin session无效");

            loginListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject response = (JSONObject) o;
                    try {
                        int ret = response.getInt("ret");
                        if (ret == 0) {
                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            openId = response.getString("openid");
                            String accessToken = response.getString("access_token");
                            String expires = response.getString("expires_in");
                            Log.i("lww", "Openid:" + openId);

                            /** 存入SharedPreferences*/
                            SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("opId",openId);
                            editor.putString("tk",accessToken);
                            editor.putLong("start",new Date().getTime());
                            editor.putInt("expires", Integer.parseInt(expires));
                            editor.commit();

                            mTencent.setOpenId(openId);
                            mTencent.setAccessToken(accessToken, expires);

                            /** 获取QQ用户信息,并发送至后台*/
                            getAndTransferInfo();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    /**
     * 获取QQ用户信息,并发送至后台
     */
    private void getAndTransferInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            Log.i("lww", "updateUserInfo1");
            userInfoListener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }
                @Override
                public void onComplete(Object response) {
                    JSONObject json = (JSONObject) response;

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

                    /** 将openId、photo发送至后台*/
                    final String Path = URLEncoder.encode(path);
                    new Thread() {
                        @Override
                        public void run() {
                            FormBody formBody = new FormBody.Builder().add("accout","").add("openId",openId).add("photo",Path).build();
                            final Request request = new Request.Builder().post(formBody).url("http://"+getResources().getString(R.string.IP)+":8080/FarmKnowledge/user/bindingQQ").build();
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

    /**
     * 解绑QQ
     */
    private void unBindingQQ(){
        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("accout","").add("openId",openId).build();
                final Request request = new Request.Builder().post(formBody).url("http://"+getResources().getString(R.string.IP)+":8080/FarmKnowledge/user/unBindingQQ").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        sendMessage(2,result);
                    }
                });
            }
        }.start();
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
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
