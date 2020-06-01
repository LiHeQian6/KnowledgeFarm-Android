package com.li.knowledgefarm.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.Crop;
import com.li.knowledgefarm.entity.ShopItemBean;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlantItemPopUp extends PopupWindow {

    private Context context;
    private Crop shopItemBean;
    private OkHttpClient okHttpClient;
    private Button button;
    private Button cancel;
    private TextView thisName;
    private TextView thisPrice;
    private TextView thisTime;
    private ImageView thisFlower;
    private ImageView imgBtnJian;
    private ImageView imgBtnPlus;
    private EditText shopNumber;
    private Handler doAfterAdd;
    private TextView description;//植物描述

    public PlantItemPopUp(Context context) {
        this.context = context.getApplicationContext();
        this.setOutsideTouchable(false);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.shop_dialog,
                null, false);
        this.setContentView(contentView);
        okHttpClient = OkHttpUtils.getInstance(context);
        getViews(contentView);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        showMessage();
        setFocusable(false);
        super.showAtLocation(parent, gravity, x, y);
        final View view = getContentView();
        FullScreen.hideBottomUIMenu(view);
        setFocusable(true);
        update();
    }

    public void setShopItemBean(Crop shopItemBean) {
        this.shopItemBean = shopItemBean;
    }

    /**
     * @Description 填充展示信息
     * @Author 孙建旺
     * @Date 下午9:28 2020/05/24
     * @Param []
     * @return void
     */
    private void showMessage() {
        thisName.setText(shopItemBean.getName());
        thisPrice.setText("单价："+shopItemBean.getPrice()+"金币");
        thisTime.setText("成熟时间："+shopItemBean.getMatureTime()+"");
        description.setText("\u3000\u3000"+shopItemBean.getDescription());
        shopNumber.setText("1");
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.huancun2)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(shopItemBean.getImg4()).apply(requestOptions).into(thisFlower);
    }

    /**
     * @Description 获取控件Id
     * @Author 孙建旺
     * @Date 下午9:27 2020/05/24
     * @Param [contentView]
     * @return void
     */
    private void getViews(View contentView) {
        button = contentView.findViewById(R.id.buy);
        cancel = contentView.findViewById(R.id.btnCancel);
        thisName = contentView.findViewById(R.id.thisName);
        thisPrice = contentView.findViewById(R.id.thisPrice);
        thisTime = contentView.findViewById(R.id.thisTime);
        thisFlower = contentView.findViewById(R.id.thisFlower);
        imgBtnJian = contentView.findViewById(R.id.imgBtnJian);
        imgBtnPlus = contentView.findViewById(R.id.imgBtnPlus);
        shopNumber = contentView.findViewById(R.id.shopNum);
        description = contentView.findViewById(R.id.this_crop_description);

        registerListener();
    }

    private void registerListener() {
        imgBtnJian.setOnClickListener(new CustomerOnclickListener());
        imgBtnPlus.setOnClickListener(new CustomerOnclickListener());
        button.setOnClickListener(new CustomerOnclickListener());
        cancel.setOnClickListener(new CustomerOnclickListener());
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
                    if(Integer.parseInt(shopNumber.getText().toString()) == 1)
                        imgBtnJian.setImageDrawable(context.getDrawable(R.drawable.jianhui));
                    if (Integer.parseInt(shopNumber.getText().toString()) > 1)
                        imgBtnJian.setImageDrawable(context.getDrawable(R.drawable.jian));
                    if (Integer.parseInt(shopNumber.getText().toString()) <= 999)
                        imgBtnPlus.setImageDrawable(context.getDrawable(R.drawable.plus));
                }
                if(!shopNumber.getText().toString().equals("")) {
                    if (Integer.parseInt(shopNumber.getText().toString()) <= 0)
                        shopNumber.setText(1+"");
                    if (Integer.parseInt(shopNumber.getText().toString()) > 999)
                        shopNumber.setText(999+"");
                }
            }
        });
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
                Request request = new Request.Builder().url(context.getResources().getString(R.string.URL)+"/user/buyCrop?cropId="+cropId+"&number="+num).build();
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
                        OkHttpUtils.unauthorized(response.code());
                        String addCallback = response.body().string();
                        Message message = Message.obtain();
                        message.obj = addCallback;
                        doAfterAdd.sendMessage(message);
                    }
                });
            }
        }.start();
        doAfterAdd = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String addCallBack = (String)msg.obj;
                if(addCallBack.equals("true")){
                    int newMoney = UserUtil.getUser().getMoney() - Integer.parseInt(shopNumber.getText().toString().trim())*shopItemBean.getPrice();
                    UserUtil.getUser().setMoney(newMoney);
                    CustomerToast.getInstance(context,"购买成功！", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else if(addCallBack.equals("notEnoughMoney")){
                    CustomerToast.getInstance(context,"你的钱不够了哦！", Toast.LENGTH_SHORT).show();
                }else{
                    CustomerToast.getInstance(context,"购买失败！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    private class CustomerOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imgBtnJian:
                    if(!shopNumber.getText().toString().equals("")) {
                        if (Integer.parseInt(shopNumber.getText().toString()) >= 2) {
                            int num = Integer.parseInt(shopNumber.getText().toString()) - 1;
                            shopNumber.setText(num+"");
                        }
                    }
                    break;
                case R.id.imgBtnPlus:
                    if(!shopNumber.getText().toString().equals("")) {
                        if (Integer.parseInt(shopNumber.getText().toString()) < 999) {
                            int num = Integer.parseInt(shopNumber.getText().toString()) + 1;
                            shopNumber.setText(num+"");
                        }
                    }
                    break;
                case R.id.btnCancel:
                    dismiss();
                    break;
                case R.id.buy:
                    if(shopNumber.getText().toString().equals("")){
                        Toast.makeText(context,"购买数量不能为空哦！",Toast.LENGTH_SHORT).show();
                    }else{
                        buyFlowers(
                                UserUtil.getUser().getId(),
                                shopItemBean.getId(),
                                Integer.parseInt(shopNumber.getText().toString().trim())
                        );
                    }
                    break;
            }
        }
    }
}
