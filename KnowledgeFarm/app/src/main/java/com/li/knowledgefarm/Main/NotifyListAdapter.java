package com.li.knowledgefarm.Main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.li.knowledgefarm.R;

import java.util.List;
import java.util.Map;

/**
 * @author 孙建旺
 * @description
 * @date 2020/04/16 下午3:49
 */

public class NotifyListAdapter extends BaseAdapter {

    private List<Map<String,String>> list;
    private int id;
    private Context context;

    public NotifyListAdapter(List<Map<String,String>> list, int id, Context context) {
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
            viewHolder.title = convertView.findViewById(R.id.notify_title);
            viewHolder.content = convertView.findViewById(R.id.notify_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(list.get(position).get("title"));
        viewHolder.content.setText(list.get(position).get("content"));
        return convertView;
    }

    private class ViewHolder{
        private TextView title;
        private TextView content;
    }
}
