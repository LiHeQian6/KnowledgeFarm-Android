package com.li.knowledgefarm.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
    private AlertDialog alertDialog;
    private ImageView imgBtnJian;
    private ImageView imgBtnPlus;
    private EditText shopNumber;

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
     * @Description 设置加减数量选择器
     * @Auther 孙建旺
     * @Date 下午 6:56 2019/12/07
     * @Param []
     * @return void
     */
    private void setShopNumber(){
        shopNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if(!shopNumber.getText().toString().equals("")) {
                    if (Integer.parseInt(shopNumber.getText().toString()) >= 1)
                        imgBtnJian.setImageDrawable(getDrawable(R.drawable.jian));
                    if (Integer.parseInt(shopNumber.getText().toString()) <= 999)
                        imgBtnPlus.setImageDrawable(getDrawable(R.drawable.plus));
                }
                if(!shopNumber.getText().toString().equals("")) {
                    if (Integer.parseInt(shopNumber.getText().toString()) <= 0)
                        shopNumber.setText(1+"");
                    if (Integer.parseInt(shopNumber.getText().toString()) > 999)
                        shopNumber.setText(999+"");
                }
            }
        });

        imgBtnJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!shopNumber.getText().toString().equals("")) {
                    if (Integer.parseInt(shopNumber.getText().toString()) >= 2) {
                        int num = Integer.parseInt(shopNumber.getText().toString()) - 1;
                        shopNumber.setText(num+"");
                    }
                }
            }
        });

        imgBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!shopNumber.getText().toString().equals("")) {
                    if (Integer.parseInt(shopNumber.getText().toString()) < 999) {
                        int num = Integer.parseInt(shopNumber.getText().toString()) + 1;
                        shopNumber.setText(num+"");
                    }
                }
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
                showSingleAlertDialog(position);
            }
        });
    }

    /**
     * @Description  点击商品弹出框
     * @Auther 孙建旺
     * @Date 下午 5:04 2019/12/07
     * @Param [position]
     * @return void
     */
    @SuppressLint("ResourceType")
    private void showSingleAlertDialog(int position){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.shop_dialog, null);
        Button button = layout.findViewById(R.id.buy);
        Button cancel = layout.findViewById(R.id.btnCancel);
        TextView thisName = layout.findViewById(R.id.thisName);
        TextView thisPrice = layout.findViewById(R.id.thisPrice);
        TextView thisTime = layout.findViewById(R.id.thisTime);
        imgBtnJian = layout.findViewById(R.id.imgBtnJian);
        imgBtnPlus = layout.findViewById(R.id.imgBtnPlus);
        shopNumber = layout.findViewById(R.id.shopNum);
        setShopNumber();
        shopNumber.setText("1");
        alertBuilder.setView(layout);
        thisName.setText("名称："+shopList.get(position).getName());
        thisPrice.setText("价格："+shopList.get(position).getPrice()+"");
        thisTime.setText("成熟时间："+shopList.get(position).getMatureTime()+"");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
        WindowManager.LayoutParams attrs = alertDialog.getWindow().getAttributes();
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int)(300*scale+0.5f);
        attrs.height =(int)(200*scale+0.5f);
        alertDialog.getWindow().setAttributes(attrs);
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
