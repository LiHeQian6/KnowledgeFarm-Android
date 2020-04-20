package com.li.knowledgefarm.MyFriends;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.Login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Settings.SettingActivity;
import com.li.knowledgefarm.Shop.ShopActivity;
import com.li.knowledgefarm.Study.SubjectListActivity;
import com.li.knowledgefarm.entity.BagCropNumber;
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.User;
import com.li.knowledgefarm.entity.UserCropItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
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
    private ImageView myFriends;
    private TextView nickName;
    private TextView level;
    private TextView account;
    private TextView money;
    private ProgressBar experience;
    private TextView experienceValue;
    private FrameLayout lands;
    private ListView friendsListView;
    private Dialog bagDialog;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private List<BagCropNumber> dataList;
    private List<UserCropItem> cropList;
    private FriendsPage<User> friendsPage;
    private Handler cropMessagesHandler;
    private int selectLand=0;//选中第几块土地
    private long lastClickTime=0;
    private long FAST_CLICK_DELAY_TIME=500;
    private Handler waterMessagesHandler;
    private int selected=0;//选中的是水壶0，肥料-1，收获-2
    private Handler operatingHandleMessage;
    private int selectedPlant=0;//选中的植物是第几块土地
    private int displayWidth;
    private int displayHeight;
    private Handler friendsMessagesHandler;
    private EditText searchAccount;
    private RadioGroup searchSelected;
    private int searchSelectedItem=0;
    private User user;
    private float LAND_WIDTH_2=160;
    private float LAND_HEIGHT_2=80;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        dataList = new ArrayList<>();
        user= (User) getIntent().getSerializableExtra("friend");
        ImageView dog = findViewById(R.id.dog);
        Glide.with(this).asGif().load(R.drawable.mydog).into(dog);
        setStatusBar();
        getViews();
        addListener();
        getCrop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                        .url(getResources().getString(R.string.URL)+"/user/findUserInfoByUserId?userId="+LoginActivity.user.getId())
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
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/usercrop/initUserCrop?userId="+user.getId()).build();
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
        Glide.with(this).load(user.getPhoto()).apply(requestOptions).into(photo);
        nickName.setText(user.getNickName());
        account.setText("账号:"+user.getAccout());
        level.setText("Lv:"+user.getLevel());
        money.setText("金币:"+user.getMoney());
        waterCount.setText(LoginActivity.user.getWater()+"");
        fertilizerCount.setText(LoginActivity.user.getFertilizer()+"");
        int[] levelExperience = getResources().getIntArray(R.array.levelExperience);
        int l = user.getLevel() ;
        experience.setMax(levelExperience[l]-levelExperience[l-1]);
        experience.setProgress((int) user.getExperience()-levelExperience[l-1]);
        experienceValue.setText("" + user.getExperience() + "/" + levelExperience[l]);
    }

    /**
     * 生成土地
     */
    private void showLand(){
        lands.removeAllViews();
        int flag=0;
        float x=0;
        float y=0;
        for (int i = 1; i <19 ; i++) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View landGroup = inflater.inflate(R.layout.land_group,null);
            landGroup.setLayoutParams(new FrameLayout.LayoutParams(320,160));
            if ((i-1)%3==0){
                x=((i-1)/3)*LAND_WIDTH_2+LAND_WIDTH_2*2;
                y=((i-1)/3)*LAND_HEIGHT_2;
                landGroup.setTranslationX(x);
                landGroup.setTranslationY(y);
            }else{
                x = x - LAND_WIDTH_2;
                landGroup.setTranslationX(x);
                y = y + LAND_HEIGHT_2;
                landGroup.setTranslationY(y);
            }
            landGroup.setTag(""+i);
            final ImageView land = (ImageView) landGroup.findViewWithTag("land");
            ImageView plant=landGroup.findViewWithTag("plant");
            TextView progressNum = (TextView) landGroup.findViewWithTag("progressNum");
            ProgressBar progress = (ProgressBar) landGroup.findViewWithTag("progress");
            final ImageView animation = (ImageView) landGroup.findViewWithTag("animation");
            final int finalI = Integer.parseInt((String) landGroup.getTag());//第几块土地
            if(user.getLandStauts(finalI)==-1) {//土地状态为-1表示土地未开垦，当第一次运行到的时候表示该块土地上是扩建牌
            }
            else if (user.getLandStauts(finalI)==0) {
                land.setImageResource(R.drawable.land0);
                //种植
                land.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                            System.out.println("x"+motionEvent.getX()+"   y"+motionEvent.getY());
                            if (isSelectLand(motionEvent.getX(),motionEvent.getY())) {
                                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                                    return true;
                                }
                                lastClickTime = System.currentTimeMillis();
                                land.setImageResource(R.drawable.land_lights);
                                selectLand=finalI;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        land.setImageResource(R.drawable.land0);
                                    }
                                },200);
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
            else {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.meigui)
                        .fallback(R.drawable.meigui)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                land.setImageResource(R.drawable.land0);
                plant.setRotationX(-50);
                plant.setRotation(-5);
                UserCropItem crop=null;
                //得到植物信息
                for (int j = 0; j < cropList.size(); j++) {
                    if(cropList.get(j).getUserCropId()==user.getLandStauts(finalI)){
                        crop=cropList.get(j);
                        break;
                    }
                }
                if (crop!=null){
                    plant.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    progressNum.setVisibility(View.VISIBLE);
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
                    progress.setMax(crop.getCrop().getMatureTime());
                    progress.setProgress(crop.getProgress());
                    //植物成长值
                    progressNum.setText(crop.getProgress()+"/"+crop.getCrop().getMatureTime());
                    if(crop.getState()==0){
                        land.setImageResource(R.drawable.land_ganhan);
                    }
                    //浇水、施肥、收获
                    final UserCropItem finalCrop = crop;

                    land.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                                System.out.println("x"+motionEvent.getX()+"   y"+motionEvent.getY());
                                if (isSelectLand(motionEvent.getX(),motionEvent.getY())) {
                                    land.setImageResource(R.drawable.land_lights);
                                    if(selected==0) {
                                        selectedPlant=finalI;
                                        if(status==1) {
                                            Toast.makeText(MyFriendActivity.this, "植物已经成熟哦！", Toast.LENGTH_SHORT).show();
                                            if(finalCrop.getState()==0){
                                                land.setImageResource(R.drawable.land_ganhan);
                                            }else
                                                land.setImageResource(R.drawable.land0);
                                        }
                                        else{
                                            Glide.with(MyFriendActivity.this).asGif().load(R.drawable.jiaoshui).into(animation);
                                            operating(0);//浇水
                                        }
                                    }else if(selected==-1){
                                        selectedPlant=finalI;
                                        if(status==1) {
                                            Toast.makeText(MyFriendActivity.this, "植物已经成熟哦！", Toast.LENGTH_SHORT).show();
                                            if(finalCrop.getState()==0){
                                                land.setImageResource(R.drawable.land_ganhan);
                                            }else
                                                land.setImageResource(R.drawable.land0);
                                        }else{
                                            Glide.with(MyFriendActivity.this).asGif().load(R.drawable.shifei).into(animation);
                                            operating(-1);//施肥
                                        }
                                    }else{
                                        selectedPlant=finalI;
                                        if(status==1) {
                                            Glide.with(MyFriendActivity.this).asGif().load(R.drawable.shouhuog).into(animation);
                                            operating(-2);//成熟
                                        }
                                        else {
                                        }
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(finalCrop.getState()==0){
                                                    land.setImageResource(R.drawable.land_ganhan);
                                                }else
                                                    land.setImageResource(R.drawable.land0);
                                            }
                                        },200);
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
//                                                        upLevel();
                                                            }
                                                            animation.setVisibility(View.GONE);
                                                        }
                                                    },1000);

                                                }
                                            } else {
                                                Toast.makeText(MyFriendActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                                            }
                                            if(finalCrop.getState()==0){
                                                land.setImageResource(R.drawable.land_ganhan);
                                            }else
                                                land.setImageResource(R.drawable.land0);
                                        }
                                    };
                                }
                            }
                            return false;
                        }
                    });

                }

            }
            lands.addView(landGroup);
        }

    }


    /**
     * @Author li
     * @param x 横坐标
     * @param y 纵坐标
     * @return boolean
     * @Description 判断是否选中该块土地(即点击的区域是否在菱形区域内)
     * @Date 12:19 2020/3/19
     **/
    public boolean isSelectLand(float x,float y){
        float rx = (x-LAND_WIDTH_2)*LAND_HEIGHT_2;
        float ry = (LAND_HEIGHT_2-y)*LAND_WIDTH_2;
        if((Math.abs(rx)+Math.abs(ry))<=(LAND_WIDTH_2*2*LAND_HEIGHT_2*2/4)){
            return true;
        }else{
            return false;
        }
    }

    private void addListener() {
        learn.setOnClickListener(new MainListener());
        water.setOnClickListener(new MainListener());
        fertilizer.setOnClickListener(new MainListener());
        bag.setOnClickListener(new MainListener());
        shop.setOnClickListener(new MainListener());
        pet.setOnClickListener(new MainListener());
        TextView pett= findViewById(R.id.pett);
        pett.setText("回家");
        setting.setOnClickListener(new MainListener());
        harvest.setOnClickListener(new MainListener());
        myFriends.setOnClickListener(new MainListener());
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
        xzw.setVisibility(View.VISIBLE);
        xzf.setVisibility(View.GONE);
        xzs.setVisibility(View.GONE);
        myFriends=findViewById(R.id.friends);
        LinearLayout layout1=findViewById(R.id.bagbox);
        LinearLayout layout2=findViewById(R.id.shopbox);
        LinearLayout layout3=findViewById(R.id.settingbox);
        LinearLayout layout4=findViewById(R.id.harvestbox);
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
        layout4.setVisibility(View.GONE);
    }
    class MainListener implements View.OnClickListener {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.learn:
                    Intent intent = new Intent();
                    intent.setClass(MyFriendActivity.this, SubjectListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.water:
                    selected=0;
                    xzw.setVisibility(View.VISIBLE);
                    xzf.setVisibility(View.GONE);
                    xzs.setVisibility(View.GONE);
                    break;
                case R.id.fertilizer:
                    selected=-1;
                    xzw.setVisibility(View.GONE);
                    xzf.setVisibility(View.VISIBLE);
                    xzs.setVisibility(View.GONE);
                    break;
                case R.id.harvest:
                    selected=-2;
                    xzw.setVisibility(View.GONE);
                    xzf.setVisibility(View.GONE);
                    xzs.setVisibility(View.VISIBLE);
                    break;
                case R.id.bag:
                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                    //showBagMessages();
                    break;
                case R.id.shop:
                    intent = new Intent();
                    intent.setClass(MyFriendActivity.this, ShopActivity.class);
                    startActivity(intent);
                    break;
                case R.id.pet:
                    Intent back = new Intent(MyFriendActivity.this, MainActivity.class);
                    startActivity(back);
                    break;
                case R.id.setting:
                    intent = new Intent();
                    intent.setClass(MyFriendActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.friends:
                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                    showFriends();
                    myFriends.setVisibility(View.GONE);
                    break;
                case R.id.pre:
                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                    if (searchSelectedItem==0)
                        getFriendsInfo(friendsPage.getPrePageNum());
                    else
                        getAllInfo(friendsPage.getPrePageNum());
                    break;
                case R.id.next:
                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                    if (searchSelectedItem==0)
                        getFriendsInfo(friendsPage.getNextPageNum());
                    else
                        getAllInfo(friendsPage.getNextPageNum());
                    break;
                case R.id.search:
                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                    Log.e("searchSelectedItem",searchSelectedItem+"");
                    if (searchSelectedItem==0)
                        findFriendInfo(searchAccount.getText().toString());
                    else
                        findPeopleByAccount(searchAccount.getText().toString());
                    break;

            }
        }
    }

    /**
     * 根据账号在所有人中查询
     * @param account
     */
    private void findPeopleByAccount(final String account) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/userfriend/findAllUser?accout="+account).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj ="Fail";
                        friendsMessagesHandler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj =response.body().string();
                        friendsMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * 根据账号查询好友
     */
    private void findFriendInfo(final String account) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/userfriend/findUserFriend?userId="+LoginActivity.user.getId()+"&accout="+account).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj ="Fail";
                        friendsMessagesHandler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj =response.body().string();
                        friendsMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFriends() {
        final Dialog friendsDialog = new Dialog(this);
        //获取屏幕显示区域尺寸
        WindowManager.LayoutParams attrs = friendsDialog .getWindow().getAttributes();
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayHeight = ds.heightPixels;
        displayWidth = ds.widthPixels;
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.friends_dialog, null);
        friendsListView=layout.findViewById(R.id.friends_lv);
        searchAccount=layout.findViewById(R.id.search_account);
        searchSelected=layout.findViewById(R.id.searchSelected);
        //设置控件大小
        setFriendSize(layout);
        searchSelected.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.my:
                        friendsPage.setPrePageNum(1);
                        friendsPage.setNextPageNum(1);
                        searchSelectedItem=0;
                        getFriendsInfo(1);
                        break;
                    case R.id.all:
                        friendsPage.setPrePageNum(1);
                        friendsPage.setNextPageNum(1);
                        searchSelectedItem=1;
                        getAllInfo(1);
                        break;
                }

            }
        });
        Button search=layout.findViewById(R.id.search);
        ImageView per = layout.findViewById(R.id.pre);
        ImageView next = layout.findViewById(R.id.next);
        final TextView now = layout.findViewById(R.id.now);
        per.setOnClickListener(new MainListener());
        next.setOnClickListener(new MainListener());
        search.setOnClickListener(new MainListener());
        friendsMessagesHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String messages = (String)msg.obj;
                Log.e("好友",messages);
                if(!messages.equals("Fail")){
                    Type type = new TypeToken<FriendsPage<User>>(){}.getType();
                    friendsPage = gson.fromJson(messages,type);
                    now.setText(friendsPage.getCurrentPageNum()+"/"+friendsPage.getTotalPageNum());
                    FriendsCustomerAdapter customerAdapter = new FriendsCustomerAdapter(friendsDialog.getContext(),friendsPage.getList(),R.layout.friends_list_item,searchSelectedItem);
                    friendsListView.setAdapter(customerAdapter);
                    customerAdapter.notifyDataSetChanged();
                }else{
                    Toast toast = Toast.makeText(MyFriendActivity.this,"获取数据失败！",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
        getFriendsInfo(1);
        friendsDialog.setContentView(layout);
        friendsDialog.show();
        if (friendsDialog.getWindow() != null) {
            //bagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            friendsDialog.getWindow().setDimAmount(0f);//去除遮罩
        }
        attrs.gravity = Gravity.RIGHT;
        attrs.width = (int)(displayWidth*0.40);
        attrs.height = (int)(displayHeight*0.95);
        friendsDialog.getWindow().setAttributes(attrs);
        Window dialogWindow = friendsDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        friendsDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                myFriends.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * @Description 设置好友弹出框屏幕适配
     * @Auther 孙建旺
     * @Date 上午 9:00 2019/12/18
     * @Param []
     * @return void
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setFriendSize(View view) {
        LinearLayout layout_search = view.findViewById(R.id.layout_search);
        Button search=view.findViewById(R.id.search);
        ImageView pre = view.findViewById(R.id.pre);
        ImageView next = view.findViewById(R.id.next);
        TextView now = view.findViewById(R.id.now);

        LinearLayout.LayoutParams params_search = new LinearLayout.LayoutParams((int)(displayWidth*0.3),(int)(displayHeight*0.08));
        params_search.gravity = Gravity.CENTER_HORIZONTAL;
        layout_search.setLayoutParams(params_search);

        LinearLayout.LayoutParams params_edit = new LinearLayout.LayoutParams((int)(displayWidth*0.24),(int)(displayHeight*0.1));
        params_edit.gravity = Gravity.CENTER;
        searchAccount.setLayoutParams(params_edit);
        searchAccount.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.ShopTextColor));
        searchAccount.setHintTextColor(ContextCompat.getColor(getApplicationContext(),R.color.ShopTextColor));
        searchAccount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        LinearLayout.LayoutParams params_button = new LinearLayout.LayoutParams((int)(displayWidth*0.06),(int)(displayHeight*0.07));
        params_button.gravity = Gravity.CENTER;
        search.setLayoutParams(params_button);
        search.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.ShopTextColor));
        search.setTextSize((int)(displayHeight*0.02));
        search.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        LinearLayout.LayoutParams params_select = new LinearLayout.LayoutParams((int)(displayWidth*0.3),(int)(displayHeight*0.06));
        params_select.setMargins((int)(displayWidth*0.02),0,0,0);
        searchSelected.setLayoutParams(params_select);

        LinearLayout.LayoutParams params_listview = new LinearLayout.LayoutParams((int)(displayWidth*0.3),(int)(displayHeight*0.6));
        params_listview.gravity = Gravity.CENTER_HORIZONTAL;
        params_listview.setMargins(0,(int)(displayHeight*0.018),0,(int)(displayHeight*0.018));
        friendsListView.setLayoutParams(params_listview);
        friendsListView.setDividerHeight((int)(displayHeight*0.015));

        LinearLayout.LayoutParams params_pre = new LinearLayout.LayoutParams((int)(displayWidth*0.1),(int)(displayHeight*0.06));
        pre.setLayoutParams(params_pre);
        next.setLayoutParams(params_pre);
        now.setLayoutParams(params_pre);
        now.setTextSize((int)(displayHeight*0.02));
    }


    /**
     * 获得好友分页
     * @param pageNumber
     */
    private void getFriendsInfo(final int pageNumber) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/userfriend/findUserFriend?userId="+LoginActivity.user.getId()+"&pageNumber="+pageNumber).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj ="Fail";
                        friendsMessagesHandler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj =response.body().string();
                        friendsMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();

    }

    /**
     * 获得所有人分页
     * @param pageNumber
     */
    private void getAllInfo(final int pageNumber){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/userfriend/findAllUser?pageNumber="+pageNumber).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj ="Fail";
                        friendsMessagesHandler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj =response.body().string();
                        friendsMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDaiLog(final HashMap add){
        final Dialog dialog = new Dialog(this);
        View view = View.inflate(this, R.layout.math_return_dialog, null);
        TextView text=view.findViewById(R.id.waringText);
        ImageView cancel=view.findViewById(R.id.cancel_return);
        ImageView sure = view.findViewById(R.id.sure_return);
        if (((boolean) add.values().toArray()[0])){
            text.setText("你确定添加ta为好友并发送申请吗？");
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(add.keySet());
                    //addFriend();
                }
            });
        }else{
            text.setText("你确定从好友列表删除ta吗？");
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        setDialogSize(view);
        dialog.setContentView(view);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
        if (dialog.getWindow() != null) {
            //bagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0f);//去除遮罩
        }
        attrs.gravity = Gravity.CENTER;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int)(300*scale+0.5f);
        attrs.height =(int)(300*scale+0.5f);
        dialog.getWindow().setAttributes(attrs);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void setDialogSize(View view){
        //获取屏幕显示区域尺寸
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayHeight = ds.heightPixels;
        displayWidth = ds.widthPixels;

        ImageView cancel = view.findViewById(R.id.cancel_return);
        ImageView sure = view.findViewById(R.id.sure_return);
        TextView warning = view.findViewById(R.id.waringText);
        LinearLayout panduan = view.findViewById(R.id.panduan);

        LinearLayout.LayoutParams params_cancel = new LinearLayout.LayoutParams((int)(displayWidth*0.065),(int)(displayWidth*0.065));
        params_cancel.setMargins(0,0,(int)(displayWidth*0.08),0);
        cancel.setLayoutParams(params_cancel);

        LinearLayout.LayoutParams params_sure = new LinearLayout.LayoutParams((int)(displayWidth*0.065),(int)(displayWidth*0.065));
        sure.setLayoutParams(params_sure);

        warning.setTextSize((int)(displayWidth*0.012));

        LinearLayout.LayoutParams params_layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_layout.setMargins(0,(int)(displayHeight*0.12),0,0);
        panduan.setLayoutParams(params_layout);
    }

    /**
     * 浇水、施肥、收获操作
     */
    private void operating(final int operating) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request=null;
                if(operating==0)
                    request = new Request.Builder().url(getResources().getString(R.string.URL)+"/userfriend/waterForFriend?userId="+LoginActivity.user.getId()+"&landNumber=land"+selectedPlant+"&friendId="+user.getId()).build();
                else if(operating==-1){
                    request = new Request.Builder().url(getResources().getString(R.string.URL)+"/userfriend/fertilizerForFriend?userId="+LoginActivity.user.getId()+"&landNumber=land"+selectedPlant+"&friendId="+user.getId()).build();
                }/*else{
                    request = new Request.Builder().url(getResources().getString(R.string.URL)+"/user/harvest?userId="+LoginActivity.user.getId()+"&landNumber=land"+selectedPlant).build();
                }*/
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj ="Fail";
                        waterMessagesHandler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj =response.body().string();
                        waterMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
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