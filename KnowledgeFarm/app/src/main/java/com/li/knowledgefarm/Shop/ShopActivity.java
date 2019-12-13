package com.li.knowledgefarm.Shop;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.Login.StartActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.ShopItemBean;

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
    private AlertDialog alertDialog = null;
    private ImageView imgBtnJian;
    private ImageView imgBtnPlus;
    private EditText shopNumber;
    private Handler doAfterAdd;
    private long lastClickTime = 0L;
    // 两次点击间隔不能少于1000ms
    private static final int FAST_CLICK_DELAY_TIME = 1000;
    public static Boolean mIsScroll = false;
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
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                showSingleAlertDialog(position);
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    mIsScroll = false;
                }else{
                    mIsScroll = true;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
    private void showSingleAlertDialog(final int position){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this,R.style.dialog_soft_input);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.shop_dialog, null);
        Button button = layout.findViewById(R.id.buy);
        Button cancel = layout.findViewById(R.id.btnCancel);
        TextView thisName = layout.findViewById(R.id.thisName);
        TextView thisPrice = layout.findViewById(R.id.thisPrice);
        TextView thisTime = layout.findViewById(R.id.thisTime);
        ImageView thisFlower = layout.findViewById(R.id.thisFlower);
        imgBtnJian = layout.findViewById(R.id.imgBtnJian);
        imgBtnPlus = layout.findViewById(R.id.imgBtnPlus);
        shopNumber = layout.findViewById(R.id.shopNum);
        setShopNumber();
        shopNumber.setText("1");
        alertBuilder.setView(layout);
        thisName.setText("名称："+shopList.get(position).getName());
        thisPrice.setText("单价："+shopList.get(position).getPrice()+"");
        thisTime.setText("成熟时间："+shopList.get(position).getMatureTime()+"");
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.huancun2)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(alertBuilder.getContext()).load(shopList.get(position).getImg3()).apply(requestOptions).into(thisFlower);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyFlowers(
                        LoginActivity.user.getId(),
                        shopList.get(position).getId(),
                        Integer.parseInt(shopNumber.getText().toString().trim())
                );
            }
        });
        doAfterAdd = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String addCallBack = (String)msg.obj;
                if(addCallBack.equals("true")){
                    int newMoney = LoginActivity.user.getMoney() - Integer.parseInt(shopNumber.getText().toString().trim())*shopList.get(position).getPrice();
                    LoginActivity.user.setMoney(newMoney);
                    Toast toast = Toast.makeText(alertBuilder.getContext(),"购买成功！",Toast.LENGTH_SHORT);
                    toast.show();
                    alertDialog.dismiss();
                }else if(addCallBack.equals("notEnoughMoney")){
                    Toast toast = Toast.makeText(alertBuilder.getContext(),"你的钱不够了哦！",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(alertBuilder.getContext(),"添加失败！",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
        WindowManager.LayoutParams attrs = alertDialog.getWindow().getAttributes();
        attrs.gravity = Gravity.CENTER;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int)(300*scale+0.5f);
        attrs.height =(int)(300*scale+0.5f);
        Window dialogWindow = alertDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setAttributes(attrs);
    }

    /**
     * @Description 上传购买种子信息
     * @Auther 孙建旺
     * @Date 下午 3:08 2019/12/09
     * @Param []
     * @return void
     */
    private void buyFlowers(final int userID, final int cropId, final int num){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/user/buyCrop?userId="+userID+"&cropId="+cropId+"&number="+num).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        String addCallback = "Fail";
                        Message message = Message.obtain();
                        message.obj = addCallback;
                        doAfterAdd.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String addCallback = response.body().string();
                        Message message = Message.obtain();
                        message.obj = addCallback;
                        doAfterAdd.sendMessage(message);
                    }
                });
            }
        }.start();
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
                        .url(getResources().getString(R.string.URL)+"/crop/initCrop")
                        .build();
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
