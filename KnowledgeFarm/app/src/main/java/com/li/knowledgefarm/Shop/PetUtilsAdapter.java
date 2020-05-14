package com.li.knowledgefarm.Shop;

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
import com.li.knowledgefarm.entity.PetUtil;

import java.util.List;

public class PetUtilsAdapter extends BaseAdapter {

    private List<PetUtil> petUtils;
    private Context context;
    private int id;

    public PetUtilsAdapter(List<PetUtil> petUtils, Context context, int id) {
        this.petUtils = petUtils;
        this.context = context;
        this.id = id;
    }

    @Override
    public int getCount() {
        return petUtils.size();
    }

    @Override
    public Object getItem(int position) {
        return petUtils.get(position);
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
            viewHolder.name = convertView.findViewById(R.id.flowerName);
            viewHolder.imageView = convertView.findViewById(R.id.flowerImage);
            viewHolder.price = convertView.findViewById(R.id.flowerPrice);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.dog)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(petUtils.get(position).getImg()).apply(requestOptions).into(viewHolder.imageView);
        viewHolder.name.setText(petUtils.get(position).getName());
        viewHolder.price.setText(petUtils.get(position).getPrice().toString()+"金币");
        if(!MyFragment.mIsScroll) {
            notifyDataSetChanged();
        }
        return convertView;
    }

    private class ViewHolder{
        TextView name;
        ImageView imageView;
        TextView price;
    }
}
