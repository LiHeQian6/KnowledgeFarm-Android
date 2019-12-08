package com.li.knowledgefarm.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.Login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Settings.SettingActivity;
import com.li.knowledgefarm.Shop.ShopActivity;
import com.li.knowledgefarm.Study.SubjectListActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
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
    private TextView nickName;
    private TextView level;
    private TextView account;
    private TextView money;
    private GridLayout lands;
    private Dialog bagDialog;
    private OkHttpClient okHttpClient;
    private String bagMessages;
    private Handler bagMessagesHandler;
    private Gson gson;
    private List<BagMessagesBean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        okHttpClient = new OkHttpClient();
        gson = new Gson();
        dataList = new ArrayList<>();

        setStatusBar();
        getViews();
        addListener();
        showLand();
    }

    private void showUserInfo() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.huancun)
                .error(R.drawable.meigui)
                .fallback(R.drawable.meigui)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(this).load(LoginActivity.user.getPhoto()).apply(requestOptions).into(photo);
        nickName.setText(LoginActivity.user.getNickName());
        account.setText("账号"+LoginActivity.user.getAccout());
        level.setText("Lv:"+LoginActivity.user.getLevel());
        money.setText("金币:"+LoginActivity.user.getMoney());
        waterCount.setText(LoginActivity.user.getWater()+"");
        fertilizerCount.setText(LoginActivity.user.getFertilizer()+"");
    }

    private void showLand() {
        for (int i=1;i<19;i++){
            ImageView land = new ImageView(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(200,200);
            land.setLayoutParams(lp);
            land.setImageResource(R.drawable.land);
            final int finalI = i;
            land.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("aaaa","点击了land"+ finalI);
                }
            });
            lands.addView(land);
        }
        //lands.setPivotY(0);
        //lands.setPivotX(0);
        lands.setRotationX(30);
        //lands.setCameraDistance(4000);
        //lands.setRotationY(10);
        lands.setRotation(15);

    }

    private void addListener() {
        learn.setOnClickListener(new MainListener());
        water.setOnClickListener(new MainListener());
        fertilizer.setOnClickListener(new MainListener());
        bag.setOnClickListener(new MainListener());
        shop.setOnClickListener(new MainListener());
        pet.setOnClickListener(new MainListener());
        setting.setOnClickListener(new MainListener());
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
    }
    class MainListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.learn:
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SubjectListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.water:
                    break;
                case R.id.fertilizer:
                    break;
                case R.id.bag:
                    showSingleAlertDialog();
                    break;
                case R.id.shop:
                    intent = new Intent();
                    intent.setClass(MainActivity.this, ShopActivity.class);
                    startActivity(intent);
                    break;
                case R.id.pet:
                    break;
                case R.id.setting:
                    intent = new Intent();
                    intent.setClass(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * @Description 获取背包信息数据
     * @Auther 孙建旺
     * @Date 下午 2:38 2019/12/08
     * @Param []
     * @return void
     */
    private void getBagMessages(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url("http://10.7.87.220:8080/FarmKnowledge/bag/initUserBag?userId=37").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        bagMessages = "Fail";
                        Message message = Message.obtain();
                        message.obj = bagMessages;
                        bagMessagesHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        bagMessages = response.body().string();
                        Message message = Message.obtain();
                        message.obj = bagMessages;
                        bagMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * @Description 背包弹出框
     * @Auther 孙建旺
     * @Date 下午 2:01 2019/12/08
     * @Param [position]
     * @return void
     */
    private void showSingleAlertDialog(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this,R.style.dialog_soft_input);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.bag_girdview, null);
        final GridView gridView = layout.findViewById(R.id.bag_grid_view);
        alertBuilder.setView(layout);
        bagDialog = alertBuilder.create();
        bagDialog.show();
        getBagMessages();
        bagMessagesHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String messages = (String)msg.obj;
                if(!messages.equals("Fail")){
                    Type type = new TypeToken<List<BagMessagesBean>>(){}.getType();
                    dataList = gson.fromJson(messages,type);
                    BagCustomerAdapter customerAdapter = new BagCustomerAdapter(alertBuilder.getContext(),dataList,R.layout.gird_adapteritem);
                    gridView.setAdapter(customerAdapter);
                }else{
                    Toast toast = Toast.makeText(MainActivity.this,"获取数据失败！",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
        WindowManager.LayoutParams attrs = bagDialog.getWindow().getAttributes();
        attrs.gravity = Gravity.RIGHT;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int)(300*scale+0.5f);
        attrs.height =(int)(400*scale+0.5f);
        bagDialog.getWindow().setAttributes(attrs);
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }
}