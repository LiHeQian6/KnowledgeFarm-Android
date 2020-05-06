package com.li.knowledgefarm.notify;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.Notification;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SendNotifyAdapter extends BaseAdapter {

    private FriendsPage<Notification> list;
    private int id;
    private Context context;
    private String notifyYesId_str = "";
    private String notifyNoId_str = "";

    public SendNotifyAdapter(FriendsPage<Notification> list, int id, Context context) {
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
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context,id,null);
            viewHolder = new ViewHolder();
            viewHolder.photo = convertView.findViewById(R.id.send_photo);
            viewHolder.id = convertView.findViewById(R.id.send_id);
            viewHolder.nickName = convertView.findViewById(R.id.send_nickname);
            viewHolder.result = convertView.findViewById(R.id.send_result);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.photo)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(list.getList().get(position).getFrom().getPhoto()).apply(requestOptions).into(viewHolder.photo);
        viewHolder.id.setText("账号："+list.getList().get(position).getTo().getAccount()+"");
        viewHolder.nickName.setText("昵称："+list.getList().get(position).getTo().getNickName());
        //ToDo
        switch (list.getList().get(position).isHaveRead()){
            case 0:
                viewHolder.result.setText("正在等待回应");
                break;
            case 1:
                viewHolder.result.setText("已同意");
                break;
            case -1:
                viewHolder.result.setText("已拒绝");
                break;
            case 2:
                viewHolder.result.setText("已同意");
                if(notifyYesId_str.length() == 0){
                    notifyYesId_str += list.getList().get(position).getId();
                }else{
                    notifyYesId_str += "," + list.getList().get(position).getId();
                }
                break;
            case -2:
                viewHolder.result.setText("已拒绝");
                if(notifyNoId_str.length() == 0){
                    notifyNoId_str += list.getList().get(position).getId();
                }else{
                    notifyNoId_str += "," + list.getList().get(position).getId();
                }
                break;
        }
        if(position == list.getList().size() -1){
            ChangeReadStatus("-1",notifyNoId_str);
            ChangeReadStatus("1",notifyYesId_str);
        }
        return convertView;
    }

    private void ChangeReadStatus(final String if_read,final String ids){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/notification/editNotificationReadStatus?haveRead="+if_read+"&ids="+ids).build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                });
            }
        }.start();
    }

    private class ViewHolder{
        private TextView result;
        private ImageView photo;
        private TextView nickName;
        private TextView id;
    }
}
