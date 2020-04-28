package com.li.knowledgefarm.pet;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.DoTaskBean;
import com.li.knowledgefarm.entity.Pet;
import com.li.knowledgefarm.entity.TaskItem;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class PetAdapter extends BaseAdapter {
    private List<Pet> list;
    private int id;
    private Context context;
    private Handler change_pet;
    private ViewHolder viewHolder;

    public PetAdapter(Context context, int id, List<Pet> list) {
        this.list = list;
        this.id = id;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context,id,null);
            viewHolder = new ViewHolder();
            viewHolder.petImg = convertView.findViewById(R.id.pet_img);
            viewHolder.isUse = convertView.findViewById(R.id.is_use);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.growth = convertView.findViewById(R.id.growth);
            viewHolder.description = convertView.findViewById(R.id.description);
            viewHolder.intelligence_bar = convertView.findViewById(R.id.intelligence_bar);
            viewHolder.physical_bar = convertView.findViewById(R.id.physical_bar);
            viewHolder.life_bar = convertView.findViewById(R.id.life_bar);
            viewHolder.intelligence_value = convertView.findViewById(R.id.intelligence_value);
            viewHolder.physical_value = convertView.findViewById(R.id.physical_value);
            viewHolder.life_value = convertView.findViewById(R.id.life_value);
            viewHolder.use = convertView.findViewById(R.id.use);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Pet pet = list.get(position);
        String url = pet.getGrowPeriod() == 0 ? pet.getImg1() : pet.getGrowPeriod()==1? pet.getImg2() : pet.getImg3();
//        Glide.with(context).load(url).into(viewHolder.petImg);
//        pet.getUse()?viewHolder.isUse.setVisibility(View.VISIBLE):viewHolder.isUse.setVisibility(View.GONE);
        viewHolder.name.setText(pet.getName());
        viewHolder.description.setText(pet.getDescription());
        viewHolder.growth.setText(pet.getGrowPeriod() == 0 ? "幼年期" : pet.getGrowPeriod()==1? "成长期" : "成年期");
        viewHolder.description.setText(pet.getDescription());
        viewHolder.intelligence_bar.setMax(pet.getIntelligence());
        viewHolder.physical_bar.setMax(pet.getPhysical());
        viewHolder.life_bar.setMax(pet.getLife());
//        viewHolder.intelligence_bar.setProgress(pet.getMyIntelligence());
//        viewHolder.physical_bar.setProgress(pet.getMyPhysical());
//        viewHolder.life_bar.setProgress(pet.getMyLife());
//        viewHolder.intelligence_value.setText(pet.getMyIntelligence()+"/"+pet.getIntelligence());
//        viewHolder.physical_value.setText(pet.getMyPhysical()+"/"+pet.getPhysical());
//        viewHolder.life_value.setText(pet.getMyLife()+"/"+pet.getLife());
//        if (pet.getUse()){
//            viewHolder.use.setVisibility(View.GONE);
//        }
        viewHolder.use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePet(pet.getId());
            }
        });
        return convertView;
    }

    private class ViewHolder{
        private ImageView petImg;
        private TextView isUse;
        private TextView name;
        private TextView growth;
        private TextView description;
        private ProgressBar intelligence_bar;
        private ProgressBar physical_bar;
        private ProgressBar life_bar;
        private TextView intelligence_value;
        private TextView physical_value;
        private TextView life_value;
        private Button use;
    }


    private void changePet(final int petId){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/pet/usePet?petId="+petId+"&userId="+LoginActivity.user.getId()).build();
                Call call = new OkHttpClient().newCall(request);
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
                        //TODO 把原来展示的‘正在使用’标隐藏和‘使用’按钮显示，把切换到的‘正在使用’显示和‘使用’按钮隐藏
                        notifyDataSetChanged();
                    }else
                        Toast.makeText(context,"更换失败！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}

