package com.li.knowledgefarm.pet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.BagPetUtilItem;
import com.li.knowledgefarm.entity.UserPetHouse;

import java.util.List;


public class PetUtilAdapter extends BaseAdapter {
    private List<BagPetUtilItem> list;
    private int id;
    private Context context;
    private ImageView photo;
    private TextView num;
    private TextView name;

    public PetUtilAdapter(Context context, int id, List<BagPetUtilItem> list) {
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
        num=convertView.findViewById(R.id.num);
        name=convertView.findViewById(R.id.name);
        name.setText(list.get(position).getPetUtil().getName());
        num.setText(list.get(position).getNumber()+"");
        Glide.with(context).load(list.get(position).getPetUtil().getImg()).error(R.drawable.huafei1).into(photo);
        return convertView;
    }

}