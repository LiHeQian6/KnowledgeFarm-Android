package com.li.knowledgefarm.Settings;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateNickNameDialog extends PopupWindow {
    private View view;
    private Context context;
    private ImageView iv_return;
    private EditText edtNickName;
    /** 字符长度*/
    private TextView tv_nickName_length;
    /** 保存*/
    private TextView tv_save;
    /** OKHttpClient*/
    private OkHttpClient okHttpClient;
    private String ip = "10.7.87.220";
    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1: // 修改昵称判断
                    if(msg.obj.equals("true")){
                        Toast.makeText(context,"昵称修改成功",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else{
                        Toast.makeText(context,"昵称修改失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public UpdateNickNameDialog(final Context context, String nickName) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.update_nickname, null);

        /** 设置设置popupWindow样式*/
        setpopupWndow();

        /** 初始化页面*/
        iv_return = view.findViewById(R.id.iv_return);
        edtNickName = view.findViewById(R.id.edtNickName);
        tv_nickName_length = view.findViewById(R.id.tv_nickName_length);
        tv_save = view.findViewById(R.id.tv_save);
        edtNickName.setText(nickName);
        tv_nickName_length.setText(nickName.length()+"/20");
        okHttpClient = new OkHttpClient();

        /** 监听输入框*/
        edtNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nickName = edtNickName.getText().toString().trim();
                tv_nickName_length.setText(nickName.length()+"/20");
                if(nickName.length() != 0){
                    tv_save.setTextColor(Color.parseColor("#FF0000"));
                }else{
                    tv_save.setTextColor(Color.parseColor("#AAAAAA"));
                }
                if(nickName.length() == 20){
                    Toast.makeText(context,"最多输入20个字符",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /** 点击保存*/
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
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
     * 保存
     */
    private void save(){
        final String nickName = edtNickName.getText().toString().trim();
        if(edtNickName.getText().toString().trim().equals("")){
            Toast.makeText(context,"您还没有填写昵称呢！",Toast.LENGTH_SHORT).show();
        }else{
            new Thread(){
                @Override
                public void run() {
                    FormBody formBody = new FormBody.Builder().add("accout","71007839").add("nickName",nickName).build();
                    final Request request = new Request.Builder().post(formBody).url("http://"+ip+":8080/FarmKnowledge/user/updateUserNickName").build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("lww","请求失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            sendMessage(1,result);
                        }
                    });
                }
            }.start();
        }
    }

    /**
     * 设置popupWindow样式
     */
    private void setpopupWndow(){
        this.setContentView(view);
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        //this.setAnimationStyle(R.style.pop_animation);
        //ColorDrawable dw = new ColorDrawable(0xb0000000);//背景半透明
        ColorDrawable d = new ColorDrawable(Color.parseColor("#f5f5f5"));
        this.setBackgroundDrawable(d);
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
