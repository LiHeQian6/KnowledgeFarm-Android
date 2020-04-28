package com.li.knowledgefarm.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {
    /** 返回*/
    private ImageView iv_return;
    /** 头像*/
    private ImageView iv_photo;
    /** 修改昵称*/
    private Button btnUpdateNickName;
    /** 修改年级*/
    private Button btnUpdateGrade;
    /** 修改密码*/
    private Button btnUpdatePassword;
    /** 修改头像*/
    private Button btnUpdatePhoto;
    /** 绑定QQ*/
    private Button btnBindingQQ;
    /** 解绑QQ*/
    private Button btnUnBindingQQ;
    /** 绑定邮箱*/
    private Button btnBindingEmail;
    /** 解绑邮箱*/
    private Button btnUnBindingEmail;
    /** QQ信息*/
    private TextView tv_QQ;
    /** 邮箱信息*/
    private TextView tv_email;
    /** 切换账号*/
    private Button btnRegout;
    /** OKHttpClient*/
    private OkHttpClient okHttpClient;
    /** 自定义点击事件监听器*/
    private CustomerListener listener;
    /** EvenBus*/
    EventBus eventBus;
    /** Gson*/
    Gson gson = new Gson();

    /** Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI*/
    private Tencent mTencent;
    /** 其中mAppId是分配给第三方应用的appid，类型为String*/
    public String mAppId = "1110065654";//101827370
    /** 用户信息*/
    private UserInfo mInfo;
    /** 授权登录监听器*/
    private IUiListener loginListener;
    /** 获取用户信息监听器*/
    private IUiListener userInfoListener;
    private String openId;
    private String accessToken;
    private String expires;

    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0: //账号是否已被绑定QQ判断
                    switch ((String)msg.obj){
                        case "true":
                            btnBindingQQ.setVisibility(View.GONE);
                            btnUnBindingQQ.setVisibility(View.VISIBLE);
                            tv_QQ.setText("您的账号"+LoginActivity.user.getAccount()+"已绑定QQ");
                            break;
                        case "false":
                            tv_QQ.setText("您的账号"+LoginActivity.user.getAccount()+"还未绑定QQ");
                            break;
                    }
                    break;
                case 1: //绑定QQ判断
                    switch ((String)msg.obj){
                        case "true":
                            btnBindingQQ.setVisibility(View.GONE);
                            btnUnBindingQQ.setVisibility(View.VISIBLE);
                            tv_QQ.setText("您的账号"+LoginActivity.user.getAccount()+"已绑定QQ");
                            /** 存入SharedPreferences*/
                            SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("opId",openId);
                            editor.putString("tk",accessToken);
                            editor.putLong("start",new Date().getTime());
                            editor.putInt("expires", Integer.parseInt(expires));
                            editor.commit();
                            Toast.makeText(getApplicationContext(),"绑定QQ成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(getApplicationContext(),"绑定QQ失败",Toast.LENGTH_SHORT).show();
                            break;
                        case "already":
                            Toast.makeText(getApplicationContext(),"该QQ号已被其他账号绑定",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 2: //解绑QQ判断
                    switch ((String)msg.obj){
                        case "true":
                            btnBindingQQ.setVisibility(View.VISIBLE);
                            btnUnBindingQQ.setVisibility(View.GONE);
                            tv_QQ.setText("您的账号"+LoginActivity.user.getAccount()+"还未绑定QQ");
                            mTencent.logout(getApplicationContext());
                            /** 删除SharedPreferences内Token信息*/
                            SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.commit();
                            Toast.makeText(getApplicationContext(),"解绑QQ成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(getApplicationContext(),"解绑QQ失败",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 3: //解绑邮箱判断
                    switch ((String)msg.obj){
                        case "true":
                            LoginActivity.user.setEmail("");
                            btnBindingEmail.setVisibility(View.VISIBLE);
                            btnUnBindingEmail.setVisibility(View.GONE);
                            tv_email.setText("您的账号"+LoginActivity.user.getAccount()+"还未绑定邮箱");
                            Toast.makeText(getApplicationContext(),"解绑邮箱成功",Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(getApplicationContext(),"解绑邮箱失败", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 4: //修改头像判断
                    String aString = (String)msg.obj;
                    if(aString.equals("false") || aString.equals("")){
                        Toast.makeText(getApplicationContext(),"头像上传失败", Toast.LENGTH_SHORT).show();
                    }else if(aString.equals("null")){
                        Toast.makeText(getApplicationContext(), "图片为空", Toast.LENGTH_SHORT).show();
                    }else{
                        LoginActivity.user.setPhoto(aString);
                        RequestOptions options = new RequestOptions();
                        options.placeholder(R.drawable.huancun2) //加载图片时
                                .error(R.drawable.huancun2) //请求出错（图片资源不存在，无访问权限）
                                .fallback(R.drawable.huancun2) //请求资源为null
                                .circleCrop() //转换图片效果
                                .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存策略
                        Glide.with(getApplicationContext()).load(aString).apply(options).into(iv_photo);
                    }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /** 初始化*/
        getViews();

        /** 请求服务器，判断该账号是否已绑定QQ，返回并刷新界面*/
        isBindingQQ();

        /** 判断是否已绑定邮箱，再刷新界面*/
        isBindingEmail();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Subscribe//可以订阅粘性事件
    public void onEventBeanStikyEvent(String result){
        if(result.equals("绑定邮箱成功")){
            Log.i("lww","绑定邮箱成功");
            isBindingEmail();
        }
    }

    class CustomerListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_return:
                    finish();
                    break;
                case R.id.btnUpdateNickName:
                    dialog_update_nickName();
                    break;
                case R.id.btnUpdateGrade:
                    dialog_update_grade();
                    break;
                case R.id.btnUpdatePassword:
                    update_password();
                    break;
                case R.id.btnUpdatePhoto:
                    openPhonePhoto();
                    break;
                case R.id.btnBindingQQ:
                    bindingQQ();
                    break;
                case R.id.btnUnBindingQQ:
                    showAlertDialogQQ();
                    break;
                case R.id.btnBindingEmail:
                    Intent intent = new Intent();
                    intent.setClass(SettingActivity.this, BindingEmailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnUnBindingEmail:
                    showAlertDialogEmail();
                    break;
                case R.id.btnRegout:
                    showAlertDialogLogout();
                    break;
            }
        }
    }

    /**
     * 初始化
     */
    private void getViews(){
        iv_return = findViewById(R.id.iv_return);
        iv_photo = findViewById(R.id.iv_photo);
        btnUpdateNickName = findViewById(R.id.btnUpdateNickName);
        btnUpdateGrade = findViewById(R.id.btnUpdateGrade);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnUpdatePhoto = findViewById(R.id.btnUpdatePhoto);
        btnBindingQQ = findViewById(R.id.btnBindingQQ);
        btnUnBindingQQ = findViewById(R.id.btnUnBindingQQ);
        btnBindingEmail = findViewById(R.id.btnBindingEmail);
        btnUnBindingEmail = findViewById(R.id.btnUnBindingEmail);
        tv_QQ = findViewById(R.id.tv_QQ);
        tv_email = findViewById(R.id.tv_email);
        btnRegout = findViewById(R.id.btnRegout);

        listener = new CustomerListener();
        iv_return.setOnClickListener(listener);
        btnUpdateNickName.setOnClickListener(listener);
        btnUpdateGrade.setOnClickListener(listener);
        btnUpdatePassword.setOnClickListener(listener);
        btnUpdatePhoto.setOnClickListener(listener);
        btnBindingQQ.setOnClickListener(listener);
        btnUnBindingQQ.setOnClickListener(listener);
        btnBindingEmail.setOnClickListener(listener);
        btnUnBindingEmail.setOnClickListener(listener);
        btnRegout.setOnClickListener(listener);

        okHttpClient = new OkHttpClient();
        eventBus = EventBus.getDefault();
        eventBus.register(SettingActivity.this);
        mTencent = Tencent.createInstance(mAppId, getApplicationContext());

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.huancun2) //加载图片时
                .error(R.drawable.huancun2) //请求出错（图片资源不存在，无访问权限）
                .fallback(R.drawable.huancun2) //请求资源为null
                .circleCrop() //转换图片效果
                .diskCacheStrategy(DiskCacheStrategy.NONE);//缓存策略
        Glide.with(getApplicationContext()).load(LoginActivity.user.getPhoto()).apply(options).into(iv_photo);

        /** 关闭状态栏*/
        FullScreen.NavigationBarStatusBar(SettingActivity.this,true);
    }

    /**
     * 弹出修改昵称窗口
     */
    private void dialog_update_nickName(){
        //管理多个Fragment对象
        FragmentManager fragmentManager = getSupportFragmentManager();
        //事务(原子性的操作)
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //创建CustomDialog,添加数据
        UpdateNickNameDialog customDialog = new UpdateNickNameDialog();
        //判断是否已经被添加过
        if(!customDialog.isAdded()){
            //添加Fragment
            transaction.add(customDialog,"dialog");
        }
        //显示Fragment
        transaction.show(customDialog);
        transaction.commit();
    }

    /**
     * 弹出修改年级窗口
     */
    private void dialog_update_grade(){
        //管理多个Fragment对象
        FragmentManager fragmentManager = getSupportFragmentManager();
        //事务(原子性的操作)
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //创建CustomDialog,添加数据
        UpdateGradeDialog customDialog = new UpdateGradeDialog();
        //判断是否已经被添加过
        if(!customDialog.isAdded()){
            //添加Fragment
            transaction.add(customDialog,"dialog");
        }
        //显示Fragment
        transaction.show(customDialog);
        transaction.commit();
    }

    /**
     * 弹出修改密码界面
     */
    private void update_password(){
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this,UpdatePasswordActivity.class);
        startActivity(intent);
    }

    /**
     * 打开相册
     */
    private void openPhonePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }

    /**
     * 修改头像
     */
    private void updatePhoto(final File photo){
        new Thread(){
            @Override
            public void run() {
                Log.i("lww",photo.getAbsolutePath());
                MediaType mimeType = MediaType.parse("image/*");
                if(!photo.exists()){
                    Log.i("lww","文件不存在");
                }
                RequestBody requestBody = RequestBody.create(photo,mimeType);
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("id", "" + LoginActivity.user.getId())
                        .addFormDataPart("photo", URLEncoder.encode(LoginActivity.user.getPhoto()))
                        .addFormDataPart("upload",photo.getName(),requestBody)
                        .build();
                Request request = new Request.Builder().post(body).url(getResources().getString(R.string.URL)+"/user/updatePhoto").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        sendMessage(4,result);
                    }
                });
            }
        }.start();
    }

    /**
     * 判断该账号是否已绑定QQ
     */
    private void isBindingQQ(){
        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("account",LoginActivity.user.getAccount()).build();
                final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/isBindingQQ").build();
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
     * 判断是否已绑定邮箱，再修改页面
     */
    private void isBindingEmail(){
        if(null!=LoginActivity.user&&!LoginActivity.user.getEmail().equals("")){
            btnBindingEmail.setVisibility(View.GONE);
            btnUnBindingEmail.setVisibility(View.VISIBLE);
            tv_email.setText("您的账号"+LoginActivity.user.getAccount()+"已绑定邮箱：" + LoginActivity.user.getEmail());
        }else{
            tv_email.setText("您的账号"+LoginActivity.user.getAccount()+"还未绑定邮箱");
        }
    }

    /**
     * 拉起QQ登陆，返回登录信息
     */
    private void bindingQQ(){
        if (!mTencent.isSessionValid()) {
            Log.i("lww", "onClickLogin session无效");

            loginListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject response = (JSONObject) o;
                    Log.i("lww",response.toString());
                    try {
                        int ret = response.getInt("ret");
                        Log.i("lww",""+ret);
                        if (ret == 0) {
                            openId = response.getString("openid");
                            accessToken = response.getString("access_token");
                            expires = response.getString("expires_in");
                            Log.i("lww", "Openid:" + openId);

                            mTencent.setOpenId(openId);
                            mTencent.setAccessToken(accessToken, expires);

                            /** 获取QQ用户信息,并发送至后台*/
                            getAndTransferInfo();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(UiError uiError) {
                }

                @Override
                public void onCancel() {
                }
            };
            mTencent.login(this, "all", loginListener, true);//（true）设置允许扫码

        } else {
            Log.i("lww", "onClickLogin session有效");
        }
    }

    /**
     * 获取QQ用户信息,并发送至后台
     */
    private void getAndTransferInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            Log.i("lww", "updateUserInfo1");
            userInfoListener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }
                @Override
                public void onComplete(Object response) {
                    JSONObject json = (JSONObject) response;

                    /** 昵称*/
                    String nickname = null;
                    try {
                        nickname = ((JSONObject) response).getString("nickname");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /** 头像*/
                    String path = null;
                    try {
                        path = json.getString("figureurl_qq_2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /** 将openId发送至后台*/
                    new Thread() {
                        @Override
                        public void run() {
                            FormBody formBody = new FormBody.Builder().add("account",LoginActivity.user.getAccount()).add("openId",openId).build();
                            final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/bindingQQ").build();
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

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(userInfoListener);
        } else {
            Log.i("lww", "updateUserInfo2");
        }
    }

    /**
     * @Description 提示是否切换账号
     * @Auther 孙建旺
     * @Date 下午 5:19 2019/12/13
     * @Param []
     * @return void
     */
    private void showAlertDialogLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        //设置标题
        builder.setTitle("温馨提示");
        //设置提示内容
        builder.setMessage("确定要切换账号吗？");
        //设置取消按钮
        builder.setNegativeButton("取消",null);
        //设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                regout();
            }
        });
        //创建AlertDialog对象
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 弹出确定、取消对话框来确定是否解除绑定QQ
     */
    private void showAlertDialogQQ(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        //设置标题
        builder.setTitle("温馨提示");
        //设置提示内容
        builder.setMessage("确定要解除绑定吗？"+"\r\n"+"解除绑定后该QQ号将不能继续登录该账号下的游戏");
        //设置取消按钮
        builder.setNegativeButton("取消",null);
        //设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unBindingQQ();
            }
        });
        //创建AlertDialog对象
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 解绑QQ
     */
    private void unBindingQQ(){

        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("account",LoginActivity.user.getAccount()).build();
                final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/unBindingQQ").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        sendMessage(2,result);
                    }
                });
            }
        }.start();
    }

    /**
     * 弹出确定、取消对话框来确定是否解除绑定邮箱
     */
    private void showAlertDialogEmail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        //设置标题
        builder.setTitle("温馨提示");
        //设置提示内容
        builder.setMessage("确定要解除绑定吗？"+"\r\n"+"邮箱可用于找回密码，解除绑定后将无法找回密码");
        //设置取消按钮
        builder.setNegativeButton("取消",null);
        //设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unBindingEmail();
            }
        });
        //创建AlertDialog对象
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 解绑邮箱
     */
    private void unBindingEmail(){
        new Thread() {
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("account",LoginActivity.user.getAccount()).build();
                final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/unBindingEmail").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        sendMessage(3,result);
                    }
                });
            }
        }.start();
    }

    /**
     * 切换账号
     */
    private void regout() {
        mTencent.logout(getApplicationContext());

        /** 删除SharedPreferences内Token信息*/
        SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        /** 跳转到登录页面*/
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    /**
     * 返回Activity的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                mTencent.handleResultData(data, loginListener);
            }
        }
        if(requestCode == 1){
            if (data != null) {
                final Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getApplicationContext().getContentResolver().openInputStream(uri));
                    updatePhoto(saveBitmapFile(bitmap));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Log.i("lww", uri.getPath());
            } else {
                Log.i("lww", "打开相册返回data为null");
            }
        }
    }

    /**
     * Bitmap对象保存为图片文件
     */
    public File saveBitmapFile(Bitmap bitmap) throws FileNotFoundException {
        File file = new File(getFilesDir().getAbsolutePath()+"/photo.png");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return file;
    }

}
