package com.li.knowledgefarm.pet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.BagPetUtilItem;
import com.li.knowledgefarm.entity.UserPetHouse;
import com.li.knowledgefarm.view.HorizontalListView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
    private ListView pet_list;
    private Context context;
    private Gson gson;
    private Handler get_pet;
    private View contentView;
    private ImageView petImg;
    private TextView isUse;
    private TextView name;
    private TextView growth;
    private TextView description;
    private ProgressBar physical_bar;
    private TextView intelligence_value;
    private TextView physical_value;
    private TextView life_value;
    private Button use;
    private ArrayList<UserPetHouse> pets= new ArrayList<>();
    private Handler change_pet;
    private PetAdapter adapter;
    private int selectItem=0;//选中的宠物位置-1
    private OkHttpClient okHttpClient;
    private Handler get_pet_util;
    private HorizontalListView pet_utils_list;
    private List<BagPetUtilItem> pet_utils;
    private PetUtilAdapter util_adapter;
    private Handler use_util;

    public PetPopUpWindow(Context context) {
        super(context);
        this.context = context;
        Init();
        getPetInfo();
        getPetUtilInfo();
    }

    /**
     * @Author li
     * @param
     * @return void
     * @Description 初始化弹出框布局
     * @Date 10:52 2020/4/22
     **/
    private void Init(){
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                300, context.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                500, context.getResources().getDisplayMetrics());
        this.setHeight(height);
        this.setWidth(width);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        okHttpClient = OkHttpUtils.getInstance(context);
        contentView = LayoutInflater.from(context).inflate(R.layout.pet_layout,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setFocusable(false);
        super.showAtLocation(parent, gravity, x, y);
        final View view = getContentView();
        FullScreen.hideBottomUIMenu(view);
        setFocusable(true);
        update();
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
        pet_list=contentView.findViewById(R.id.pet_list);
        petImg = contentView.findViewById(R.id.pet_img);
        isUse = contentView.findViewById(R.id.is_use);
        name = contentView.findViewById(R.id.name);
        growth = contentView.findViewById(R.id.growth);
        description = contentView.findViewById(R.id.description);
        physical_bar = contentView.findViewById(R.id.physical_bar);
        intelligence_value = contentView.findViewById(R.id.intelligence_value);
        physical_value = contentView.findViewById(R.id.physical_value);
        life_value = contentView.findViewById(R.id.life_value);
        use = contentView.findViewById(R.id.use);
        pet_utils_list = contentView.findViewById(R.id.pet_utils);
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
                        .url(context.getResources().getString(R.string.URL)+"/userpethouse/showUserPetHouse").build();
                Call call = okHttpClient.newCall(request);
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
                        OkHttpUtils.unauthorized(response.code());
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
                    Type type = new TypeToken<List<UserPetHouse>>(){}.getType();
                    pets= gson.fromJson(message,type);
                    Collections.sort(pets);
                    if (pets.size()!=0){
                        final UserPetHouse pet = pets.get(0);
                        setPetDetail(pet);
                        adapter = new PetAdapter(context, R.layout.pet_item_layout, pets);
                        PetPopUpWindow.this.pet_list.setAdapter(adapter);
                        pet_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                setPetDetail(pets.get(i));
                                adapterView.getChildAt(selectItem).findViewById(R.id.border).setVisibility(View.INVISIBLE);
                                view.findViewById(R.id.border).setVisibility(View.VISIBLE);
                                selectItem=i;
                            }
                        });
                    }
                }else {
                    Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * @Author li
     * @param pet
     * @return void
     * @Description 设置宠物详细信息
     * @Date 21:48 2020/4/30
     **/
    private void setPetDetail(final UserPetHouse pet) {
        String url = pet.getGrowPeriod() == 0 ? pet.getPet().getImg1() : pet.getGrowPeriod()==1? pet.getPet().getImg2() : pet.getPet().getImg3();
        Glide.with(context).load(url).error(R.drawable.dog).into(petImg);
        if (pet.getIfUsing()== 1) {
            isUse.setVisibility(View.VISIBLE);
            use.setVisibility(View.GONE);
        } else {
            isUse.setVisibility(View.INVISIBLE);
            use.setVisibility(View.VISIBLE);
        }
        name.setText(pet.getPet().getName());
        description.setText(pet.getPet().getDescription());
        growth.setText(pet.getGrowPeriod() == 0 ? "幼年期" : pet.getGrowPeriod()==1? "成长期" : "成年期");
        description.setText(pet.getPet().getDescription());
        physical_bar.setMax(pet.getPet().getPhysical());
        physical_bar.setProgress(pet.getPhysical());
        intelligence_value.setText(""+pet.getIntelligence());
        physical_value.setText(pet.getPhysical()+"/"+pet.getPet().getPhysical());
        life_value.setText(""+pet.getLife());
        use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUserPetHouse(pet.getPet().getId());
            }
        });
//        feed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                feedPet(pet.getPet().getId());
//            }
//        });
    }

    /**
     * @Author li
     * @param petId
     * @return void
     * @Description 喂养宠物
     * @Date 12:22 2020/5/1
     **/
    private void feedPet(final int petId) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/pet/changePet?willUsingPetId="+petId).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("喂养宠物", "请求失败");
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        change_pet.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        change_pet.sendMessage(message);
                    }
                });
            }
        }.start();
        change_pet = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                if (!message.equals("Fail")){
                    if (message.equals("true")){

                    }else
                        Toast.makeText(context,"投喂失败！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * @Author li
     * @param petId
     * @return void
     * @Description 更换正在使用的宠物
     * @Date 21:49 2020/4/30
     **/
    private void changeUserPetHouse(final int petId){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/pet/changePet?willUsingPetId="+petId).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("更换宠物", "请求失败");
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        change_pet.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        change_pet.sendMessage(message);
                    }
                });
            }
        }.start();
        change_pet = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                if (!message.equals("Fail")){
                    if (message.equals("true")){
                        Toast.makeText(context,"更换成功！",Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < pets.size(); i++) {
                            if (pets.get(i).getPet().getId()==petId) {
                                pets.get(i).setIfUsing(1);
                            }else{
                                pets.get(i).setIfUsing(0);
                            }
                        }
                        selectItem=0;
                        isUse.setVisibility(View.VISIBLE);
                        use.setVisibility(View.GONE);
                        Collections.sort(pets);
                        adapter.notifyDataSetChanged();
                    }else
                        Toast.makeText(context,"更换失败！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * @Author li
     * @param
     * @return void
     * @Description
     * @Date 10:54 2020/5/14
     **/
    @SuppressLint("HandlerLeak")
    private void getPetUtilInfo(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/userpethouse/initUserPetUtilBag").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("宠物道具信息", "请求失败");
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        get_pet_util.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        get_pet_util.sendMessage(message);
                    }
                });
            }
        }.start();
        get_pet_util = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                Log.e("宠物道具信息",message);
                if (!message.equals("Fail")){
                    Type type = new TypeToken<List<BagPetUtilItem>>(){}.getType();
                    pet_utils = gson.fromJson(message,type);
                    Collections.sort(pet_utils);
                    if (pet_utils.size()!=0){
                        util_adapter = new PetUtilAdapter(context, R.layout.pet_util_item_layout, pet_utils);
                        PetPopUpWindow.this.pet_utils_list.setAdapter(util_adapter);
                        pet_utils_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @SuppressLint("RestrictedApi")
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                PopupMenu popupMenu = new PopupMenu(context, view, Gravity.TOP);
                                MenuInflater inflater = popupMenu.getMenuInflater();
                                inflater.inflate(R.menu.pet_util_menu, popupMenu.getMenu());
                                popupMenu.show();
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        userPetUtil(pets.get(selectItem).getId(),pet_utils.get(i).getId(),i);
                                        return false;
                                    }
                                });
                            }
                        });
                    }
                }else {
                    Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void userPetUtil(final int petId, final int petUtilId, final int i) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/user/feedPet?userPetHouseId="+petId+"&petUtilBagId="+petUtilId).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("使用道具", "请求失败");
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        use_util.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        use_util.sendMessage(message);
                    }
                });
            }
        }.start();
        use_util = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                if (!message.equals("Fail")){
                    if (message.equals("true")){
                        Toast.makeText(context,"使用成功！",Toast.LENGTH_SHORT).show();
                        BagPetUtilItem bagPetUtilItem = pet_utils.get(i);
                        bagPetUtilItem.setNumber(bagPetUtilItem.getNumber()-1);
                        pet_utils.remove(bagPetUtilItem);
                        pet_utils.add(bagPetUtilItem);
                        Collections.sort(pet_utils);
                        util_adapter.notifyDataSetChanged();
                        //TODO 更新展示的宠物的信息
                    }else
                        Toast.makeText(context,"使用失败！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
