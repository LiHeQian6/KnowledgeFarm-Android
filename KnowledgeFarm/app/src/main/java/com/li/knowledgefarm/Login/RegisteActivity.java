package com.li.knowledgefarm.Login;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.li.knowledgefarm.Util.Md5Encode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.Login.dialog.SpinnerAdapter;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.EventBean;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisteActivity extends AppCompatActivity {
    private String rName;
    private String grade;
    private String password;
    private ImageView closeImg;
    private LinearLayout linearAccount;
    private LinearLayout linearRegist;
    private TextView newAccount;
    private String[] array;
    private SpinnerAdapter arrayAdapter;
    private int displayWidth;
    private int displayHeight;
    private EventBus eventBus;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 4:
                    Toast.makeText(RegisteActivity.this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
                    EventBean eventBean = new EventBean();
                    eventBean.setAccount(msg.obj.toString());
                    eventBus.post(eventBean);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                    break;
                case 5:
                    Toast.makeText(RegisteActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        okHttpClient = OkHttpUtils.getInstance(this);
        getViews();
        setViewSize();
        FullScreen.NavigationBarStatusBar(RegisteActivity.this,true);
    }

    /**
     * @Description 获取控件Id
     * @Auther 孙建旺
     * @Date 上午 11:17 2019/12/17
     * @Param []
     * @return void
     */
    private void getViews() {
        eventBus = EventBus.getDefault();
        array = getResources().getStringArray(R.array.sarry);
        Spinner spinner = findViewById(R.id.spinner);
        arrayAdapter = new SpinnerAdapter(this,array);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new ProvOnItemSelectedListener());
        linearAccount = findViewById(R.id.linearCount);
        linearRegist = findViewById(R.id.linearRegist);
        closeImg = findViewById(R.id.RegisteReturn);
        newAccount = findViewById(R.id.newAccount);
        final EditText registName = findViewById(R.id.registName);
        final EditText pwd = findViewById(R.id.registPwd2);
        final EditText configPwd = findViewById(R.id.configPwd2);
        TextView button = findViewById(R.id.btnRegist2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rName = registName.getText().toString();
                password = pwd.getText().toString();
                String config = configPwd.getText().toString();
                if(rName.equals("")||password.equals("")||config.equals("")){
                    Toast.makeText(RegisteActivity.this,"请完善注册信息！",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!password.equals(config)){
                    Toast.makeText(RegisteActivity.this,"密码输入不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }else if(password.length()<8||password.length()>14){
                    Toast.makeText(getApplicationContext(),"请输入8-14位字符",Toast.LENGTH_SHORT).show();
                    return;
                }else if(password.contains(" ")){
                    Toast.makeText(getApplicationContext(),"含有非法字符",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    new Thread(){
                        @Override
                        public void run() {
                            registToServer();
                        }
                    }.start();
                }
            }
        });

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * @Description 设置控件适配屏幕
     * @Auther 孙建旺
     * @Date 下午 1:04 2019/12/15
     * @Param []
     * @return void
     */
    private void setViewSize() {
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayWidth = ds.widthPixels;
        displayHeight = ds.heightPixels;

        EditText nickname = findViewById(R.id.registName);
        EditText pwd = findViewById(R.id.registPwd2);
        EditText configPwd = findViewById(R.id.configPwd2);
        Spinner grade = findViewById(R.id.spinner);
        TextView registe = findViewById(R.id.btnRegist2);

        LinearLayout.LayoutParams params_nickname = new LinearLayout.LayoutParams((int)(displayWidth*0.4),(int)(displayHeight*0.1));
        params_nickname.gravity = Gravity.CENTER_HORIZONTAL;
        params_nickname.setMargins(0,(int)(displayHeight*0.03),0,0);
        nickname.setLayoutParams(params_nickname);
        pwd.setLayoutParams(params_nickname);
        configPwd.setLayoutParams(params_nickname);

        LinearLayout.LayoutParams params_spinner = new LinearLayout.LayoutParams((int)(displayWidth*0.3),(int)(displayHeight*0.1));
        params_spinner.gravity = Gravity.CENTER_HORIZONTAL;
        params_spinner.setMargins(0,(int)(displayHeight*0.05),0,0);
        grade.setLayoutParams(params_spinner);

        LinearLayout.LayoutParams params_registe = new LinearLayout.LayoutParams((int)(displayWidth*0.2),(int)(displayHeight*0.1));
        params_registe.setMargins(0,(int)(displayHeight*0.05),0,0);
        params_registe.gravity = Gravity.CENTER_HORIZONTAL;
        registe.setLayoutParams(params_registe);

    }

    //注册，访问服务器
    private void registToServer() {
        //Request对象(Post、FormBody)
        FormBody formBody = new FormBody.Builder()
                .add("nickName",rName)
                .add("grade",grade)
                .add("password", Md5Encode.getMD5(password.getBytes()))
                .build();
        Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/registAccount").build();
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
                Message message = new Message();
                if(result.equals("fail")){
                    message.what = 5;
                    handler.sendMessage(message);
                }else {
                    message.what = 4;
                    try {
                        message.obj = new JSONObject(result).getString("account");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handler.sendMessage(message);
                }
            }
        });
    }

    //年级选择
    private class ProvOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    grade = "1";
                    break;
                case 1:
                    grade = "2";
                    break;
                case 2:
                    grade = "3";
                    break;
                case 3:
                    grade = "4";
                    break;
                case 4:
                    grade = "5";
                    break;
                case 5:
                    grade = "6";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            grade = "1";
        }
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
