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
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.li.knowledgefarm.MainActivity;
import com.li.knowledgefarm.R;

import java.io.IOException;

public class SettingActivity extends AppCompatActivity {
    /** 返回*/
    private ImageView iv_return;
    /** 修改昵称*/
    private Button btnUpdateNickName;
    /** 修改年级*/
    private Button btnUpdateGrade;
    /** 修改密码*/
    private Button btnUpdatePassword;
    /** 修改头像*/
    private Button btnUpdatePhoto;
    /** 绑定QQ*/
    private Button btnBindingQQ;
    /** 切换账号*/
    private Button btnRegout;
    /** OKHttpClient*/
    private OkHttpClient okHttpClient;
    /** 自定义点击事件监听器*/
    private CustomerListener listener;
    private String ip = "10.7.87.220";
    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 3: //修改密码判断
                    switch ((String)msg.obj){
                        case "0":
                            Toast.makeText(getApplicationContext(),"旧密码输入错误",Toast.LENGTH_SHORT).show();
                            break;
                        case "1":
                            Toast.makeText(getApplicationContext(),"密码修改成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "2":
                            Toast.makeText(getApplicationContext(),"密码修改失败",Toast.LENGTH_SHORT).show();
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

        /** 加载视图*/
        getViews();
        /** 注册点击事件监听器*/
        registListener();
        setStatusBar();
        okHttpClient = new OkHttpClient();
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
                case R.id.btnUpdatePhoto:
                    Toast.makeText(getApplicationContext(),"功能尚在开放",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnBindingQQ:
                    Toast.makeText(getApplicationContext(),"功能尚在开放",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnRegout:
                    Toast.makeText(getApplicationContext(),"功能尚在开放",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
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
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    /**
     * 修改用户密码
     */
    public void updatePassword(){
        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("accout","71007839").add("oldPassword","123").add("newPassword","1234").build();
                final Request request = new Request.Builder().post(formBody).url("http://"+ip+":8080/FarmKnowledge/user/updateUserPassword").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        sendMessage(3,result);
                    }
                });
            }
        }.start();
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
     * 加载视图
     */
    private void getViews(){
        iv_return = findViewById(R.id.iv_return);
        btnUpdateNickName = findViewById(R.id.btnUpdateNickName);
        btnUpdateGrade = findViewById(R.id.btnUpdateGrade);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnUpdatePhoto = findViewById(R.id.btnUpdatePhoto);
        btnBindingQQ = findViewById(R.id.btnBindingQQ);
        btnRegout = findViewById(R.id.btnRegout);
    }

    /**
     * 注册点击事件监听器
     */
    private void registListener(){
        listener = new CustomerListener();
        iv_return.setOnClickListener(listener);
        btnUpdateNickName.setOnClickListener(listener);
        btnUpdateGrade.setOnClickListener(listener);
        btnUpdatePassword.setOnClickListener(listener);
        btnUpdatePhoto.setOnClickListener(listener);
        btnBindingQQ.setOnClickListener(listener);
        btnRegout.setOnClickListener(listener);
    }
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }
}
