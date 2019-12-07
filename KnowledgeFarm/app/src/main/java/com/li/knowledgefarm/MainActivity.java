package com.li.knowledgefarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.knowledgefarm.Settings.SettingActivity;
import com.li.knowledgefarm.Shop.ShopActivity;
import com.li.knowledgefarm.Study.SubjectListActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();
        getViews();
        addListener();
        showLand();
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
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }
}
