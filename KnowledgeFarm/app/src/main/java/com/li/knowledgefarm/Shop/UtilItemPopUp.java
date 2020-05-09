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
import android.view.Gravity;
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
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.PetUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UtilItemPopUp extends PopupWindow {

    private Context context;
    private PetUtil petUtil;
    private ImageView util_imgBtnJian; //数量减
    private ImageView util_imgBtnPlus; //数量加
    private EditText util_Number; //数量
    private TextView util_name; //道具名称
    private ImageView util_image; //道具图片
    private TextView util_price;//道具价格
    private TextView util_type;//道具类型
    private TextView util_description;//道具描述
    private Button cancel; //取消按钮
    private Button buy_util; //购买
    private Toast toast;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String result = (String)msg.obj;
            switch (result){
                case "true":
                    result = "购买成功!";
                    break;
                case "false":
                    result = "服务器开小差了";
                    break;
                case "notEnoughMoney":
                    result = "你的钱不够了哦";
                    break;
            }
            toast = Toast.makeText(context,result,Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
            if(result.equals("购买成功!")){
                dismiss();
            }
        }
    };

    public UtilItemPopUp(Context context, PetUtil petUtil) {
        super(context);
        this.context = context;
        this.petUtil = petUtil;
        this.setOutsideTouchable(false);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.util_pop_up,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
        showMessage();
        setShopNumber();
    }

    /**
     * @Description 展示信息
     * @Author 孙建旺
     * @Date 上午10:41 2020/05/09
     * @Param []
     * @return void
     */
    private void showMessage() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.meat)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(petUtil.getImg()).apply(requestOptions).into(util_image);
        util_name.setText(petUtil.getName());
        util_price.setText("价格："+petUtil.getPrice()+"");
        switch (petUtil.getPetUtilType().getId()){
            case 1:
                util_type.setText("作用：恢复生命值"+petUtil.getValue()+"点");
                break;
            case 2:
                util_type.setText("作用：恢复体力"+petUtil.getValue()+"点");
                break;
        }
        util_description.setText("    "+petUtil.getDescription());
    }

    /**
     * @Description 获取控件Id
     * @Author 孙建旺
     * @Date 上午10:41 2020/05/09
     * @Param [contentView]
     * @return void
     */
    private void getViews(View contentView) {
        util_image = contentView.findViewById(R.id.this_util_image);
        util_name = contentView.findViewById(R.id.util_name);
        util_price = contentView.findViewById(R.id.this_util_price);
        util_type = contentView.findViewById(R.id.this_util_type);
        util_description = contentView.findViewById(R.id.this_util_description);
        util_imgBtnJian = contentView.findViewById(R.id.util_imgBtnJian);
        util_imgBtnPlus = contentView.findViewById(R.id.util_imgBtnPlus);
        util_Number = contentView.findViewById(R.id.util_Num);
        cancel = contentView.findViewById(R.id.util_btnCancel);
        buy_util = contentView.findViewById(R.id.buy_util);
        util_imgBtnJian.setOnClickListener(new CustomerOnclickListener());
        util_imgBtnPlus.setOnClickListener(new CustomerOnclickListener());
        cancel.setOnClickListener(new CustomerOnclickListener());
        buy_util.setOnClickListener(new CustomerOnclickListener());
    }

    /**
     * @Description 购买道具
     * @Author 孙建旺
     * @Date 上午11:47 2020/05/09
     * @Param []
     * @return void
     */
    private void BuyThisUtil(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(context.getResources().getString(R.string.URL)+"/user/buyPetUtil?number="
                        +util_Number.getText().toString().trim()
                        +"&userId="+ LoginActivity.user.getId()
                        +"&petUtilId="+ petUtil.getId()).build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        message.arg1 = response.code();
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * @Description 设置加减数量选择器
     * @Auther 孙建旺
     * @Date 下午 6:56 2019/12/07
     * @Param []
     * @return void
     */
    private void setShopNumber(){
        util_Number.addTextChangedListener(new TextWatcher() {
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
                if(!util_Number.getText().toString().equals("")) {
                    if(Integer.parseInt(util_Number.getText().toString()) == 1)
                        util_imgBtnJian.setImageDrawable(context.getDrawable(R.drawable.jianhui));
                    if (Integer.parseInt(util_Number.getText().toString()) > 1)
                        util_imgBtnJian.setImageDrawable(context.getDrawable(R.drawable.jian));
                    if (Integer.parseInt(util_Number.getText().toString()) <= 999)
                        util_imgBtnPlus.setImageDrawable(context.getDrawable(R.drawable.plus));
                }
                if(!util_Number.getText().toString().equals("")) {
                    if (Integer.parseInt(util_Number.getText().toString()) <= 0)
                        util_Number.setText(1+"");
                    if (Integer.parseInt(util_Number.getText().toString()) > 999)
                        util_Number.setText(999+"");
                }
            }
        });
    }

    private class CustomerOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.util_imgBtnJian:
                    if(!util_Number.getText().toString().equals("")) {
                        if (Integer.parseInt(util_Number.getText().toString()) >= 2) {
                            int num = Integer.parseInt(util_Number.getText().toString()) - 1;
                            util_Number.setText(num+"");
                        }
                    }
                    break;
                case R.id.util_imgBtnPlus:
                    if(!util_Number.getText().toString().equals("")) {
                        if (Integer.parseInt(util_Number.getText().toString()) < 999) {
                            int num = Integer.parseInt(util_Number.getText().toString()) + 1;
                            util_Number.setText(num+"");
                        }
                    }
                    break;
                case R.id.util_btnCancel:
                    dismiss();
                    break;
                case R.id.buy_util:
                    BuyThisUtil();
                    break;
            }
        }
    }
}
