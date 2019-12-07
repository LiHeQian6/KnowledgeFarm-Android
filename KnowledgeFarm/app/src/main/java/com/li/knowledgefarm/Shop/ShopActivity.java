package com.li.knowledgefarm.Shop;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.li.knowledgefarm.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShopActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        okHttpClient = new OkHttpClient();
        setStatusBar();
        new Thread(){
            @Override
            public void run() {
                super.run();
                getShopingsItems();
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
        Request request = new Request.Builder()
                .url("http://"+getResources().getString(R.string.IP)+":8080/FarmKnowledge/crop/initCrop")
                .build();
        Log.i("Ip","http://"+getResources().getString(R.string.IP)+":8080/FarmKnowledge/crop/initCrop");
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                String shopItemMessages = response.body().string();
                Log.i("shop",shopItemMessages);
            }
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                Log.i("error","请求失败！");
            }
        });
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
