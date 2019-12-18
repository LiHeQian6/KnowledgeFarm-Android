package com.li.knowledgefarm.MyFriends;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.User;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.RequiresApi;

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
            viewHolder = new ViewHolder();
            viewHolder.photo = convertView.findViewById(R.id.photo);
            viewHolder.name = convertView.findViewById(R.id.nickName);
            viewHolder.level = convertView.findViewById(R.id.level);
            viewHolder.account = convertView.findViewById(R.id.account);
            viewHolder.go=convertView.findViewById(R.id.go);
            setViewSize(convertView,viewHolder);
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
                if(!dataList.get(position).getAccout().equals(LoginActivity.user.getAccout())) {
                    Intent go = new Intent(context, MyFriendActivity.class);
                    go.putExtra("friend", (Serializable) dataList.get(position));
                    context.startActivity(go);
                }else{
                    Intent go = new Intent(context, MainActivity.class);
                    context.startActivity(go);
                }
            }
        });
        notifyDataSetChanged();
        return convertView;
    }

    /**
     * @Description 设置控件大小
     * @Auther 孙建旺
     * @Date 上午 10:14 2019/12/18
     * @Param []
     * @return void
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setViewSize(View convertView, ViewHolder viewHolder) {
        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams((int)(displayWidth*0.3),(int)(displayHeight*0.138));
        convertView.setLayoutParams(params_view);

        viewHolder.name.setTextSize((int)(displayHeight*0.013));
        viewHolder.name.setTextColor(context.getResources().getColor(R.color.ShopTextColor,null));

        LinearLayout.LayoutParams params_level = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_level.setMargins(0,(int)(displayHeight*0.014),0,(int)(displayHeight*0.014));
        viewHolder.level.setLayoutParams(params_level);
        viewHolder.level.setTextSize((int)(displayHeight*0.013));
        viewHolder.level.setTextColor(context.getResources().getColor(R.color.ShopTextColor,null));

        viewHolder.account.setTextSize((int)(displayHeight*0.013));
        viewHolder.account.setTextColor(context.getResources().getColor(R.color.ShopTextColor,null));

        LinearLayout.LayoutParams params_go = new LinearLayout.LayoutParams((int)(displayWidth*0.07),(int)(displayHeight*0.09));
        params_go.gravity = Gravity.CENTER_VERTICAL;
        viewHolder.go.setLayoutParams(params_go);
        viewHolder.go.setTextSize((int)(displayHeight*0.014));
        viewHolder.go.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    private class ViewHolder{
        public Button go;
        private ImageView photo;
        private TextView name;
        private TextView level;
        private TextView account;
    }
}

