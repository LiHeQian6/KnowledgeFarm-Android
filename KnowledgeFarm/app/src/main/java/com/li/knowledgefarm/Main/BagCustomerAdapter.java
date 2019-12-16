package com.li.knowledgefarm.Main;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.BagCropNumber;

import java.util.List;

/**
 * @auther 孙建旺
 * @description 背包信息自定义Adapter
 * @date 2019/12/08 下午 2:21
 */

public class BagCustomerAdapter extends BaseAdapter {

    private Context context;
    private List<BagCropNumber> dataList;
    private int resource;
    private int displayHeight;
    private int displayWidth;

    public BagCustomerAdapter(Context context, List<BagCropNumber> dataList, int resource) {
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
            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics ds = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(ds);
            displayHeight = ds.heightPixels;
            displayWidth = ds.widthPixels;
            LinearLayout.LayoutParams params_gridview = new LinearLayout.LayoutParams((int)(displayWidth*0.09), ViewGroup.LayoutParams.WRAP_CONTENT);
            params_gridview.gravity = Gravity.CENTER_HORIZONTAL;
            params_gridview.setMargins(0,(int)(displayHeight*0.05),0,0);
            convertView.setLayoutParams(params_gridview);
            viewHolder = new ViewHolder();
            viewHolder.flowerImg = convertView.findViewById(R.id.bag_flower_img);
            viewHolder.name = convertView.findViewById(R.id.bag_flower_name);
            viewHolder.number = convertView.findViewById(R.id.bag_flower_number);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(dataList.get(position).getCrop().getImg4()).apply(requestOptions).into(viewHolder.flowerImg);
        viewHolder.name.setText(dataList.get(position).getCrop().getName());
        viewHolder.number.setText("*"+dataList.get(position).getNumber());
        notifyDataSetChanged();
        return convertView;
    }

    private class ViewHolder{
        private ImageView flowerImg;
        private TextView name;
        private TextView number;
    }
}
