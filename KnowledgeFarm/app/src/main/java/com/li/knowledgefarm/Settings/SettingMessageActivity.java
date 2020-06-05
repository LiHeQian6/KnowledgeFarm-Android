package com.li.knowledgefarm.Settings;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDoneException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.GlideEngine;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UpdateUtil;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.User;
import com.li.knowledgefarm.entity.UserAuthority;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingMessageActivity extends AppCompatActivity {

    /** Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI*/
    private Tencent mTencent;
    /** 其中mAppId是分配给第三方应用的appid，类型为String*/
    public String mAppId = "1110065654";//101827370
    /** 授权登录监听器*/
    private IUiListener loginListener;
    /** 获取用户信息监听器*/
    private IUiListener userInfoListener;
    private String openId;
    private String accessToken;
    private String expires;
    private Handler handler;
    /** 用户信息*/
    private UserInfo mInfo;
    private Button my_message; //我的信息按钮
    private Button system_setting; //系统设置按钮
    private Button about;//版权信息按钮
    private LinearLayout user_mess_li; //个人信息展示块
    private LinearLayout system_setting_li; //系统设置展示块
    private LinearLayout system_about; // 版权信息展示块
    private TextView show_grade; //展示用户年级
    private TextView user_nickName; //展示昵称
    private TextView user_account; //展示账号
    private TextView user_QQ; //展示绑定QQ
    private TextView user_Email; //展示绑定Email
    private Spinner select_grade; //年级选择器
    private Button change_grade; //修改年级Button
    private Button change_QQ; //绑定QQButton
    private Button change_Email; //绑定邮箱Button
    private Button change_password;
    private ImageView user_photo; //用户头像
    private User user;
    private OkHttpClient okHttpClient;
    private ChangeEmailPopUpWindow popUpWindow; //修改邮箱弹出框
    private ChangeNicknamePop nicknamePop; //修改昵称弹出框
    private ChangePasswordPop   passwordPop; //修改密码弹出框
    private IfDoPop ifDoPop;//询问弹出框
    private ImageView change_nickname;
    /** 选中的年级*/
    private String newGrade;
    private String[] spin;
    private ImageView returns_message;
    private Button log_out;
    private WindowManager wm;
    private DisplayMetrics ds;
    private Button check_update;//检查更新按钮
    private File userPhoto = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message);
        okHttpClient = OkHttpUtils.getInstance(this);
        FullScreen.NavigationBarStatusBar(SettingMessageActivity.this,true);
        getViews();
        registListener();
        ShowUserMessage();
        resultHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShowUserMessage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null!=popUpWindow&&popUpWindow.isShowing()){
            return false;
        }
        return super.onTouchEvent(event);
    }

    /**
     * @Description 展示用户信息
     * @Author 孙建旺
     * @Date 下午5:44 2020/05/24
     * @Param []
     * @return void
     */
    private void ShowUserMessage(){
        user = UserUtil.getUser();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.photo)
                .error(R.drawable.photo)
                .fallback(R.drawable.photo)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(getApplicationContext()).load(user.getPhoto()).apply(requestOptions).into(user_photo);
        user_account.setText("账号："+user.getAccount());
        user_nickName.setText("昵称："+user.getNickName());
        show_grade.setText(DoubleToString(user.getGrade()));
        if(!(user.getEmail() == null || user.getEmail().equals(""))){
            user_Email.setText("已绑定"+user.getEmail());
            change_Email.setText("解绑");
        }else{
            user_Email.setText("未绑定");
            change_Email.setText("去绑定");
        }
        if(!(user.getUserAuthority() == null)){
            user_QQ.setText("已绑定");
            change_QQ.setText("解绑");
        }else {
            user_QQ.setText("未绑定");
            change_QQ.setText("去绑定");
        }
    }

    /**
     * @Description 注册监听器
     * @Author 孙建旺
     * @Date 下午5:45 2020/05/24
     * @Param []
     * @return void
     */
    private void registListener(){
        my_message.setOnClickListener(new CustomerOnclickListener());
        system_setting.setOnClickListener(new CustomerOnclickListener());
        about.setOnClickListener(new CustomerOnclickListener());
        change_grade.setOnClickListener(new CustomerOnclickListener());
        user_photo.setOnClickListener(new CustomerOnclickListener());
        change_Email.setOnClickListener(new CustomerOnclickListener());
        change_nickname.setOnClickListener(new CustomerOnclickListener());
        change_QQ.setOnClickListener(new CustomerOnclickListener());
        returns_message.setOnClickListener(new CustomerOnclickListener());
        change_password.setOnClickListener(new CustomerOnclickListener());
        log_out.setOnClickListener(new CustomerOnclickListener());
        check_update.setOnClickListener(new CustomerOnclickListener());
        select_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newGrade = spin[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * @Description 获取控件ID
     * @Author 孙建旺
     * @Date 下午9:32 2020/04/28
     * @Param []
     * @return void
     */
    private void getViews() {
        my_message = findViewById(R.id.my_message_btn);
        system_setting = findViewById(R.id.system_setting);
        about = findViewById(R.id.about);
        user_mess_li = findViewById(R.id.user_mess_li);
        system_setting_li = findViewById(R.id.system_setting_li);
        system_about = findViewById(R.id.system_about_li);
        show_grade = findViewById(R.id.show_grade);
        select_grade = findViewById(R.id.grade_spinner);
        change_grade = findViewById(R.id.btnUpdateGrade);
        user_photo = findViewById(R.id.btnUpdatePhoto);
        user_account = findViewById(R.id.user_account);
        user_nickName = findViewById(R.id.user_nickname);
        change_Email = findViewById(R.id.btnUpdateEmail);
        change_QQ = findViewById(R.id.btnUpdateQQ);
        change_password = findViewById(R.id.change_password);
        user_QQ = findViewById(R.id.showQQ);
        user_Email = findViewById(R.id.showEmail);
        change_nickname = findViewById(R.id.change_nickname);
        log_out = findViewById(R.id.btn_logout);
        mTencent = Tencent.createInstance(mAppId, getApplicationContext());
        spin = getResources().getStringArray(R.array.sarry);
        returns_message = findViewById(R.id.returns_message);
        check_update = findViewById(R.id.check_update);
        wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
    }


    /**
     * @Description 线程服务端返回处理
     * @Author 孙建旺
     * @Date 下午5:19 2020/05/24
     * @Param []
     * @return void
     */
    private void resultHandler(){
        /** 线程服务端返回处理*/
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1: //绑定QQ判断
                        switch ((String)msg.obj){
                            case "true":
                                /** 存入SharedPreferences*/
                                SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("opId",openId);
                                editor.putString("tk",accessToken);
                                editor.putLong("start",new Date().getTime());
                                editor.putInt("expires", Integer.parseInt(expires));
                                editor.commit();
                                CustomerToast.getInstance(getApplicationContext(),"绑定QQ成功",Toast.LENGTH_SHORT).show();
                                break;
                            case "false":
                                UserUtil.getUser().setUserAuthority(null);
                                CustomerToast.getInstance(getApplicationContext(),"绑定QQ失败",Toast.LENGTH_SHORT).show();
                                break;
                            case "already":
                                CustomerToast.getInstance(getApplicationContext(),"该QQ号已被其他账号绑定",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case 2: //解绑QQ判断
                        switch ((String)msg.obj){
                            case "true":
                                mTencent.logout(getApplicationContext());
                                /** 删除SharedPreferences内Token信息*/
                                UserUtil.getUser().setUserAuthority(null);
                                ShowUserMessage();
                                SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                CustomerToast.getInstance(getApplicationContext(),"解绑QQ成功",Toast.LENGTH_SHORT).show();
                                break;
                            case "false":
                                CustomerToast.getInstance(getApplicationContext(),"解绑QQ失败",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case 4: //修改头像判断
                        String aString = (String)msg.obj;
                        if(aString.equals("false") || aString.equals("")){
                            Log.e("photo","上传头像失败1");
                            CustomerToast.getInstance(getApplicationContext(),"头像上传失败", Toast.LENGTH_SHORT).show();
                        }else if(aString.equals("null")){
                            Log.e("photo","上传头像失败2");
                            CustomerToast.getInstance(getApplicationContext(), "图片为空", Toast.LENGTH_SHORT).show();
                        }else{
                            UserUtil.getUser().setPhoto(aString);
                            Glide.with(getApplicationContext()).load(userPhoto).into(user_photo);
                            CustomerToast.getInstance(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            PictureFileUtils.deleteAllCacheDirFile(getApplicationContext());
                        }
                        break;
                    case 5: // 修改年级判断
                        if(msg.obj.equals("true")){
                            UserUtil.getUser().setGrade(transmit(newGrade));
                            change_grade.setText("修改");
                            show_grade.setVisibility(View.VISIBLE);
                            show_grade.setText(DoubleToString(UserUtil.getUser().getGrade()));
                            select_grade.setVisibility(View.GONE);
                            CustomerToast.getInstance(SettingMessageActivity.this,"年级修改成功",Toast.LENGTH_SHORT).show();
                        }else{
                            CustomerToast.getInstance(SettingMessageActivity.this,"年级修改失败",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
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
                            FormBody formBody = new FormBody.Builder().add("openId",openId).build();
                            final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/bindingQQ").build();
                            Call call = okHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.i("lww","请求失败");
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    OkHttpUtils.unauthorized(response.code());
                                    String result = response.body().string();
                                    Message message = new Message();
                                    message.what = 1;
                                    message.obj = result;
                                    handler.sendMessage(message);
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
     * 拉起QQ登陆，返回登录信息
     */
    private void bindingQQ(){
        if (!mTencent.isSessionValid()) {
            Log.i("lww", "onClickLogin session无效");
            loginListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject response = (JSONObject) o;
                    try {
                        UserUtil.getUser().setUserAuthority(new UserAuthority());
                        ShowUserMessage();
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
     * 解绑QQ
     */
    private void unBindingQQ(){

        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().build();
                final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/unBindingQQ").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String result = response.body().string();
                        Message message = Message.obtain();
                        message.obj = result;
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * 打开相册
     */
    public void openPhonePhoto(){
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
                        .addFormDataPart("photo", URLEncoder.encode(UserUtil.getUser().getPhoto()))
                        .addFormDataPart("upload",photo.getName(),requestBody)
                        .build();
                Request request = new Request.Builder().post(body).url(getResources().getString(R.string.URL)+"/user/updatePhoto").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        Log.i("photo",e.toString());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String result = response.body().string();
                        Message message = new Message();
                        message.what = 4;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
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
        if(requestCode == PictureConfig.CHOOSE_REQUEST){
            if(data != null) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList.size() > 0) {
                    if (selectList.get(0).isCompressed())
                        userPhoto = new File(selectList.get(0).getCompressPath());
                    else {
                        if (Build.VERSION.SDK_INT == 29)
                            userPhoto = new File(selectList.get(0).getAndroidQToPath());
                        else
                            userPhoto = new File(selectList.get(0).getRealPath());
                    }
                    if(userPhoto.exists()) {
                        updatePhoto(userPhoto);
                    }else {
                        CustomerToast.getInstance(getApplicationContext(),"获取图片失败",Toast.LENGTH_SHORT).show();
                    }
                }
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

    /**
     * @Description 展示绑定邮箱窗口
     * @Author 孙建旺
     * @Date 上午10:57 2020/05/05
     * @Param [type]
     * @return void
     */
    private void ShowChangeMessagePop(String type){
        popUpWindow = new ChangeEmailPopUpWindow(this,type);
        popUpWindow.setHeight((int)(ds.heightPixels));
        popUpWindow.setWidth((int)(ds.widthPixels));
        popUpWindow.showAtLocation(change_Email, Gravity.CENTER,0,0);
        popUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ShowUserMessage();
            }
        });
    }

    /**
     * @Description 展示修改昵称窗口
     * @Author 孙建旺
     * @Date 上午10:57 2020/05/05
     * @Param []
     * @return void
     */
    private void ShowChangeNickNamePop(){
        nicknamePop = new ChangeNicknamePop(this);
        nicknamePop.setHeight((int)(ds.heightPixels));
        nicknamePop.setWidth((int)(ds.widthPixels));
        nicknamePop.showAtLocation(change_Email, Gravity.CENTER,0,0);
        nicknamePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ShowUserMessage();
            }
        });
    }

    /**
     * @Description 展示修改密码窗口
     * @Author 孙建旺
     * @Date 上午10:58 2020/05/05
     * @Param []
     * @return void
     */
    private void ShowChangePasswordPop(){
        passwordPop = new ChangePasswordPop(this);
        passwordPop.setHeight((int)(ds.heightPixels));
        passwordPop.setWidth((int)(ds.widthPixels));
        passwordPop.showAtLocation(change_Email, Gravity.CENTER,0,0);
        passwordPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ShowUserMessage();
            }
        });
    }

    /**
     * @Description 展示询问弹窗
     * @Author 孙建旺
     * @Date 上午11:26 2020/05/05
     * @Param []
     * @return void
     */
    private void ShowIfDoPop(String type){
        ifDoPop = new IfDoPop(this,type);
        ifDoPop.setHeight((int)(ds.heightPixels*0.55));
        ifDoPop.setWidth((int)(ds.widthPixels*0.4));
        ifDoPop.showAtLocation(change_Email, Gravity.CENTER,0,0);
        ifDoPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ShowUserMessage();
            }
        });
    }

    /**
     * 保存
     */
    private void save(){
        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("grade",""+transmit(newGrade)).build();
                final Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/updateUserGrade").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String result = response.body().string();
                        Message message = new Message();
                        message.obj = result;
                        message.what = 5;
                        message.arg2 = response.code();
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    private String DoubleToString(int grade){
        switch (grade){
            case 1:
                return "一年级 上";
            case 2:
                return "一年级 下";
            case 3:
                return "二年级 上";
            case 4:
                return "二年级 下";
            case 5:
                return "三年级 上";
            case 6:
                return "三年级 下";
        }
        return "一年级 上";
    }

    /**
     * 年级形式转换(string -> double)
     */
    private int transmit(String grade){
        switch (grade){
            case "一年级 上":
                return 1;
            case "一年级 下":
                return 2;
            case "二年级 上":
                return 3;
            case "二年级 下":
                return 4;
            case "三年级 上":
                return 5;
            case "三年级 下":
                return 6;
        }
        return 0;
    }

    private class CustomerOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.my_message_btn:
                    user_mess_li.setVisibility(View.VISIBLE);
                    system_setting_li.setVisibility(View.GONE);
                    system_about.setVisibility(View.GONE);
                    break;
                case R.id.system_setting:
                    user_mess_li.setVisibility(View.GONE);
                    system_about.setVisibility(View.GONE);
                    system_setting_li.setVisibility(View.VISIBLE);
                    break;
                case R.id.about:
                    user_mess_li.setVisibility(View.GONE);
                    system_about.setVisibility(View.VISIBLE);
                    system_setting_li.setVisibility(View.GONE);
                    break;
                case R.id.btnUpdateGrade:
                    if(change_grade.getText().toString().equals("修改")) {
                        show_grade.setVisibility(View.GONE);
                        select_grade.setVisibility(View.VISIBLE);
//                        int position = LoginActivity.user.getGrade() - 1;
//                        newGrade = spin[position];
//                        select_grade.setSelection(position,true);
                        change_grade.setText("保存");
                    }else {
                        save();
                    }
                    break;
                case R.id.btnUpdatePhoto:
                    //openPhonePhoto();
                    PictureSelector.create(SettingMessageActivity.this)
                            .openGallery(PictureMimeType.ofImage())
                            .imageEngine(GlideEngine.createGlideEngine())
                            .imageSpanCount(4)
                            .selectionMode(PictureConfig.SINGLE)
                            .minimumCompressSize(30)
                            .cutOutQuality(10)
                            .isCompress(true)
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                    break;
                case R.id.btnUpdateEmail:
                    if(change_Email.getText().toString().equals("去绑定")){
                        ShowChangeMessagePop("Email");
                    }else {
                        ShowIfDoPop("UnBindEmail");
                    }
                    break;
                case R.id.btnUpdateQQ:
                    if(change_QQ.getText().toString().equals("去绑定")) {
                        bindingQQ();
                    }else {
                        unBindingQQ();
                    }
                    break;
                case R.id.change_nickname:
                    ShowChangeNickNamePop();
                    break;
                case R.id.change_password:
                    ShowChangePasswordPop();
                    break;
                case R.id.returns_message:
                    finish();
                    break;
                case R.id.btn_logout:
                    ShowIfDoPop("LogOut");
                    break;
                case R.id.check_update:
                    UpdateUtil.checkUpdate(SettingMessageActivity.this);
                    break;
            }
        }
    }
}
