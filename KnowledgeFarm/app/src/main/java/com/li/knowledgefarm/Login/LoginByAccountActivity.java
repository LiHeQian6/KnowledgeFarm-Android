package com.li.knowledgefarm.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import com.li.knowledgefarm.Login.Interpolator.JellyInterpolator;
import com.li.knowledgefarm.Login.dialog.NotifyAccountDialog;
import com.li.knowledgefarm.Login.dialog.RegistAccountDialog;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.User;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Timer;
import java.util.TimerTask;

import static com.li.knowledgefarm.Login.LoginActivity.parsr;
import static com.li.knowledgefarm.Login.LoginActivity.user;

public class LoginByAccountActivity extends AppCompatActivity {

    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;
    private String accountStr;
    private String pwdStr;

    private TextView registAccount;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 5:
                    user = (User) msg.obj;
                    Intent intentToStart = new Intent(LoginByAccountActivity.this,StartActivity.class);
                    intentToStart.setAction("accountLogin");
                    startActivity(intentToStart);
                    finish();
                    break;
                default:
                    Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    recovery();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_by_account);

        //设置横屏
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setStatusBar();
        initView();
    }

    private void initView() {
        mBtnLogin = findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = findViewById(R.id.input_layout_name);
        mPsw = findViewById(R.id.input_layout_psw);
        registAccount = findViewById(R.id.registAccount);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtCount = findViewById(R.id.accout);
                EditText pwd = findViewById(R.id.pwd);
                accountStr = edtCount.getText().toString();
                pwdStr = pwd.getText().toString();
                // 计算出控件的高与宽
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                // 隐藏输入框
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                mBtnLogin.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),pwdStr,Toast.LENGTH_SHORT).show();
                inputAnimator(mInputLayout, mWidth, mHeight);
                if(accountStr.equals("")||pwdStr.equals("")){
                    Toast.makeText(getApplicationContext(),"账号或密码为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        /**
                         *要执行的操作
                         */
                        new Thread(){
                            @Override
                            public void run() {
                                loginByAccount(accountStr,pwdStr);
                            }
                        }.start();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 2000);
            }
        });

        registAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegistDialog();
            }
        });
    }

    private void loginByAccount(String accountStr, String pwdStr) {
        //Request对象(Post、FormBody)
        FormBody formBody = new FormBody.Builder()
                .add("account", accountStr)
                .add("password", pwdStr)
                .build();
        Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/loginByAccount").build();
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
                if(result.equals("PasswordError")){
                    message.what = 4;
                    message.obj = result;
                    handler.sendMessage(message);
                }else if(result.equals("notExist")){
                    message.what = 6;
                    message.obj = result;
                    handler.sendMessage(message);
                }else if(result.equals("notEffect")){
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

    private void showRegistDialog() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        RegistAccountDialog registAccountDialog = new RegistAccountDialog();
        if(!registAccountDialog.isAdded()){
            transaction.add(registAccountDialog,"notify");
        }
        transaction.show(registAccountDialog);
        transaction.commit();
    }

    /**
     * 输入框的动画效果
     *
     * @param view
     *            控件
     * @param w
     *            宽
     * @param h
     *            高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.2f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                recovery();
            }
        });

    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }
    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }


    /**
     * @Description 设置状态栏
     * @Auther 孙建旺
     * @Date 下午 4:20 2019/12/09
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
}
