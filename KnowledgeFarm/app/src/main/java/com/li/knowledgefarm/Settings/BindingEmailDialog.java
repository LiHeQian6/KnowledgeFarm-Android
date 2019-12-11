package com.li.knowledgefarm.Settings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.li.knowledgefarm.R;

import okhttp3.OkHttpClient;

public class BindingEmailDialog extends PopupWindow {
    private View view;
    private Context context;
    /** 验证码输入框上面的文字*/
    private TextView tv_email;
    /** 验证码输入框*/
    private EditText edtTestCode;
    /** 获取验证码*/
    private TextView tv_getTestCode;
    /** 完成*/
    private Button btnTest;
    /** OKHttpClient*/
    private OkHttpClient okHttpClient;
    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1: // 绑定邮箱判断

            }
        }
    };

    public BindingEmailDialog(final Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.update_nickname, null);

        /** 设置设置popupWindow样式*/
        setpopupWndow();

        /** 初始化*/
        init();

        /** 点击获取验证码*/

    }

    /**
     * 设置popupWindow样式
     */
    private void setpopupWndow(){
        this.setContentView(view);
        //this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        //this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
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
        tv_email = view.findViewById(R.id.tv_email);
        edtTestCode = view.findViewById(R.id.edtTestCode);
        tv_getTestCode = view.findViewById(R.id.tv_getTestCode);

        okHttpClient = new OkHttpClient();
    }

    /**
     * 获取验证码
     */
    private void getTestCode(){

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
