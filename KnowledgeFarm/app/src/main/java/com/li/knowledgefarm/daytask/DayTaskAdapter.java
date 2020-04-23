package com.li.knowledgefarm.daytask;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.Task;
import com.li.knowledgefarm.entity.TaskItem;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

class DayTaskAdapter extends BaseAdapter {
    private List<TaskItem> list;
    private int id;
    private Context context;

    public DayTaskAdapter( Context context, int id,List<TaskItem> list) {
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
        TaskItem taskItem = list.get(position);
        if (taskItem.getStatus()==1){
            viewHolder.todo.setText("领取奖励");
            viewHolder.todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }else if (taskItem.getStatus()==0){
            viewHolder.todo.setText("去完成");
            viewHolder.todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //关闭窗口
                }
            });
        }else if (taskItem.getStatus()==2){
            viewHolder.todo.setVisibility(View.GONE);
            viewHolder.done.setVisibility(View.VISIBLE);
        }
        if (taskItem.getType().equals("water")){
            viewHolder.icon.setImageResource(R.drawable.shuihu);
            taskItem.setContent("快去给你的植物浇一次水吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }else if (taskItem.getType().equals("signIn")){
            viewHolder.icon.setImageResource(R.drawable.daytask);
            taskItem.setContent("每日签到");
            taskItem.setReward("奖励：金币x100  经验x100");
            viewHolder.todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取奖励
                }
            });
            viewHolder.todo.setText("签到");
        }else if(taskItem.getType().equals("fertilize")){
            viewHolder.icon.setImageResource(R.drawable.huafei1);
            taskItem.setContent("快去给你的植物施一次肥吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }else if(taskItem.getType().equals("crop")){
            viewHolder.icon.setImageResource(R.drawable.shouhuo);
            taskItem.setContent("快去给你种植一株植物吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }else if(taskItem.getType().equals("harvest")){
            viewHolder.icon.setImageResource(R.drawable.shouhuo);
            taskItem.setContent("快去给你收获一株植物吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }else if(taskItem.getType().equals("helpWater")){
            viewHolder.icon.setImageResource(R.drawable.shouhuo);
            taskItem.setContent("快去帮助好友给一株植物浇水吧！");
            taskItem.setReward("奖励：金币x100  经验x100");
        }else if(taskItem.getType().equals("helpFertilize")){
            viewHolder.icon.setImageResource(R.drawable.shouhuo);
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
}
