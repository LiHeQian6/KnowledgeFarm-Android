package com.li.knowledgefarm.Settings;

import androidx.appcompat.app.AppCompatActivity;
import com.li.knowledgefarm.Util.Md5Encode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;

import java.io.IOException;

public class UpdatePasswordActivity extends AppCompatActivity {
    /** 返回*/
    private ImageView iv_return;
    /** 旧密码、新密码、确认密码输入框*/
    private EditText edtOldPassword,edtNemPassword,edtNewPasswordTest;
    /** 保存*/
    private TextView tv_save;
    /** OKHttpClient*/
    private OkHttpClient okHttpClient;
    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1://修改密码判断
                    switch ((String)msg.obj){
                        case "true":
                            LoginActivity.user.setPassword(edtNemPassword.getText().toString().trim());
                            finish();
                            Toast.makeText(getApplicationContext(),"密码修改成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "PasswordError":
                            Toast.makeText(getApplicationContext(), "旧密码错误", Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(getApplicationContext(),"密码修改失败",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        /** 初始化*/
        init();

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
                finish();
            }
        });

    }

    /**
     * 初始化
     */
    private void init(){
        iv_return = findViewById(R.id.iv_return);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNemPassword = findViewById(R.id.edtNewPassword);
        edtNewPasswordTest = findViewById(R.id.edtNewPasswordTest);
        tv_save = findViewById(R.id.tv_save);
        okHttpClient = new OkHttpClient();

        FullScreen.NavigationBarStatusBar(UpdatePasswordActivity.this,true);
    }

    /**
     * 保存
     */
    private void save(){
        final String oldPassword = edtOldPassword.getText().toString().trim();
        final String newPassword = edtNemPassword.getText().toString().trim();
        final String newPasswordTest = edtNewPasswordTest.getText().toString().trim();
        if(oldPassword.equals("") || newPassword.equals("") || newPasswordTest.equals("")){
            Toast.makeText(getApplicationContext(),"您还有没填写的内容！",Toast.LENGTH_SHORT).show();
        }else {
            if (!newPassword.equals(newPasswordTest)) {
                Toast.makeText(getApplicationContext(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        FormBody formBody = new FormBody.Builder()
                                .add("account", LoginActivity.user.getAccount())
                                .add("oldPassword", Md5Encode.getMD5(oldPassword.getBytes()))
                                .add("newPassword", Md5Encode.getMD5(newPassword.getBytes()))
                                .build();
                        final Request request = new Request.Builder().post(formBody).url(getApplicationContext().getResources().getString(R.string.URL) + "/user/updateUserPassword").build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.i("lww", "请求失败");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result = response.body().string();
                                sendMessage(1, result);
                            }
                        });
                    }
                }.start();
            }
        }
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

//    //MD5加密
//    public String stringMD5(String input) {
//        try {
//            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
//            MessageDigest messageDigest =MessageDigest.getInstance("MD5");
//            // 输入的字符串转换成字节数组
//            byte[] inputByteArray = input.getBytes();
//            // inputByteArray是输入字符串转换得到的字节数组
//            messageDigest.update(inputByteArray);
//            // 转换并返回结果，也是字节数组，包含16个元素
//            byte[] resultByteArray = messageDigest.digest();
//            // 字符数组转换成字符串返回
//            return byteArrayToHex(resultByteArray);
//        } catch (NoSuchAlgorithmException e) {
//            return null;
//        }
//    }
//
//    //将字节数组换成成16进制的字符串
//    public String byteArrayToHex(byte[] byteArray) {
//        // 首先初始化一个字符数组，用来存放每个16进制字符
//        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
//        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
//        char[] resultCharArray =new char[byteArray.length * 2];
//        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
//        int index = 0;
//        for (byte b : byteArray) {
//            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
//            resultCharArray[index++] = hexDigits[b& 0xf];
//        }
//        // 字符数组组合成字符串返回
//        return new String(resultCharArray);
//    }

}
