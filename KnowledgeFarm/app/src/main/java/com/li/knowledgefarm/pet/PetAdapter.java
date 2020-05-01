package com.li.knowledgefarm.pet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.EventBean;
import com.li.knowledgefarm.entity.UserPetHouse;

import java.util.ArrayList;
import java.util.List;
import android.widget.BaseAdapter;


public class PetAdapter extends BaseAdapter {
    private List<UserPetHouse> list;
    private int id;
    private Context context;
    private ImageView photo;

    public PetAdapter(Context context, int id, List<UserPetHouse> list) {
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
        if(convertView == null) {
            convertView = View.inflate(context, id, null);
        }
        photo= convertView.findViewById(R.id.photo);
        ImageView border=convertView.findViewById(R.id.border);
        if (position==0){
            border.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(list.get(position).getPet().getImg1()).apply(RequestOptions.bitmapTransform(new CircleCrop())).error(R.drawable.dog).into(photo);
        return convertView;
    }

}