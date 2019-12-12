package com.li.knowledgefarm.Settings;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BindingEmailDialog extends PopupWindow {
    private View view;
    private Context context;
    private ImageView iv_return;
    /** 邮箱输入框上面的文字*/
    private TextView tv_email;
    /** 邮箱输入框*/
    private EditText edtEmail;
    /** 验证码输入框*/
    private EditText edtTestCode;
    /** 获取验证码*/
    private TextView tv_getTestCode;
    /** 验证码*/
    private TextView tv_testCode;
    /** 完成*/
    private Button btnTest;
    /** OKHttpClient*/
    private OkHttpClient okHttpClient;
    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0: // 绑定邮箱判断
                    if(msg.obj.equals("true")){
                        LoginActivity.user.setEmail(edtEmail.getText().toString().trim());
                        dismiss();
                        Toast.makeText(context,"绑定邮箱成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"绑定邮箱失败",Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };

    public BindingEmailDialog(final Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.binding_email, null);

        /** 设置设置popupWindow样式*/
        setpopupWndow();

        /** 初始化*/
        init();

        /** 点击获取验证码*/
        tv_getTestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmail(edtEmail.getText().toString())){

                }
                getTestCode();
            }
        });

        /** 点击完成*/
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("lww","over");
                Log.i("lww","testCode:"+tv_testCode.getText());
                if(edtTestCode.getText().toString().trim().equals(tv_testCode.getText())){
                    over();
                }else{
                    Toast.makeText(context,"验证码输入错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /** 点击返回*/
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * 设置popupWindow样式
     */
    private void setpopupWndow(){
        this.setContentView(view);
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.pop_animation);
        //ColorDrawable d = new ColorDrawable(0xb0000000);//背景半透明
        ColorDrawable d = new ColorDrawable(Color.parseColor("#f5f5f5"));
        this.setBackgroundDrawable(d);
    }

    /**
     * 初始化
     */
    private void init(){
        iv_return = view.findViewById(R.id.iv_return);
        tv_email = view.findViewById(R.id.tv_email);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtTestCode = view.findViewById(R.id.edtTestCode);
        tv_getTestCode = view.findViewById(R.id.tv_getTestCode);
        tv_testCode = view.findViewById(R.id.tv_testCode);
        btnTest = view.findViewById(R.id.btnTest);

        okHttpClient = new OkHttpClient();
    }

    /**
     * 获取验证码
     */
    private void getTestCode(){
        GetTestCodeAsyncTask asyncTask = new GetTestCodeAsyncTask(context,tv_email,tv_getTestCode,tv_testCode,edtEmail.getText().toString().trim());
        asyncTask.execute();
    }

    /**
     * 完成
     */
    private void over(){
        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("accout", LoginActivity.user.getAccout()).add("email", edtEmail.getText().toString().trim()).build();
                final Request request = new Request.Builder().post(formBody).url(context.getResources().getString(R.string.URL)+"/user/bindingEmail").build();
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
     * 判断邮箱格式是否正确
     * @param email
     * @return
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
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

}
