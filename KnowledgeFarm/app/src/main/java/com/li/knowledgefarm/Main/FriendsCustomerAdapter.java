package com.li.knowledgefarm.Main;


import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.MyFriends.MyFriendActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.BagCropNumber;
import com.li.knowledgefarm.entity.User;

import java.io.Serializable;
import java.util.List;

public class FriendsCustomerAdapter extends BaseAdapter {

    private Context context;
    private List<User> dataList;
    private int resource;
    private int displayHeight;
    private int displayWidth;

    public FriendsCustomerAdapter(Context context, List<User> dataList, int resource) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context,resource,null);
            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics ds = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(ds);
            displayHeight = ds.heightPixels;
            displayWidth = ds.widthPixels;
//            LinearLayout.LayoutParams params_gridview = new LinearLayout.LayoutParams((int)(displayWidth*0.09), ViewGroup.LayoutParams.WRAP_CONTENT);
//            params_gridview.gravity = Gravity.CENTER_HORIZONTAL;
//            params_gridview.setMargins(0,(int)(displayHeight*0.05),0,0);
//            convertView.setLayoutParams(params_gridview);
            viewHolder = new ViewHolder();
            viewHolder.photo = convertView.findViewById(R.id.photo);
            viewHolder.name = convertView.findViewById(R.id.nickName);
            viewHolder.level = convertView.findViewById(R.id.level);
            viewHolder.account = convertView.findViewById(R.id.account);
            viewHolder.go=convertView.findViewById(R.id.go);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(dataList.get(position).getPhoto()).apply(requestOptions).into(viewHolder.photo);
        viewHolder.name.setText(dataList.get(position).getNickName());
        viewHolder.account.setText("账号:"+dataList.get(position).getAccout());
        viewHolder.level.setText("lv:"+dataList.get(position).getLevel());
        viewHolder.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(context, MyFriendActivity.class);
                go.putExtra("friend", (Serializable) dataList.get(position));
                context.startActivity(go);
            }
        });
        notifyDataSetChanged();
        return convertView;
    }

    private class ViewHolder{
        public Button go;
        private ImageView photo;
        private TextView name;
        private TextView level;
        private TextView account;
    }
}

