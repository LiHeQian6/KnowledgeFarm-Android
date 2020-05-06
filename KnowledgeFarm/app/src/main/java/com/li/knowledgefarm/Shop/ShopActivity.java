package com.li.knowledgefarm.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.Login.StartActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Settings.ChangePasswordPop;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.entity.Pet;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShopActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private Gson gson;
    private List<ShopItemBean> shopList;
    private List<Pet> pet_list;
    private Map<String,GridView> gridViewList;
    private Map<String,List> itemList;
    private GridView plant_gridView;
    private GridView pet_gridView;
    private ViewPager viewPager;
    private Handler messages;
    private ImageView imageView;
    private AlertDialog alertDialog = null;
    private ImageView imgBtnJian;
    private ImageView imgBtnPlus;
    private EditText shopNumber;
    private Handler doAfterAdd;
    private long lastClickTime = 0L;
    // 两次点击间隔不能少于1000ms
    private static final int FAST_CLICK_DELAY_TIME = 1000;
    public static Boolean mIsScroll = false;
    private int displayWidth;
    private int displayHeight;
    private Toast toast;
    private MyFragmentPagerAdapter adapter;
    private WindowManager wm;
    private DisplayMetrics ds;
    private PetItemPopUpWindow petItemPopUpWindow;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        getViews();
        messages = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String message = (String)msg.obj;
                        if(!message.equals("") && msg.arg1 == 200) {
                            Type type = new TypeToken<List<ShopItemBean>>() {
                            }
                                    .getType();
                            shopList = gson.fromJson(message, type);
                            itemList.put("plant",shopList);
                            gridViewList.put("plant",plant_gridView);
                            if(shopList.size() != 0 && pet_list.size() != 0){
                                setViewPagerAdapter();
                            }
                        }else {
                            Toast.makeText(ShopActivity.this,"网络出了点问题",Toast.LENGTH_SHORT);
                        }
                        break;
                    case 2:
                        String pet_string = (String)msg.obj;
                        if(!pet_string.equals("") && msg.arg1 == 200) {
                            Type type = new TypeToken<List<Pet>>() {
                            }.getType();
                            pet_list = gson.fromJson(pet_string, type);
                            itemList.put("pet",pet_list);
                            gridViewList.put("pet",pet_gridView);
                            if(shopList.size() != 0 && pet_list.size() != 0){
                                setViewPagerAdapter();
                            }
                        }else {
                            Toast.makeText(ShopActivity.this,"网络出了点问题",Toast.LENGTH_SHORT);
                        }
                        break;
                }
            }
        };
        getShopingsItems();
        getAllPets();
        FullScreen.NavigationBarStatusBar(ShopActivity.this,true);
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
    }

    private void setViewPagerAdapter() {
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),itemList);
        viewPager.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText("植物"));
        tabLayout.addTab(tabLayout.newTab().setText("宠物"));
        tabLayout.setupWithViewPager(viewPager);
        adapter.notifyDataSetChanged();
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

        LinearLayout.LayoutParams params_gridview = new LinearLayout.LayoutParams((int)(displayWidth*0.7),(int)(displayHeight*0.7));
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
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        shopList = new ArrayList<>();
        pet_list = new ArrayList<>();
//        gridView = findViewById(R.id.gird_view);
        tabLayout = findViewById(R.id.tab_layout);
        imageView = findViewById(R.id.goBack);
        viewPager = findViewById(R.id.pager_view);
        pet_gridView = View.inflate(this,R.layout.pet_gridview_layout,null).findViewById(R.id.pet_gird_view);
        plant_gridView = View.inflate(this,R.layout.plant_gridview_layout,null).findViewById(R.id.plant_gird_view);
        gridViewList = new HashMap<>();
        itemList = new HashMap<>();
        wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
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
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/pet/showInStore").build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
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
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
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
}
