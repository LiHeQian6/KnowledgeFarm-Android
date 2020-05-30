package com.li.knowledgefarm.daytask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.DoTaskBean;
import com.li.knowledgefarm.entity.EventBean;
import com.li.knowledgefarm.entity.Task;
import com.li.knowledgefarm.entity.TaskItem;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class DayTaskAdapter extends BaseAdapter {
    private final OkHttpClient okHttpClient;
    private List<TaskItem> list;
    private int id;
    private Context context;
    private Handler get_reward;

    public DayTaskAdapter( Context context, int id,List<TaskItem> list) {
        this.list = list;
        this.id = id;
        this.context = context;
        okHttpClient = OkHttpUtils.getInstance(context);
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
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context,id,null);
            viewHolder = new ViewHolder();
            viewHolder.reward = convertView.findViewById(R.id.reward);
            viewHolder.content = convertView.findViewById(R.id.content);
            viewHolder.done = convertView.findViewById(R.id.done);
            viewHolder.todo = convertView.findViewById(R.id.todo);
            viewHolder.icon = convertView.findViewById(R.id.icon);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final TaskItem taskItem = list.get(position);
        if (taskItem.getStatus()==1){
            viewHolder.todo.setVisibility(View.VISIBLE);
            viewHolder.done.setVisibility(View.GONE);
            viewHolder.todo.setText("领取奖励");
            viewHolder.todo.setBackgroundResource(R.drawable.button);
            viewHolder.todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getReward(taskItem.getType());
                }
            });
        }else if (taskItem.getStatus()==0){
            viewHolder.todo.setBackgroundResource(R.drawable.button);
            viewHolder.todo.setVisibility(View.VISIBLE);
            viewHolder.done.setVisibility(View.GONE);
            viewHolder.todo.setText("去完成");
            viewHolder.todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DoTaskBean event = new DoTaskBean();
                    if (taskItem.getType().substring(0,4).equals("help")) {
                        event.setToFriend(true);
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }else if (taskItem.getStatus()==2){
            viewHolder.todo.setVisibility(View.GONE);
            viewHolder.done.setVisibility(View.VISIBLE);
        }
        if (taskItem.getType().equals("signIn")){
            viewHolder.icon.setImageResource(R.drawable.daytask);
            taskItem.setContent("每日签到");
            taskItem.setReward("奖励：金币x100  经验x100");
            viewHolder.todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getReward(taskItem.getType());
                }
            });
            viewHolder.todo.setText("签到");
        }else if (taskItem.getType().equals("water")){
            viewHolder.icon.setImageResource(R.drawable.shuihu);
            taskItem.setContent("快去给你的植物浇一次水吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }else if(taskItem.getType().equals("fertilize")){
            viewHolder.icon.setImageResource(R.drawable.huafei1);
            taskItem.setContent("快去给你的植物施一次肥吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }else if(taskItem.getType().equals("crop")){
            viewHolder.icon.setImageResource(R.drawable.seed);
            taskItem.setContent("快去种植一株植物吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }else if(taskItem.getType().equals("harvest")){
            viewHolder.icon.setImageResource(R.drawable.shouhuo);
            taskItem.setContent("快去收获一株植物吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }else if(taskItem.getType().equals("helpWater")){
            viewHolder.icon.setImageResource(R.drawable.shuihu);
            taskItem.setContent("快去帮助好友给一株植物浇水吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }else if(taskItem.getType().equals("helpFertilize")){
            viewHolder.icon.setImageResource(R.drawable.huafei1);
            taskItem.setContent("快去帮助好友给一株植物施肥吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }
        viewHolder.reward.setText(taskItem.getReward());
        viewHolder.content.setText(taskItem.getContent());
        return convertView;
    }

    private class ViewHolder{
        private TextView content;
        private TextView reward;
        private TextView done;
        private ImageView icon;
        private Button todo;
    }

    /**
     * @Author li
     * @param taskName
     * @return void
     * @Description 获取奖励
     * @Date 20:54 2020/4/23
     **/
    private void getReward(final String taskName){
        new Thread(){
            @Override
            public void run() {
                super.run();
                String name = HumpToUnderline(taskName);
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/task/getReward2?taskName="+ name).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("获取奖励信息", "请求失败");
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        get_reward.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        get_reward.sendMessage(message);
                    }
                });
            }
        }.start();
        get_reward = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                if (!message.equals("Fail")){
                    if (message.equals("2")){
                        CustomerToast.getInstance(context,"领取成功！",Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getType().equals(taskName)){
                                list.get(i).setStatus(2);
                                Collections.sort(list);
                            }
                        }
                        notifyDataSetChanged();
                    }else
                        CustomerToast.getInstance(context,"领取失败！",Toast.LENGTH_SHORT).show();
                }else {
                    CustomerToast.getInstance(context, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public String HumpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        if (!para.contains("_")) {
            for(int i=0;i<para.length();i++){
                if(Character.isUpperCase(para.charAt(i))){
                    sb.insert(i+temp, "_");
                    temp+=1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }

}

