package com.li.knowledgefarm.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.jpush.android.api.JPushInterface;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.Login.dialog.NotifyAccountDialog;
import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.User;

import static com.li.knowledgefarm.Login.LoginActivity.mTencent;

public class StartActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView nickName;
    private Button btnStart;
    private StartListener listener;
    private Button btnRegout;
    private User user = UserUtil.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //设置横屏
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        FullScreen.NavigationBarStatusBar(StartActivity.this,true);
        getViews();
        registListener();
        Intent getInfo = getIntent();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.huancun2)
                .error(R.drawable.meigui)
                .fallback(R.drawable.meigui)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if(getInfo.getAction().equals("QQFirstLogin")&&user!=null){
            nickName.setText(user.getNickName());
            Glide.with(this).load(user.getPhoto()).apply(requestOptions).into(photo);
            showNotifyDialog();
        }else if(getInfo.getAction().equals("autoLogin")&&user!=null){
            nickName.setText(user.getNickName());
            Glide.with(this).load(user.getPhoto()).apply(requestOptions).into(photo);
        }else if(getInfo.getAction().equals("accountLogin")){
            nickName.setText(user.getNickName());
            Glide.with(this).load(user.getPhoto()).apply(requestOptions).into(photo);
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
                    JPushInterface.setAlias(getApplicationContext(),1, UserUtil.getUser().getAccount());
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
        SharedPreferences qq = getSharedPreferences("token",MODE_PRIVATE);
        SharedPreferences.Editor editorQQ = qq.edit();
        editorQQ.clear();
        editorQQ.commit();
        SharedPreferences account = getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editorAccount = account.edit();
        editorAccount.putString("password","");
        editorAccount.commit();
        user = null;
        Intent backToLogin = new Intent(StartActivity.this,LoginActivity.class);
        backToLogin.setAction("logout");
        startActivity(backToLogin);
        finish();
    }

    //第一次登录后，显示用户账号信息提示弹出框
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
