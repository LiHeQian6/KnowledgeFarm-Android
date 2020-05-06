package com.li.knowledgefarm.Shop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.Pet;
import com.li.knowledgefarm.entity.PetVO;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PetItemPopUpWindow extends PopupWindow {
    private Context context;
    private PetVO pet;
    private TextView pet_name;//宠物名称
    private ImageView pet_image;//宠物图片
    private TextView pet_price;//宠物价格
    private TextView pet_description;//宠物描述
    private TextView pet_intelligence;//宠物智力值
    private TextView pet_life;//宠物生命值
    private TextView physical_value;//宠物体力值
    private Button cancel;//取消按钮
    private Button buy_pet;//购买按钮
    private OkHttpClient okHttpClient;
    private Toast toast;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String result = "网络出了点问题";
            if(msg.what == 200) {
                switch ((String) msg.obj) {
                    case "true":
                        result = "购买成功！";
                        break;
                    case "false":
                        result = "服务器开小差了";
                        break;
                    case "own":
                        result = "你已经有这个宠物啦！";
                        break;
                    case "notEnoughMoney":
                        result = "你的钱不够啦！";
                        break;
                }
            }
            toast = Toast.makeText(context,result,Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
            if(result.equals("true")){
                dismiss();
            }
        }
    };

    public PetItemPopUpWindow(Context context, PetVO pet) {
        super(context);
        this.context = context;
        this.pet = pet;
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.pet_pop_up,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
        showMessage();
    }

    @Override
    public void dismiss() {
        handler.removeCallbacksAndMessages(null);
        super.dismiss();
    }

    /**
     * @Description 展示宠物信息
     * @Author 孙建旺
     * @Date 下午4:02 2020/05/06
     * @Param []
     * @return void
     */
    private void showMessage(){
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.dog)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(pet.getImg1()).apply(requestOptions).into(pet_image);
        pet_name.setText(pet.getName());
        pet_price.setText("宠物价格:"+pet.getPrice()+"金币");
        pet_description.setText("    "+pet.getDescription());
        pet_intelligence.setText((int)(pet.getIntelligence()*0.7)+"-"+pet.getIntelligence());
        pet_life.setText((int)(pet.getLife()*0.7)+"-"+pet.getLife());
        physical_value.setText(pet.getPhysical()+"");
//        if(pet.getOwn() != 0){
//            buy_pet.setText("已拥有");
//            buy_pet.setClickable(false);
//        }
    }

    /**
     * @Description 获取控件ID
     * @Author 孙建旺
     * @Date 下午3:33 2020/05/06
     * @Param [contentView]
     * @return void
     */
    private void getViews(View contentView) {
        pet_name = contentView.findViewById(R.id.pet_name);
        pet_image = contentView.findViewById(R.id.this_pet_image);
        pet_price = contentView.findViewById(R.id.this_pet_price);
        pet_description = contentView.findViewById(R.id.this_pet_description);
        pet_intelligence = contentView.findViewById(R.id.pet_intelligence_value);
        pet_life = contentView.findViewById(R.id.pet_life_value);
        physical_value = contentView.findViewById(R.id.pet_physical_value);
        cancel = contentView.findViewById(R.id.pet_btnCancel);
        buy_pet = contentView.findViewById(R.id.buy_pet);
        okHttpClient = new OkHttpClient();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        buy_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyThisPet();
            }
        });
    }

    /**
     * @Description 购买宠物
     * @Author 孙建旺
     * @Date 下午4:47 2020/05/06
     * @Param []
     * @return void
     */
    private void BuyThisPet(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(context.getResources().getString(R.string.URL)+"/user/buyPet?userId="
                        + LoginActivity.user.getId() + "&petId=" + pet.getId()).build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        message.what = response.code();
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }
}
