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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    private ImageView learn;
    private ImageView water;
    private ImageView fertilizer;
    private ImageView bag;
    private ImageView shop;
    private ImageView pet;
    private ImageView setting;
    private ImageView photo;
    private TextView nickName;
    private TextView level;
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

        bagMessagesHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String messages = (String)msg.obj;
                if(!messages.equals("Fail")){
                    Type type = new TypeToken<List<BagMessagesBean>>(){}.getType();
                    dataList = gson.fromJson(messages,type);
                    showSingleAlertDialog();
                }else{
                    Toast toast = Toast.makeText(MainActivity.this,"获取数据失败！",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
    }

    private void showLand() {
//        ImageView land1=findViewById(R.id.land1);
//        ImageView land2=findViewById(R.id.land2);
//        land2.setDrawingCacheEnabled(true);
//        land2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("点击了",""+motionEvent.getAction());
//                Log.e("点击了",""+(int) motionEvent.getY());
//                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
//                    Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
//                    int color = bmp.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());
//                    if (color == Color.TRANSPARENT){
//                        Log.e("点击了","land2");
//                        return false;
//                    }
//
//                    else {
//                        Log.e("点击了","land2");
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//        land1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
//                    Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
//                    int color = bmp.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());
//                    if (color == Color.TRANSPARENT)
//                        return false;
//                    else {
//                        Log.e("点击了","land1");
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
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
        fertilizer=findViewById(R.id.fertilizer);
        bag=findViewById(R.id.bag);
        shop=findViewById(R.id.shop);
        pet=findViewById(R.id.pet);
        setting=findViewById(R.id.setting);
        photo=findViewById(R.id.photo);
        nickName=findViewById(R.id.nickName);
        level=findViewById(R.id.level);
        money=findViewById(R.id.money);
        //lands=findViewById(R.id.lands);
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
                    getBagMessages();
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this,R.style.dialog_soft_input);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.bag_girdview, null);
        GridView gridView = layout.findViewById(R.id.bag_grid_view);
        BagCustomerAdapter customerAdapter = new BagCustomerAdapter(alertBuilder.getContext(),dataList,R.layout.gird_adapteritem);
        gridView.setAdapter(customerAdapter);
        alertBuilder.setView(layout);
        bagDialog = alertBuilder.create();
        bagDialog.show();
        WindowManager.LayoutParams attrs = bagDialog.getWindow().getAttributes();
        attrs.gravity = Gravity.RIGHT;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int)(300*scale+0.5f);
        attrs.height =(int)(300*scale+0.5f);
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
