package com.li.knowledgefarm.MyFriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Settings.SettingActivity;
import com.li.knowledgefarm.Shop.ShopActivity;
import com.li.knowledgefarm.Study.SubjectListActivity;
import com.li.knowledgefarm.entity.BagCropNumber;
import com.li.knowledgefarm.entity.User;
import com.li.knowledgefarm.entity.UserCropItem;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class MyFriendActivity extends AppCompatActivity {
    private ImageView learn;
    private ImageView water;
    private TextView waterCount;
    private ImageView fertilizer;
    private TextView fertilizerCount;
    private ImageView bag;
    private ImageView shop;
    private ImageView pet;
    private ImageView setting;
    private ImageView photo;
    private ImageView harvest;
    private ImageView xzw;
    private ImageView xzf;
    private ImageView xzs;
    private TextView nickName;
    private TextView level;
    private TextView account;
    private TextView money;
    private ProgressBar experience;
    private TextView experienceValue;
    private GridLayout lands;
    private Dialog bagDialog;
    private Dialog ifExtention;
    private OkHttpClient okHttpClient;
    private Handler bagMessagesHandler;
    private Gson gson;
    private List<BagCropNumber> dataList;
    private List<UserCropItem> cropList;
    private Handler UpdataLands;
    private Handler cropMessagesHandler;
    private int selectLand=0;//选中第几块土地
    private Handler plantMessagesHandler;
    private long lastClickTime=0;
    private long FAST_CLICK_DELAY_TIME=500;
    private Handler waterMessagesHandler;
    private int selected=-2;//选中的是水壶0，肥料-1，收获-2
    private Handler operatingHandleMessage;
    private int selectedPlant=0;//选中的植物是第几块土地
    private int displayWidth;
    private int displayHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        dataList = new ArrayList<>();
        ImageView dog = findViewById(R.id.dog);
        Glide.with(this).asGif().load(R.drawable.mydog).into(dog);
        setStatusBar();
        getViews();
        addListener();
        getCrop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        showUserInfo();
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.URL)+"/user/findUserInfoByUserId?userId="+ LoginActivity.user.getId())
                        .build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("用户信息", "请求失败");
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result =  response.body().string();
                        if (result.equals("{}")) {
                            Log.e("用户信息","信息异常");
                        }else {
                            Log.e("用户信息",result);
                            Message message = new Message();
                            message.obj = LoginActivity.parsr(URLDecoder.decode(result), User.class);
                            LoginActivity.user = (User) message.obj;
                            Message msg = new Message();
                            msg.obj="true";
                            operatingHandleMessage.sendMessage(msg);
                            if(bagDialog!=null){
                                bagDialog.cancel();
                                selectLand=0;
                            }
                        }

                    }
                });
            }
        }.start();
        operatingHandleMessage=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String messages = (String)msg.obj;
                Log.e("operating",messages);
                if(!messages.equals("Fail")){
                    showUserInfo();
                }else{
                    Toast.makeText(MyFriendActivity.this,"网络异常！",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * 获取种植的作物信息
     */
    private void getCrop() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/usercrop/initUserCrop?userId="+LoginActivity.user.getId()).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        cropMessagesHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        cropMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
        cropMessagesHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String messages = (String)msg.obj;
                Log.e("cropList",messages);
                if(!messages.equals("Fail")){
                    Type type = new TypeToken<List<UserCropItem>>(){}.getType();
                    cropList = gson.fromJson(messages,type);
                    showLand();
                }else{
                    Toast toast = Toast.makeText(MyFriendActivity.this,"网络异常！",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
    }

    /**
     * 展示用户信息
     */
    private void showUserInfo() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.huancun2)
                .error(R.drawable.meigui)
                .fallback(R.drawable.meigui)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(this).load(LoginActivity.user.getPhoto()).apply(requestOptions).into(photo);
        nickName.setText(LoginActivity.user.getNickName());
        account.setText("账号:"+LoginActivity.user.getAccout());
        level.setText("Lv:"+LoginActivity.user.getLevel());
        money.setText("金币:"+LoginActivity.user.getMoney());
        waterCount.setText(LoginActivity.user.getWater()+"");
        fertilizerCount.setText(LoginActivity.user.getFertilizer()+"");
        int[] levelExperience = getResources().getIntArray(R.array.levelExperience);
        int l = LoginActivity.user.getLevel() ;
        if(levelExperience.length<=l){
            experience.setMax(levelExperience[levelExperience.length-1]);
            experience.setProgress(levelExperience[levelExperience.length-1]);
            experienceValue.setText(""+levelExperience[levelExperience.length-1]+"/"+levelExperience[levelExperience.length-1]);
        }else {
            experience.setMax(levelExperience[l - 1]);
            experience.setProgress((int) LoginActivity.user.getExperience());
            experienceValue.setText("" + LoginActivity.user.getExperience() + "/" + levelExperience[l - 1]);
        }
    }

    /**
     * 生成土地
     */
    private void showLand() {
        int flag=0;
        lands.removeAllViews();
        for (int i=1;i<19;i++){
            final ImageView land = new ImageView(this);
            ImageView plant = new ImageView(this);
            final ImageView animation = new ImageView(this);
            Glide.with(MyFriendActivity.this).asGif().load(R.drawable.jiaoshui).into(animation);
            Glide.with(MyFriendActivity.this).asGif().load(R.drawable.shifei).into(animation);
            Glide.with(MyFriendActivity.this).asGif().load(R.drawable.shouhuog).into(animation);
            animation.setVisibility(View.GONE);
            RelativeLayout relativeLayout = new RelativeLayout(this);
            relativeLayout.addView(land);
            ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(160,160);
            animation.setLayoutParams(lp);
            land.setLayoutParams(lp);
            plant.setLayoutParams(lp);
            final int finalI = i;//第几块土地
            if(LoginActivity.user.getLandStauts(finalI)==-1) {
                if(flag==0){
                    plant.setImageResource(R.drawable.kuojian);
                    plant.setRotation(10);
                    relativeLayout.addView(plant);
                    //扩建
                    plant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                    flag++;
                }
                land.setImageResource(R.drawable.land_green);
            }
            else if (LoginActivity.user.getLandStauts(finalI)==0) {
                land.setImageResource(R.drawable.land);
                //种植
                land.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                            return;
                        }
                        lastClickTime = System.currentTimeMillis();
                        land.setImageResource(R.drawable.land_light);
                        selectLand=finalI;
                    }
                });
            }
            else {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.huancun2)
                        .error(R.drawable.meigui)
                        .fallback(R.drawable.meigui)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                land.setImageResource(R.drawable.land);
                plant.setRotationX(-50);
                plant.setRotation(-5);
                UserCropItem crop=null;
                //得到植物信息
                for (int j = 0; j < cropList.size(); j++) {
                    if(cropList.get(j).getUserCropId()==LoginActivity.user.getLandStauts(finalI)){
                        crop=cropList.get(j);
                        break;
                    }
                }
                if (crop!=null){
                    //展示植物不同阶段
                    final double status = (crop.getProgress()+0.0) / crop.getCrop().getMatureTime();
                    if(status <0.2){
                        plant.setImageResource(R.drawable.seed);
                    }else if (status<0.3){
                        Glide.with(this).load(crop.getCrop().getImg1()).apply(requestOptions).into(plant);
                    }else if (status<0.6){
                        Glide.with(this).load(crop.getCrop().getImg2()).apply(requestOptions).into(plant);
                    }else if (status<1){
                        Glide.with(this).load(crop.getCrop().getImg3()).apply(requestOptions).into(plant);
                    }else if (status==1){
                        Glide.with(this).load(crop.getCrop().getImg4()).apply(requestOptions).into(plant);
                    }
                    //植物成长进度条
                    final ProgressBar progressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
                    progressBar.setMax(crop.getCrop().getMatureTime());
                    progressBar.setProgress(crop.getProgress());
                    progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_bg));
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(140,20);
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    progressBar.setLayoutParams(layoutParams);
                    //植物成长值
                    final TextView value = new TextView(this);
                    value.setText(crop.getProgress()+"/"+crop.getCrop().getMatureTime());
                    value.setTextSize(8);
                    value.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                    value.setLayoutParams(layoutParams);
                    if(crop.getState()==0){
                        land.setImageResource(R.drawable.land_gan);
                    }
                    //添加视图
                    relativeLayout.addView(plant);
                    relativeLayout.addView(progressBar);
                    relativeLayout.addView(value);
                    relativeLayout.addView(animation);
                    //浇水、施肥、收获
                    final UserCropItem finalCrop = crop;
                    plant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            land.setImageResource(R.drawable.land_light);
                            if(selected==0) {
                                selectedPlant=finalI;
                                if(status==1) {
                                    Toast.makeText(MyFriendActivity.this, "植物已经成熟哦！", Toast.LENGTH_SHORT).show();
                                    if(finalCrop.getState()==0){
                                        land.setImageResource(R.drawable.land_gan);
                                    }else
                                        land.setImageResource(R.drawable.land);
                                }
                                else{
                                    Glide.with(MyFriendActivity.this).asGif().load(R.drawable.jiaoshui).into(animation);
                                }
                            }else if(selected==-1){
                                selectedPlant=finalI;
                                if(status==1) {
                                    Toast.makeText(MyFriendActivity.this, "植物已经成熟哦！", Toast.LENGTH_SHORT).show();
                                    if(finalCrop.getState()==0){
                                        land.setImageResource(R.drawable.land_gan);
                                    }else
                                        land.setImageResource(R.drawable.land);
                                }else{
                                    Glide.with(MyFriendActivity.this).asGif().load(R.drawable.shifei).into(animation); }
                            }else{
                                selectedPlant=finalI;
                                if(status==1) {
                                    Glide.with(MyFriendActivity.this).asGif().load(R.drawable.shouhuog).into(animation);
                                }
                                else {
                                    Toast.makeText(MyFriendActivity.this, "植物还没有成熟哦！", Toast.LENGTH_SHORT).show();
                                    if(finalCrop.getState()==0){
                                        land.setImageResource(R.drawable.land_gan);
                                    }else
                                        land.setImageResource(R.drawable.land);
                                }
                            }
                            waterMessagesHandler = new Handler() {
                                @Override
                                public void handleMessage(@NonNull Message msg) {
                                    final String messages = (String) msg.obj;
                                    Log.e("Watering", messages);
                                    if (!messages.equals("Fail")) {
                                        if (messages.equals("false")) {
                                            Toast.makeText(MyFriendActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //Toast.makeText(MainActivity.this, "操作成功！", Toast.LENGTH_SHORT).show();
                                            animation.setVisibility(View.VISIBLE);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    getCrop();
                                                    getUserInfo();
                                                    if(messages.equals("up")){
                                                    }
                                                    animation.setVisibility(View.GONE);
                                                }
                                            },1000);

                                        }
                                    } else {
                                        Toast.makeText(MyFriendActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                                    }
                                    land.setImageResource(R.drawable.land);
                                }
                            };
                        }
                    });
                }

            }
            lands.addView(relativeLayout);
        }
    }

    private void addListener() {
        shop.setOnClickListener(new MainListener());
        pet.setOnClickListener(new MainListener());
        setting.setOnClickListener(new MainListener());
        harvest.setOnClickListener(new MainListener());
    }

    private void getViews() {
        learn=findViewById(R.id.learn);
        water=findViewById(R.id.water);
        waterCount=findViewById(R.id.waterCount);
        fertilizer=findViewById(R.id.fertilizer);
        fertilizerCount=findViewById(R.id.fertilizerCount);
        bag=findViewById(R.id.bag);
        shop=findViewById(R.id.shop);
        pet=findViewById(R.id.pet);
        setting=findViewById(R.id.setting);
        photo=findViewById(R.id.photo);
        nickName=findViewById(R.id.nickName);
        level=findViewById(R.id.level);
        money=findViewById(R.id.money);
        account=findViewById(R.id.account);
        lands=findViewById(R.id.lands);
        experience=findViewById(R.id.experience);
        experienceValue=findViewById(R.id.experienceValue);
        xzw=findViewById(R.id.xzw);
        xzf=findViewById(R.id.xzf);
        xzs=findViewById(R.id.xzs);
        harvest=findViewById(R.id.harvest);
        Glide.with(this).asGif().load(R.drawable.xuanzhong4).into(xzw);
        Glide.with(this).asGif().load(R.drawable.xuanzhong4).into(xzf);
        Glide.with(this).asGif().load(R.drawable.xuanzhong4).into(xzs);
    }

    class MainListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
//                case R.id.learn:
//                    Intent intent = new Intent();
//                    intent.setClass(MyFriendActivity.this, SubjectListActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.water:
//                    selected=0;
//                    xzw.setVisibility(View.VISIBLE);
//                    xzf.setVisibility(View.GONE);
//                    xzs.setVisibility(View.GONE);
//                    break;
//                case R.id.fertilizer:
//                    selected=-1;
//                    xzw.setVisibility(View.GONE);
//                    xzf.setVisibility(View.VISIBLE);
//                    xzs.setVisibility(View.GONE);
//                    break;
//                case R.id.harvest:
//                    selected=-2;
//                    xzw.setVisibility(View.GONE);
//                    xzf.setVisibility(View.GONE);
//                    xzs.setVisibility(View.VISIBLE);
//                    break;
//                case R.id.bag:
//                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
//                        return;
//                    }
//                    lastClickTime = System.currentTimeMillis();
//                    break;
//                case R.id.shop:
//                    intent = new Intent();
//                    intent.setClass(MyFriendActivity.this, ShopActivity.class);
//                    startActivity(intent);
//                    break;
                case R.id.pet:
                    break;
//                case R.id.setting:
//                    intent = new Intent();
//                    intent.setClass(MyFriendActivity.this, SettingActivity.class);
//                    startActivity(intent);
//                    break;
            }
        }
    }

    //退出时的时间
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MyFriendActivity.this, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }
}
