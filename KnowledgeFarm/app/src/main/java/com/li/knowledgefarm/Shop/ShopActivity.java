package com.li.knowledgefarm.Shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.DisplayUtils;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.GuideHelper;
import com.li.knowledgefarm.Util.GuideHelper.TipData;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.Crop;
import com.li.knowledgefarm.entity.PetUtil;
import com.li.knowledgefarm.entity.PetVO;
import com.li.knowledgefarm.entity.ShopItemBean;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShopActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private Gson gson;
    private List<Crop> shopList;
    private List<PetVO> pet_list;
    private List<PetUtil> petUtils;
    private Map<String,List> itemList;
    private GridView plant_gridView;
    private GridView pet_gridView;
    private ViewPager viewPager;
    private Handler messages;
    private ImageView imageView;
    private int displayWidth;
    private int displayHeight;
    private MyFragmentPagerAdapter adapter;
    private WindowManager wm;
    private DisplayMetrics ds;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        okHttpClient = OkHttpUtils.getInstance(this);
        getViews();
        messages = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String message = (String) msg.obj;
                        if (!message.equals("") && msg.arg1 == 200) {
                            Type type = new TypeToken<List<Crop>>() {
                            }
                                    .getType();
                            shopList = gson.fromJson(message, type);
                            itemList.put("plant", shopList);
                            if (itemList.size() == 3) {
                                setViewPagerAdapter();
                            }
                        } else {
                            CustomerToast.getInstance(ShopActivity.this,"网络出了点问题", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        String pet_string = (String) msg.obj;
                        if (!pet_string.equals("") && msg.arg1 == 200) {
                            Type type = new TypeToken<List<PetVO>>() {
                            }.getType();
                            pet_list = gson.fromJson(pet_string, type);
                            itemList.put("pet", pet_list);
                            if (itemList.size() == 3) {
                                setViewPagerAdapter();
                            }
                        } else {
                            CustomerToast.getInstance(ShopActivity.this,"网络出了点问题",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        String utils_string = (String)msg.obj;
                        if(!utils_string.equals("") && msg.arg1 == 200){
                            Type type = new TypeToken<List<PetUtil>>(){}.getType();
                            petUtils = gson.fromJson(utils_string,type);
                            itemList.put("utils",petUtils);
                            if(itemList.size() == 3){
                                setViewPagerAdapter();
                            }
                        }
                        break;
                }
            }
        };
        getShopingsItems();
        getAllPets();
        getAllPetUtils();
        setViewSize();
        registerOnclickListener();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullScreen.NavigationBarStatusBar(ShopActivity.this,true);
    }

    /**
     * @Description 设置适配器
     * @Author 孙建旺
     * @Date 上午11:47 2020/05/13
     * @Param []
     * @return void
     */
    private void setViewPagerAdapter() {
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),itemList);
        viewPager.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText(" 植物 "));
        tabLayout.addTab(tabLayout.newTab().setText(" 宠物 "));
        tabLayout.addTab(tabLayout.newTab().setText(" 道具 "));
        tabLayout.setupWithViewPager(viewPager);
        adapter.notifyDataSetChanged();
        guide();
    }
    /**
     * @Description 设置控件适配屏幕
     * @Auther 孙建旺
     * @Date 下午 4:14 2019/12/15
     * @Param []
     * @return void
     */
    private void setViewSize() {
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayHeight = ds.heightPixels;
        displayWidth = ds.widthPixels;

        LinearLayout.LayoutParams params_gridview = new LinearLayout.LayoutParams((int)(displayWidth*0.8),(int)(displayHeight*0.7));
        params_gridview.gravity = Gravity.CENTER_HORIZONTAL;
        viewPager.setLayoutParams(params_gridview);
    }

    /**
     * @Description 获取控件ID及初始化
     * @Auther 孙建旺
     * @Date 上午 11:50 2019/12/07
     * @Param []
     * @return void
     */
    private void getViews() {
        gson = new Gson();
        shopList = new ArrayList<>();
        pet_list = new ArrayList<>();
        petUtils = new ArrayList<>();
        tabLayout = findViewById(R.id.tab_layout);
        imageView = findViewById(R.id.goBack);
        viewPager = findViewById(R.id.pager_view);
        pet_gridView = View.inflate(this,R.layout.pet_gridview_layout,null).findViewById(R.id.pet_gird_view);
        plant_gridView = View.inflate(this,R.layout.plant_gridview_layout,null).findViewById(R.id.plant_gird_view);
        itemList = new HashMap<>();
        wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
    }

    /**
     * @Description 获取所有宠物道具
     * @Author 孙建旺
     * @Date 上午9:57 2020/05/09
     * @Param []
     * @return void
     */
    private void getAllPetUtils(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/petutil/showInStore").build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        Message message = Message.obtain();
                        message.what = 3;
                        message.obj = response.body().string();
                        message.arg1 = response.code();
                        messages.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * @Description 获取所有宠物数据
     * @Author 孙建旺
     * @Date 下午3:55 2020/05/01
     * @Param []
     * @return void
     */
    private void getAllPets(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                final Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/pet/showInStore").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        Message message = Message.obtain();
                        message.what = 2;
                        message.arg1 = response.code();
                        message.obj = response.body().string();
                        messages.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * @Description 获取商品数据
     * @Auther 孙建旺
     * @Date 上午 10:51 2019/12/07
     * @Param []
     * @return void
     */
    private void getShopingsItems() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.URL)+"/crop/initCrop")
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String shopItemMessages = response.body().string();
                        Message message = Message.obtain();
                        message.what = 1;
                        message.arg1 = response.code();
                        message.obj = shopItemMessages;
                        messages.sendMessage(message);
                    }
                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        Log.i("error","请求失败！");
                    }
                });
            }
        }.start();
    }

    private void registerOnclickListener(){
    }

    private class CustomerOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
            }
        }
    }

    public void guide(){
        SharedPreferences guide = getSharedPreferences("guide", MODE_PRIVATE);
        boolean main = guide.getBoolean("shop", true);
        if (!main){
            return;
        }
        final GuideHelper guideHelper = new GuideHelper(ShopActivity.this);
        //第一页
        TextView hello = new TextView(this);
        hello.setText("点击选择要购买的商品类型\n或者直接滑动屏幕切换");
        hello.setTextSize(30);
        TipData tipData = new TipData(hello, Gravity.CENTER);
        guideHelper.addPage(tipData);
        //第二页
        TextView textView = new TextView(this);
        textView.setText("点击心仪的种子进行购买！");
        textView.setTextSize(30);
        TipData tipData1 = new TipData(textView, Gravity.CENTER);
        guideHelper.addPage(tipData1);
        guideHelper.show(false);
        guide.edit().putBoolean("shop",false).apply();
    }

}
