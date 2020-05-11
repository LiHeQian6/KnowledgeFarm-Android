package com.li.knowledgefarm.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.li.knowledgefarm.Util.Md5Encode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import com.li.knowledgefarm.Login.Interpolator.JellyInterpolator;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.EventBean;
import com.li.knowledgefarm.entity.User;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private RelativeLayout forget;
    private TextView registAccount;
    private LinearLayout layout_title;
    private int displayWidth;
    private int displayHeight;
    private ImageView titleImage;
    private CheckBox rememberPwd;
    private boolean tagPwd;
    private EditText edtCount;
    private EditText pwd;
    private ImageView returnImg;
    private EventBus eventBus;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 4:
                    Toast.makeText(getApplicationContext(),"账号或密码错误",Toast.LENGTH_SHORT).show();
                    recovery();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(),"账号不存在！",Toast.LENGTH_SHORT).show();
                    recovery();
                    break;
                case 7:
                    Toast.makeText(getApplicationContext(),"账号已失效",Toast.LENGTH_SHORT).show();
                    recovery();
                    break;
                case 5:
                    user = (User) msg.obj;
                    SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("account",user.getAccount());

                    if(rememberPwd.isChecked()){
                        editor.putString("password",pwd.getText().toString());
                    }
                    editor.apply();
                    Intent intentToStart = new Intent(LoginByAccountActivity.this,StartActivity.class);
                    intentToStart.setAction("accountLogin");
                    startActivity(intentToStart);
                    finish();
                    break;
                case 8:
                    recovery();
                    break;
            }
        }
    };
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_by_account);
        okHttpClient = OkHttpUtils.getInstance(this);
        FullScreen.NavigationBarStatusBar(LoginByAccountActivity.this,true);
        initView();
        setViewSize();
    }

    /**
     * @Description
     * @Auther 孙建旺
     * @Date 下午 2:19 2019/12/17
     * @Param []        
     * @return void 
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBus(EventBean eventBean){
        edtCount.setText(eventBean.getAccount());
        pwd.setText("");
        rememberPwd.setChecked(false);
    }

    //事件处理
    private void initView() {
        mBtnLogin = findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = findViewById(R.id.input_layout_name);
        mPsw = findViewById(R.id.input_layout_psw);
        forget = findViewById(R.id.forgetPwd);
        registAccount = findViewById(R.id.registAccount);
        layout_title = findViewById(R.id.layout_title);
        titleImage = findViewById(R.id.titleImage);
        rememberPwd = findViewById(R.id.rememberPwd);
        returnImg = findViewById(R.id.loginByAccountReturn);
        edtCount = findViewById(R.id.account);
        pwd = findViewById(R.id.pwd);
        edtCount.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        pwd.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        eventBus = EventBus.getDefault();
        if(!eventBus.isRegistered(this)){
            eventBus.register(LoginByAccountActivity.this);
        }

        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
        edtCount.setText(sp.getString("account",""));
        pwd.setText(sp.getString("password",""));
        if(!sp.getString("password","").equals("")){
            rememberPwd.setChecked(true);
        }
        rememberPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    tagPwd = true;
                }else {
                    tagPwd = false;
                }
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountStr = edtCount.getText().toString();
                pwdStr = pwd.getText().toString();
                if(accountStr.equals("")||pwdStr.equals("")){
                    Toast.makeText(getApplicationContext(),"账号或密码为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                // 计算出控件的高与宽
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                // 隐藏输入框
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                mBtnLogin.setVisibility(View.INVISIBLE);
                inputAnimator(mInputLayout, mWidth, mHeight);
                registAccount.setVisibility(View.INVISIBLE);
                rememberPwd.setVisibility(View.INVISIBLE);
                forget.setVisibility(View.INVISIBLE);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        /**
                         *要执行的操作
                         */
                        new Thread(){
                            @Override
                            public void run() {
                                if(!isConnByHttp()){
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(),"未连接服务器",Toast.LENGTH_SHORT).show();
                                    Message message = new Message();
                                    message.what = 8;
                                    handler.sendMessage(message);
                                    Looper.loop();
                                    return;
                                }
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
                Intent intent = new Intent();
                intent.setClass(LoginByAccountActivity.this,RegisteActivity.class);
                startActivity(intent);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFindPwd = new Intent(LoginByAccountActivity.this,FindPassword.class);
                startActivity(intentFindPwd);
            }
        });

        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //账号登录
    private void loginByAccount(String accountStr, String pwdStr) {
        //Request对象(Post、FormBody)
        FormBody formBody = new FormBody.Builder()
                .add("account", accountStr)
                .add("password", Md5Encode.getMD5(pwdStr.getBytes()))
                .build();
        Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/loginByAccount").build();
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
                if(result.equals("PasswordError")){            //密码错误
                    message.what = 4;
                    message.obj = result;
                    handler.sendMessage(message);
                }else if(result.equals("notExist")){            //账号不存在
                    message.what = 6;
                    message.obj = result;
                    handler.sendMessage(message);
                }else if(result.equals("notEffect")){             //账号失效
                    message.what = 7;
                    message.obj = result;
                    handler.sendMessage(message);
                } else {
                    message.what = 5;                             //登录成功
                    message.obj = parsr(URLDecoder.decode(result), User.class);
                    handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * @Description 设置控件屏幕适配
     * @Auther 孙建旺
     * @Date 下午 6:23 2019/12/14
     * @Param []
     * @return void
     */
    private void setViewSize() {
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayWidth = ds.widthPixels;
        displayHeight = ds.heightPixels;

        LinearLayout.LayoutParams param_image = new LinearLayout.LayoutParams((int)(displayWidth*0.11),(int)(displayHeight*0.14));
        param_image.gravity = Gravity.CENTER_HORIZONTAL;
        titleImage.setLayoutParams(param_image);

        RelativeLayout relative_input = findViewById(R.id.relative_input);
        RelativeLayout.LayoutParams params_input = new RelativeLayout.LayoutParams((int)(displayWidth*0.45),(int)(displayHeight*0.5));
        params_input.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params_input.topMargin = (int)(displayHeight*0.45);
        relative_input.setLayoutParams(params_input);

        LinearLayout layout_input = findViewById(R.id.layout_input);
        LinearLayout.LayoutParams param_layout_input = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(displayHeight*0.25));
        layout_input.setLayoutParams(param_layout_input);

        EditText account = findViewById(R.id.account);
        EditText pwd = findViewById(R.id.pwd);
        account.setTextSize(TypedValue.COMPLEX_UNIT_SP,(int)(displayWidth*0.012));
        pwd.setTextSize(TypedValue.COMPLEX_UNIT_SP,(int)(displayWidth*0.012));

        CheckBox ifRember = findViewById(R.id.rememberPwd);
        RelativeLayout.LayoutParams params_Rember = new RelativeLayout.LayoutParams((int)(displayWidth*0.2),(int)(displayHeight*0.12));
        params_Rember.setMargins(0,0,0,(int)(displayHeight*0.13));
        params_Rember.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ifRember.setLayoutParams(params_Rember);

        TextView login = findViewById(R.id.main_btn_login);
        RelativeLayout.LayoutParams params_login = new RelativeLayout.LayoutParams((int)(displayWidth*0.2),(int)(displayHeight*0.12));
        params_login.setMargins(0,0,0,(int)(displayHeight*0.02));
        params_login.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params_login.addRule(RelativeLayout.CENTER_HORIZONTAL);
        login.setLayoutParams(params_login);
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

    //恢复界面动画为原始状态
    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);
        mBtnLogin.setVisibility(View.VISIBLE);
        registAccount.setVisibility(View.VISIBLE);
        forget.setVisibility(View.VISIBLE);
        rememberPwd.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }

    //加载图片
    public boolean isConnByHttp(){
        boolean isConn = true;
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
