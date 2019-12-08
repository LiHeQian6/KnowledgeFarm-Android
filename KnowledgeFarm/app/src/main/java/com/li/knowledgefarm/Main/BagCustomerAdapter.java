package com.li.knowledgefarm.Main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.R;

import java.util.List;

/**
 * @auther 孙建旺
 * @description 背包信息自定义Adapter
 * @date 2019/12/08 下午 2:21
 */

public class BagCustomerAdapter extends BaseAdapter {

    private Context context;
    private List<BagMessagesBean> dataList;
    private int resource;

    public BagCustomerAdapter(Context context, List<BagMessagesBean> dataList, int resource) {
        this.context = context;
        this.dataList = dataList;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context,resource,null);
            viewHolder = new ViewHolder();
            viewHolder.flowerImg = convertView.findViewById(R.id.bag_flower_img);
            viewHolder.name = convertView.findViewById(R.id.bag_flower_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(dataList.get(position).getImg1()).apply(requestOptions).into(viewHolder.flowerImg);
        viewHolder.name.setText(dataList.get(position).getName());
        notifyDataSetChanged();
        return convertView;
    }

    private class ViewHolder{
        private ImageView flowerImg;
        private TextView name;
    }
}
