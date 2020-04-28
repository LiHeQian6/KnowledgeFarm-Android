package com.li.knowledgefarm.pet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.Pet;
import com.li.knowledgefarm.entity.Task;
import com.li.knowledgefarm.entity.TaskItem;
import com.li.knowledgefarm.entity.User;
import com.li.knowledgefarm.view.HorizontalListView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author li
 * @Description
 * @Date 10:49 2020/4/22
 **/
public class PetPopUpWindow extends PopupWindow {
    private HorizontalListView pet;
    private Context context;
    private Gson gson;
    private Handler get_pet;
    private View contentView;
    private ArrayList<Pet> pets= new ArrayList<>();

    public PetPopUpWindow(Context context) {
        super(context);
        this.context = context;
        Init();
        getPetInfo();
    }

    /**
     * @Author li
     * @param
     * @return void
     * @Description 初始化弹出框布局
     * @Date 10:52 2020/4/22
     **/
    private void Init(){
        this.setHeight(900);
        this.setWidth(1600);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        contentView = LayoutInflater.from(context).inflate(R.layout.pet_layout,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
    }

    /**
     * @Author li
     * @param view
     * @return void
     * @Description 获取控件
     * @Date 10:58 2020/4/22
     **/
    private void getViews(View view){
        gson = new Gson();
        pet=contentView.findViewById(R.id.pet);
    }

    /**
     * @Author li
     * @return void
     * @Description 获取用户的宠物的信息
     * @Date 10:53 2020/4/22
     **/
    private void getPetInfo(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/task/getTask2?userId="+LoginActivity.user.getId()).build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("宠物信息", "请求失败");
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        get_pet.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        get_pet.sendMessage(message);
                    }
                });
            }
        }.start();
        get_pet = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                Log.e("宠物信息",message);
                if (!message.equals("Fail")){
                    Type type = new TypeToken<List<Pet>>(){}.getType();
//                    pets= gson.fromJson(message,type);
                    Pet pet = new Pet("二哈","狗中贵族",100,200,100);
                    Pet pet2 = new Pet("二哈","狗中贵族",100,200,100);
                    Pet pet3 = new Pet("二哈","狗中贵族",100,200,100);
                    pets.add(pet);
                    pets.add(pet2);
                    pets.add(pet3);
                    PetPopUpWindow.this.pet.setAdapter(new PetAdapter(context,R.layout.pet_item_layout,pets));
                }else {
                    Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


}
