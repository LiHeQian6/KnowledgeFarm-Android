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
    private EditText old_password;
    private EditText vertical;
    /** 新密码输入框 */
    private EditText new_password;
    /** 确认新密码输入框 */
    private EditText again_new_password;
    private Button get_vertical_btn;
    private Button commit_btn;
    private Button cancel_logout;
    private Button sure_logout;
    private LinearLayout get_vertical_li;
    private LinearLayout new_password_li;
    private LinearLayout log_btn_li;
    private LinearLayout input_li;
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
                case 2:
                    if(msg.obj.equals("true")){
                        LoginActivity.user.setNickName(new_message.getText().toString().trim());
                        Toast toast = Toast.makeText(context,"修改成功！",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM,0,0);
                        toast.show();
                        dismiss();
                    }else{
                        Toast.makeText(context,"网络出了点问题",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3://修改密码判断
                    switch ((String)msg.obj){
                        case "true":
                            LoginActivity.user.setPassword(new_password.getText().toString().trim());
                            dismiss();
                            Toast.makeText(context,"密码修改成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "PasswordError":
                            Toast.makeText(context, "旧密码错误", Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(context,"密码修改失败",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };

    public ChangeEmailPopUpWindow(Context context,String type) {
        super(context);
        this.context = context;
        this.type = type;
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        this.setAnimationStyle(R.style.pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.change_message_pop,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
        showTitle();
    }

    /**
     * @Description 更改标题
     * @Author 孙建旺
     * @Date 下午10:34 2020/04/29
     * @Param []
     * @return void
     */
    private void showTitle() {
        if(type.equals("Email")){
            show_title.setText("绑定邮箱");
            new_message.setHint("请输入邮箱");
        }else if (type.equals("NickName")){
            show_title.setText("修改昵称");
            new_message.setHint("请输入新的昵称");
            get_vertical_li.setVisibility(View.GONE);
        }else if(type.equals("Password")){
            show_title.setText("修改密码");
            new_message.setVisibility(View.GONE);
            get_vertical_li.setVisibility(View.GONE);
            new_password_li.setVisibility(View.VISIBLE);
        }else {
            show_title.setText("确定要退出吗");
            input_li.setVisibility(View.GONE);
            log_btn_li.setVisibility(View.VISIBLE);
        }
    }

    private void registerListener(){
        commit_btn.setOnClickListener(new CustomerOnclickListener());
        get_vertical_btn.setOnClickListener(new CustomerOnclickListener());
        cancel_logout.setOnClickListener(new CustomerOnclickListener());
        sure_logout.setOnClickListener(new CustomerOnclickListener());
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
        old_password = view.findViewById(R.id.old_password);
        new_password = view.findViewById(R.id.new_password);
        again_new_password = view.findViewById(R.id.again_password);
        new_password_li =  view.findViewById(R.id.new_password_li);
        commit_btn = view.findViewById(R.id.commit_btn);
        get_vertical_li = view.findViewById(R.id.getVertical_li);
        log_btn_li = view.findViewById(R.id.btn_li);
        input_li = view.findViewById(R.id.input_li);
        cancel_logout = view.findViewById(R.id.cancel_logout);
        sure_logout = view.findViewById(R.id.sure_logout);
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

    /**
     * 保存
     */
    private void save(){
        final String nickName = new_message.getText().toString().trim();
        if(new_message.getText().toString().trim().equals("")){
            Toast.makeText(context,"您还没有填写昵称呢！",Toast.LENGTH_SHORT).show();
        }else{
            new Thread(){
                @Override
                public void run() {
                    FormBody formBody = new FormBody.Builder().add("account", LoginActivity.user.getAccount()).add("nickName",nickName).build();
                    final Request request = new Request.Builder().post(formBody).url(context.getResources().getString(R.string.URL)+"/user/updateUserNickName").build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("lww","请求失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            sendMessage(2,response.code(),result);
                        }
                    });
                }
            }.start();
        }
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

    /**
     * 保存
     */
    private void savePassword(){
        final String oldPassword = old_password.getText().toString().trim();
        final String newPassword = new_password.getText().toString().trim();
        final String newPasswordTest = again_new_password.getText().toString().trim();
        if(oldPassword.equals("") || newPassword.equals("") || newPasswordTest.equals("")){
            Toast.makeText(context,"您还有没填写的内容！",Toast.LENGTH_SHORT).show();
        }else {
            if (!newPassword.equals(newPasswordTest)) {
                Toast.makeText(context, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            }else if(newPassword.equals(newPasswordTest) && newPassword.length() < 8){
                Toast.makeText(context, "密码长度最低为8位", Toast.LENGTH_SHORT).show();
            }
            else {
                new Thread() {
                    @Override
                    public void run() {
                        FormBody formBody = new FormBody.Builder()
                                .add("account", LoginActivity.user.getAccount())
                                .add("oldPassword", Md5Encode.getMD5(oldPassword.getBytes()))
                                .add("newPassword", Md5Encode.getMD5(newPassword.getBytes()))
                                .build();
                        final Request request = new Request.Builder().post(formBody).url(context.getResources().getString(R.string.URL) + "/user/updateUserPassword").build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.i("lww", "请求失败");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result = response.body().string();
                                sendMessage(3,response.code(), result);
                            }
                        });
                    }
                }.start();
            }
        }
    }

    /**
     * 切换账号
     */
    private void regout() {
        /** 其中mAppId是分配给第三方应用的appid，类型为String*/
        String mAppId = "1110065654";//101827370
        /** Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI*/
        Tencent mTencent = Tencent.createInstance(mAppId, context);;
        mTencent.logout(context);

        /** 删除SharedPreferences内Token信息*/
        SharedPreferences sp = context.getSharedPreferences("token",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        /** 跳转到登录页面*/
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
        dismiss();
    }

    private class CustomerOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.commit_btn:
                    if(type.equals("NickName"))
                        save();
                    else if(type.equals("Email")){
                        if(!vertical.getText().toString().trim().equals("")){ //验证码不为空
                            if(vertical.getText().toString().trim().equals(testCode)){ //验证码正确
                                over();
                            }else{ //验证码错误
                                Toast.makeText(context,"验证码输入错误",Toast.LENGTH_SHORT).show();
                            }
                        }else{ //验证码为空
                            Toast.makeText(context,"验证码不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        savePassword();
                    }
                    break;
                case R.id.getVertical_btn:
                    if(!new_message.getText().toString().trim().equals("")){ //邮箱不为空
                        if(isEmail(new_message.getText().toString().trim())){ //邮箱格式正确
                            getTestCode();
                        }else{ //邮箱格式错误
                            Toast.makeText(context,"邮箱格式错误",Toast.LENGTH_SHORT).show();
                        }
                    }else { //邮箱为空
                        Toast.makeText(context,"邮箱不能为空",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.cancel_logout:
                    dismiss();
                    break;
                case R.id.sure_logout:
                    regout();
                    break;
            }
        }
    }
}
