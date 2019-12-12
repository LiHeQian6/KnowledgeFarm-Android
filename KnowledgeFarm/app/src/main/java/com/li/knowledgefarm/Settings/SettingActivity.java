package com.li.knowledgefarm.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    /** 绑定邮箱*/
    private Button btnBindingEmail;
    /** 解绑邮箱*/
    private Button btnUnBindingEmail;
    /** QQ信息*/
    private TextView tv_QQ;
    /** 邮箱信息*/
    private TextView tv_email;
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
    private String accessToken;
    private String expires;

    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0: //账号是否已被绑定QQ判断
                    switch ((String)msg.obj){
                        case "true":
                            btnBindingQQ.setVisibility(View.GONE);
                            btnUnBindingQQ.setVisibility(View.VISIBLE);
                            tv_QQ.setVisibility(View.VISIBLE);
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
                            tv_QQ.setVisibility(View.VISIBLE);
                            /** 存入SharedPreferences*/
                            SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("opId",openId);
                            editor.putString("tk",accessToken);
                            editor.putLong("start",new Date().getTime());
                            editor.putInt("expires", Integer.parseInt(expires));
                            editor.commit();
                            Toast.makeText(getApplicationContext(),"绑定QQ成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(getApplicationContext(),"绑定QQ失败",Toast.LENGTH_SHORT).show();
                            break;
                        case "already":
                            Toast.makeText(getApplicationContext(),"该QQ号已被其他账号绑定",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 2: //解绑QQ判断
                    switch ((String)msg.obj){
                        case "true":
                            btnBindingQQ.setVisibility(View.VISIBLE);
                            btnUnBindingQQ.setVisibility(View.GONE);
                            tv_QQ.setVisibility(View.INVISIBLE);
                            /** 删除SharedPreferences内Token信息*/
                            SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.commit();
                            Toast.makeText(getApplicationContext(),"解绑QQ成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(getApplicationContext(),"解绑QQ失败",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 3: //绑定邮箱判断
                    break;
                case 4: //解绑邮箱判断
                    switch ((String)msg.obj){
                        case "true":
                            LoginActivity.user.setEmail("");
                            btnBindingEmail.setVisibility(View.VISIBLE);
                            btnUnBindingEmail.setVisibility(View.GONE);
                            tv_email.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"解绑邮箱成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(getApplicationContext(),"解绑邮箱失败",Toast.LENGTH_SHORT).show();
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

        /** 请求服务器，判断该账号是否已绑定QQ，返回并刷新界面*/
        isBindingQQ();

        /** 判断是否已绑定邮箱，再刷新界面*/
        isBindingEmail();

    }

    class CustomerListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_return:
                    finish();
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
                    showAlertDialogQQ();
                    break;
                case R.id.btnBindingEmail:
                    bindingEmail();
                    break;
                case R.id.btnUnBindingEmail:
                    showAlertDialogEmail();
                    break;
                case R.id.btnRegout:
                    regout();
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
        btnBindingEmail = findViewById(R.id.btnBindingEmail);
        btnUnBindingEmail = findViewById(R.id.btnUnBindingEmail);
        tv_QQ = findViewById(R.id.tv_QQ);
        tv_email = findViewById(R.id.tv_email);
        btnRegout = findViewById(R.id.btnRegout);

        listener = new CustomerListener();
        iv_return.setOnClickListener(listener);
        btnUpdateNickName.setOnClickListener(listener);
        btnUpdateGrade.setOnClickListener(listener);
        btnUpdatePassword.setOnClickListener(listener);
        btnBindingQQ.setOnClickListener(listener);
        btnUnBindingQQ.setOnClickListener(listener);
        btnBindingEmail.setOnClickListener(listener);
        btnUnBindingEmail.setOnClickListener(listener);
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
        UpdateNickNameDialog popupDialogShopCar = new UpdateNickNameDialog(getApplicationContext());
        popupDialogShopCar.setWidth(getSyetemWidth()*1/2);
        popupDialogShopCar.setHeight(getSyetemHeight()*3/4);
        popupDialogShopCar.showAtLocation(btnUpdateNickName, Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
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
        UpdateGradeDialog popupDialogShopCar = new UpdateGradeDialog(getApplicationContext());
        popupDialogShopCar.setWidth(getSyetemWidth()*1/2);
        popupDialogShopCar.setHeight(getSyetemHeight()*3/4);
        popupDialogShopCar.showAtLocation(findViewById(R.id.btnUpdateGrade), Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
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
        popupDialogShopCar.showAtLocation(findViewById(R.id.btnUpdatePassword), Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
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
                FormBody formBody = new FormBody.Builder().add("accout",LoginActivity.user.getAccout()).build();
                final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/isBindingQQ").build();
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
     * 判断是否已绑定邮箱，再修改页面
     */
    private void isBindingEmail(){
        if(!LoginActivity.user.getEmail().equals("")){
            btnBindingEmail.setVisibility(View.GONE);
            btnUnBindingEmail.setVisibility(View.VISIBLE);
            tv_email.setVisibility(View.VISIBLE);
            tv_email.setText("账号已绑定邮箱：" + LoginActivity.user.getEmail());
        }
    }

    /**
     * 拉起QQ登陆，返回登录信息
     */
    private void bindingQQ(){
        if (!mTencent.isSessionValid()) {
            Log.i("lww", "onClickLogin session无效");

            loginListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject response = (JSONObject) o;
                    Log.i("lww",response.toString());
                    try {
                        int ret = response.getInt("ret");
                        Log.i("lww",""+ret);
                        if (ret == 0) {
                            openId = response.getString("openid");
                            accessToken = response.getString("access_token");
                            expires = response.getString("expires_in");
                            Log.i("lww", "Openid:" + openId);

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

                    /** 将openId发送至后台*/
                    new Thread() {
                        @Override
                        public void run() {
                            FormBody formBody = new FormBody.Builder().add("accout",LoginActivity.user.getAccout()).add("openId",openId).build();
                            final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/bindingQQ").build();
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
     * 弹出确定、取消对话框来确定是否解除绑定QQ
     */
    private void showAlertDialogQQ(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        //设置标题
        builder.setTitle("温馨提示");
        //设置提示内容
        builder.setMessage("确定要解除绑定吗？"+"\r\n"+"解除绑定后该QQ号将不能继续登录该账号下的游戏");
        //设置取消按钮
        builder.setNegativeButton("取消",null);
        //设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unBindingQQ();
            }
        });
        //创建AlertDialog对象
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 解绑QQ
     */
    private void unBindingQQ(){

        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("accout",LoginActivity.user.getAccout()).build();
                final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/unBindingQQ").build();
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
     * 绑定邮箱
     */
    private void bindingEmail(){
        BindingEmailDialog popupDialogShopCar = new BindingEmailDialog(getApplicationContext());
        popupDialogShopCar.showAtLocation(findViewById(R.id.btnBindingEmail), Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
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
     * 弹出确定、取消对话框来确定是否解除绑定邮箱
     */
    private void showAlertDialogEmail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        //设置标题
        builder.setTitle("温馨提示");
        //设置提示内容
        builder.setMessage("确定要解除绑定吗？"+"\r\n"+"邮箱可用于找回密码，解除绑定后将无法找回密码");
        //设置取消按钮
        builder.setNegativeButton("取消",null);
        //设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unBindingEmail();
            }
        });
        //创建AlertDialog对象
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 解绑邮箱
     */
    private void unBindingEmail(){
        new Thread() {
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("accout",LoginActivity.user.getAccout()).build();
                final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/unBindingEmail").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        sendMessage(4,result);
                    }
                });
            }
        }.start();
    }

    /**
     * 切换账号
     */
    private void regout() {
        mTencent.logout(getApplicationContext());

        /** 删除SharedPreferences内Token信息*/
        SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        /** 跳转到登录页面*/
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    //获取系统宽度
    private int getSyetemWidth(){
        return getResources().getDisplayMetrics().widthPixels;
    }
    //获取系统高度
    private int getSyetemHeight(){
        return getResources().getDisplayMetrics().heightPixels;
    }
    //获取Activity宽度
    private int getActivityWidth(){
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getWidth();
    }
    //获取Activity高度
    private int getActivityHeight(){
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getHeight();
    }
}
