package com.li.knowledgefarm.Shop;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShopActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private Gson gson;
    private List<ShopItemBean> shopList;
    private GridView gridView;
    private Handler messages;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        getViews();
        getShopingsItems();
        setStatusBar();

        messages = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                Type type = new TypeToken<List<ShopItemBean>>(){}
                        .getType();
                shopList = gson.fromJson(message,type);
                setAdapter();
            }
        };

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * @Description 绑定视图和数据
     * @Auther 孙建旺
     * @Date 下午 12:07 2019/12/07
     * @Param []
     * @return void
     */
    private void setAdapter() {
        ShopItemAdapter itemAdapter = new ShopItemAdapter(this,shopList,R.layout.shopitem_girdview);
        gridView.setAdapter(itemAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
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
        gridView = findViewById(R.id.gird_view);
        imageView = findViewById(R.id.goBack);
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
                        .url("http://"+getResources().getString(R.string.IP)+":8080/FarmKnowledge/crop/initCrop")
                        .build();
                Log.i("Ip","http://"+getResources().getString(R.string.IP)+":8080/FarmKnowledge/crop/initCrop");
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                        String shopItemMessages = response.body().string();
                        Message message = Message.obtain();
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
    /**
     * @Description 设置状态栏状态
     * @Auther 孙建旺
     * @Date 上午 9:57 2019/12/07
     * @Param []
     * @return void
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }
}
