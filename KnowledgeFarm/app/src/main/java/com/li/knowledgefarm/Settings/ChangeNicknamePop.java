package com.li.knowledgefarm.Settings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChangeNicknamePop extends PopupWindow {

    private Context context;
    private EditText new_nickname;//昵称输入框
    private Button commit_nickname;//提交按钮
    private Button cancel_nickname;
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.obj.equals("true") && msg.what == 200){
                LoginActivity.user.setNickName(new_nickname.getText().toString().trim());
                Toast toast = Toast.makeText(context,"修改成功！",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,0,0);
                toast.show();
                dismiss();
            }else{
                Toast.makeText(context,"网络出了点问题",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public ChangeNicknamePop(Context context) {
        super(context);
        this.context = context;
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.update_nickname,
                null, false);
        this.setContentView(contentView);
        okHttpClient = OkHttpUtils.getInstance(context);
        getViews(contentView);
    }

    @Override
    public void dismiss() {
        handler.removeCallbacksAndMessages(null);
        super.dismiss();
    }

    /**
     * 保存
     */
    private void save(){
        final String nickName = new_nickname.getText().toString().trim();
        if(new_nickname.getText().toString().trim().equals("")){
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
     * @Description 获取控件ID
     * @Author 孙建旺
     * @Date 上午10:09 2020/05/05
     * @Param [contentView]
     * @return void
     */
    private void getViews(View contentView) {
        new_nickname = contentView.findViewById(R.id.new_nickname);
        new_nickname.setText(LoginActivity.user.getNickName());
        commit_nickname = contentView.findViewById(R.id.commit_nickname);
        cancel_nickname = contentView.findViewById(R.id.cancel_nickname);
        commit_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        cancel_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
