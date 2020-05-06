package com.li.knowledgefarm.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.Md5Encode;
import com.tencent.tauth.Tencent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChangeEmailPopUpWindow extends PopupWindow {
    private Context context;
    private String type;
    private TextView show_title;
    private EditText new_message;
    private EditText vertical;
    private Button get_vertical_btn;
    private Button commit_btn;
    private OkHttpClient okHttpClient;
    /** 异步线程*/
    private GetTestCodeAsyncTask asyncTask;
    /** 异步线程*/
    private TestCodeEffectAsyncTask testAsync;
    private String testCode;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 0: //邮箱是否已被其他账号绑定判断
                    if(msg.obj.equals("already")){ //邮箱已被其他账号绑定
                        endAsync1(); //停止异步任务
                        endAsync2(); //停止异步任务
                        Toast.makeText(context,"该邮箱已被其它账号绑定",Toast.LENGTH_SHORT).show();
                    }else if(msg.obj.equals("fail")){ //发送邮箱失败
                        endAsync1(); //停止异步任务
                        endAsync1(); //停止异步任务
                        Toast.makeText(context,"发送验证码失败",Toast.LENGTH_SHORT).show();
                    }else{ //获取到验证码
                        testCode = (String)msg.obj;
                    }
                    break;
                case 1: // 绑定邮箱判断
                    if(msg.obj.equals("true")){ //绑定成功
                        endAsync1(); //停止异步任务
                        endAsync2(); //停止异步任务
                        LoginActivity.user.setEmail(new_message.getText().toString().trim());
                        EventBus.getDefault().post("绑定邮箱成功");
                        Toast.makeText(context,"绑定邮箱成功",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else{ //绑定失败
                        Toast.makeText(context,"绑定邮箱失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public ChangeEmailPopUpWindow(Context context,String type) {
        super(context);
        this.context = context;
        this.type = type;
        this.setOutsideTouchable(false);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.change_message_pop,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
    }



    private void registerListener(){
        commit_btn.setOnClickListener(new CustomerOnclickListener());
        get_vertical_btn.setOnClickListener(new CustomerOnclickListener());
    }
    
    /**
     * @Description 获取控件ID
     * @Author 孙建旺
     * @Date 下午10:28 2020/04/29
     * @Param []
     * @return void
     */
    private void getViews(View view) {
        show_title = view.findViewById(R.id.show_title);
        new_message = view.findViewById(R.id.new_message);
        vertical = view.findViewById(R.id.vertical_edit);
        get_vertical_btn = view.findViewById(R.id.getVertical_btn);
        commit_btn = view.findViewById(R.id.commit_btn);
        new_message.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        vertical.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        okHttpClient = new OkHttpClient();
        registerListener();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setFocusable(false);
        super.showAtLocation(parent, gravity, x, y);
        final View view = getContentView();
        FullScreen.hideBottomUIMenu(view);
        setFocusable(true);
        update();
    }

    private void sendMessage(int arg1,int code,String message){
        Message messages = Message.obtain();
        messages.arg1 = arg1;
        messages.arg2 = code;
        messages.obj = message;
        handler.sendMessage(messages);
    }

    /**
     * 判断邮箱格式是否正确
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 结束异步任务1分钟倒计时
     */
    private void endAsync1(){
        if(asyncTask != null && !asyncTask.isCancelled() && asyncTask.getStatus() == AsyncTask.Status.RUNNING){
            asyncTask.cancel(true);
            asyncTask = null;
        }
    }

    /**
     * 结束异步任务2分钟倒计时
     */
    private void endAsync2(){
        if(testAsync != null && !testAsync.isCancelled() && testAsync.getStatus() == AsyncTask.Status.RUNNING){
            testAsync.cancel(true);
            testAsync = null;
        }
    }

    /**
     * 完成
     */
    private void over(){
        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("account", LoginActivity.user.getAccount()).add("email", new_message.getText().toString().trim()).build();
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
                        sendMessage(1,response.code(),result);
                    }
                });
            }
        }.start();
    }

    /**
     * 获取验证码
     */
    private void getTestCode(){
        get_vertical_btn.setClickable(false);
        endAsync2();

        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("email", new_message.getText().toString().trim()).build();
                final Request request = new Request.Builder().post(formBody).url(context.getResources().getString(R.string.URL)+"/user/sendTestCodeBingEmail").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        sendMessage(0,response.code(),result);
                    }
                });
            }
        }.start();

        /** 1分钟倒计时*/
        asyncTask = new GetTestCodeAsyncTask(context,get_vertical_btn);
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        /** 2分钟倒计时*/
        testAsync = new TestCodeEffectAsyncTask(get_vertical_btn);
        testAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class CustomerOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.commit_btn:
                    if(!vertical.getText().toString().trim().equals("")){ //验证码不为空
                        if(vertical.getText().toString().trim().equals(testCode)){ //验证码正确
                            over();
                        }else{ //验证码错误
                            Toast.makeText(context,"验证码输入错误",Toast.LENGTH_SHORT).show();
                        }
                    }else{ //验证码为空
                        Toast.makeText(context,"验证码不能为空",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.getVertical_btn:
                    if("859684581@qq.com".equals("859684581@qq.com")){ //邮箱不为空
                        if(isEmail("859684581@qq.com")){ //邮箱格式正确
                            getTestCode();
                        }else{ //邮箱格式错误
                            Toast.makeText(context,"邮箱格式错误",Toast.LENGTH_SHORT).show();
                        }
                    }else { //邮箱为空
                        Toast.makeText(context,"邮箱不能为空",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
