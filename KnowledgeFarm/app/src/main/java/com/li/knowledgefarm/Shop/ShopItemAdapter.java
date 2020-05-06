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

/**
 * @auther 孙建旺
 * @description 商店商品Adapter
 * @date 2019/12/07 上午 10:33
 */
public class ShopItemAdapter extends BaseAdapter {

    private Context context;
    private List<ShopItemBean> list;
    private int resource;

    public ShopItemAdapter(Context context, List<ShopItemBean> list, int resource) {
        this.context = context;
        this.list = list;
        this.resource = resource;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, resource, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.flowerName);
            holder.imageView = convertView.findViewById(R.id.flowerImage);
            holder.price = convertView.findViewById(R.id.flowerPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.loading)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(context).load(list.get(position).getImg4()).apply(requestOptions).into(holder.imageView);
            ShopItemBean bean = list.get(position);
            holder.name.setText(bean.getName());
            holder.price.setText(bean.getPrice() + "金币");
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
