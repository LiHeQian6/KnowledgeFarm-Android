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
import com.li.knowledgefarm.entity.Pet;
import com.li.knowledgefarm.entity.ShopItemBean;

import java.util.List;

public class PetItemAdapter extends BaseAdapter {

    private Context context;
    private int id;
    private List<Pet> list;

    public PetItemAdapter(Context context, int id, List<Pet> list) {
        this.context = context;
        this.id = id;
        this.list = list;
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
            viewHolder.name = convertView.findViewById(R.id.flowerName);
            viewHolder.imageView = convertView.findViewById(R.id.flowerImage);
            viewHolder.price = convertView.findViewById(R.id.flowerPrice);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(list.get(position).getImg1()).apply(requestOptions).into(viewHolder.imageView);
        viewHolder.name.setText(list.get(position).getName());
        viewHolder.price.setText(list.get(position).getPrice() + "金币");
        if(!ShopActivity.mIsScroll) {
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
