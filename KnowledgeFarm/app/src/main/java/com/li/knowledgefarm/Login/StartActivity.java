package com.li.knowledgefarm.Login;

import androidx.annotation.NonNull;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.li.knowledgefarm.Login.dialog.NotifyAccountDialog;
import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.li.knowledgefarm.Login.LoginActivity.mTencent;
import static com.li.knowledgefarm.Login.LoginActivity.user;

public class StartActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView nickName;
    private Button btnStart;
    private StartListener listener;
    private Button btnRegout;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 6:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    photo.setImageBitmap(bitmap);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //设置横屏
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setStatusBar();
        getViews();
        registListener();
        Intent getInfo = getIntent();
        if(getInfo.getAction().equals("QQFirstLogin")&&user!=null){
            nickName.setText(user.getNickName());
//            urlToImgBitmap(user.getPhoto());
            Glide.with(this).load(user.getPhoto()).centerCrop().fitCenter().circleCrop().into(photo);
            showNotifyDialog();
        }else if(getInfo.getAction().equals("autoLogin")&&user!=null){
            nickName.setText(user.getNickName());
//            urlToImgBitmap(user.getPhoto());
            Glide.with(this).load(user.getPhoto()).centerCrop().fitCenter().circleCrop().into(photo);
        }else if(getInfo.getAction().equals("accountLogin")){
            nickName.setText(user.getNickName());
//            urlToImgBitmap(user.getPhoto());
            Glide.with(this).load(user.getPhoto()).centerCrop().fitCenter().circleCrop().into(photo);
        }
    }

    private void getViews() {
        photo = findViewById(R.id.ivUserImage);
        nickName = findViewById(R.id.tvUserName);
        btnStart = findViewById(R.id.btnStart);
        btnRegout = findViewById(R.id.btnRegout);
    }

    private void registListener() {
        listener = new StartListener();
        btnRegout.setOnClickListener(listener);
        btnStart.setOnClickListener(listener);
    }
    
    class StartListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                /** 点击注销*/
                case R.id.btnRegout:
                    regout();
                    break;
                case R.id.btnStart:
                    Intent begin = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(begin);
                    finish();
                    break;
            }
        }
    }


    /**
     * 注销
     */
    private void regout() {
        mTencent.logout(getApplicationContext());
        //注销登陆
        SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        user = null;
        Intent backToLogin = new Intent(StartActivity.this,LoginActivity.class);
        backToLogin.setAction("logout");
        startActivity(backToLogin);
        finish();
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
                    message.what = 6;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showNotifyDialog() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NotifyAccountDialog notifyAccountDialog = new NotifyAccountDialog();
        if(!notifyAccountDialog.isAdded()){
            transaction.add(notifyAccountDialog,"notify");
        }
        transaction.show(notifyAccountDialog);
        transaction.commitAllowingStateLoss();
    }

    /**
     * @Description 设置状态栏
     * @Auther 孙建旺
     * @Date 下午 4:19 2019/12/09
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

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(StartActivity.this, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


}
