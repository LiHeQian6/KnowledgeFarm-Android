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
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.Notification;

public class FriendNotifyAdapter extends BaseAdapter {

    private FriendsPage<Notification> list;
    private int id;
    private Context context;

    public FriendNotifyAdapter(FriendsPage<Notification> list, int id, Context context) {
        this.list = list;
        this.id = id;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return list.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendViewHolder friendViewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context,id,null);
            friendViewHolder = new FriendViewHolder();
            friendViewHolder.photo = convertView.findViewById(R.id.request_photo);
            friendViewHolder.nickName = convertView.findViewById(R.id.request_nickname);
            friendViewHolder.id = convertView.findViewById(R.id.request_id);
            friendViewHolder.yes = convertView.findViewById(R.id.refuse);
            friendViewHolder.no = convertView.findViewById(R.id.accept);
            friendViewHolder.yes.setOnClickListener(new CustomerOnclickListner());
            friendViewHolder.no.setOnClickListener(new CustomerOnclickListner());
            friendViewHolder.photo.setOnClickListener(new CustomerOnclickListner());
            convertView.setTag(friendViewHolder);
        }else{
            friendViewHolder = (FriendViewHolder)convertView.getTag();
        }
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(list.getList().get(position).getFrom().getPhoto()).apply(requestOptions).into(friendViewHolder.photo);
        friendViewHolder.id.setText(list.getList().get(position).getFrom().getId());
        friendViewHolder.nickName.setText(list.getList().get(position).getFrom().getNickName());
        return convertView;
    }

    class CustomerOnclickListner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.accept:

                    break;
                case R.id.refuse:

                    break;
            }
        }
    }

    private class FriendViewHolder{
        private ImageView photo;
        private TextView nickName;
        private TextView id;
        private ImageView yes;
        private ImageView no;
    }
}
