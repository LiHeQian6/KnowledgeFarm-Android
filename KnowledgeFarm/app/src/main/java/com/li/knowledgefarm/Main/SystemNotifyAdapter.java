package com.li.knowledgefarm.Main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.Notification;

import java.util.List;
import java.util.Map;

/**
 * @author 孙建旺
 * @description
 * @date 2020/04/16 下午3:49
 */

public class SystemNotifyAdapter extends BaseAdapter {

    private FriendsPage<Notification> list;
    private int id;
    private Context context;

    public SystemNotifyAdapter(FriendsPage<Notification> list, int id, Context context) {
        this.list = list;
        this.id = id;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return list.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SystemViewHolder systemViewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context,id,null);
            systemViewHolder = new SystemViewHolder();
            systemViewHolder.title = convertView.findViewById(R.id.notify_title);
            systemViewHolder.content = convertView.findViewById(R.id.notify_content);
            systemViewHolder.time = convertView.findViewById(R.id.notify_time);
            convertView.setTag(systemViewHolder);
        }else {
            systemViewHolder = (SystemViewHolder) convertView.getTag();
        }
        systemViewHolder.title.setText(list.getList().get(position).getTitle());
        systemViewHolder.content.setText(list.getList().get(position).getContent());
        systemViewHolder.time.setText(list.getList().get(position).getCreateTime().toString());
        return convertView;
    }

    private class SystemViewHolder{
        private TextView title;
        private TextView content;
        private TextView time;
    }
}
