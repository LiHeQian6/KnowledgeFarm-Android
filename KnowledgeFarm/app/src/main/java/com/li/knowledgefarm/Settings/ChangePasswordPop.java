package com.li.knowledgefarm.Settings;

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
import android.widget.PopupWindow;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.Md5Encode;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChangePasswordPop extends PopupWindow {
    private Context context;
    private EditText old_password;
    /** 新密码输入框 */
    private EditText new_password;
    /** 确认新密码输入框 */
    private EditText again_new_password;
    private Button commit_password;
    private Button cancel_password;
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch ((String)msg.obj){
                case "true":
                    UserUtil.getUser().setPassword(new_password.getText().toString().trim());
                    dismiss();
                    CustomerToast.getInstance(context,"密码修改成功",Toast.LENGTH_SHORT).show();
                    break;
                case "PasswordError":
                    CustomerToast.getInstance(context, "旧密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case "false":
                    CustomerToast.getInstance(context,"密码修改失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public ChangePasswordPop(Context context) {
        super(context);
        this.context = context;
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.update_password,
                null, false);
        this.setContentView(contentView);
        okHttpClient = OkHttpUtils.getInstance(context);
        getViews(contentView);
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

    @Override
    public void dismiss() {
        handler.removeCallbacksAndMessages(null);
        super.dismiss();
    }

    /**
     * @Description 获取控件Id
     * @Author 孙建旺
     * @Date 上午10:50 2020/05/05
     * @Param [contentView]
     * @return void
     */
    private void getViews(View contentView) {
        old_password = contentView.findViewById(R.id.old_password);
        new_password = contentView.findViewById(R.id.new_password);
        again_new_password = contentView.findViewById(R.id.again_password);
        commit_password = contentView.findViewById(R.id.commit_password);
        cancel_password = contentView.findViewById(R.id.cancel_password);
        commit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePassword();
            }
        });
        cancel_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 保存
     */
    private void savePassword(){
        final String oldPassword = old_password.getText().toString().trim();
        final String newPassword = new_password.getText().toString().trim();
        final String newPasswordTest = again_new_password.getText().toString().trim();
        if(oldPassword.equals("") || newPassword.equals("") || newPasswordTest.equals("")){
            CustomerToast.getInstance(context,"您还有没填写的内容！",Toast.LENGTH_SHORT).show();
        }else {
            if (!newPassword.equals(newPasswordTest)) {
                CustomerToast.getInstance(context, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            }else if(newPassword.equals(newPasswordTest) && newPassword.length() < 8){
                CustomerToast.getInstance(context, "密码长度最低为8位", Toast.LENGTH_SHORT).show();
            }
            else {
                new Thread() {
                    @Override
                    public void run() {
                        FormBody formBody = new FormBody.Builder()
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
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                OkHttpUtils.unauthorized(response.code());
                                Message message = Message.obtain();
                                message.obj = response.body().string();
                                message.what = response.code();
                                handler.sendMessage(message);
                            }
                        });
                    }
                }.start();
            }
        }
    }
}
