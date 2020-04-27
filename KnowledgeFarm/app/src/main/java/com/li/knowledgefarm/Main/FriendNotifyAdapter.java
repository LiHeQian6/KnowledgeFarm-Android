package com.li.knowledgefarm.Main;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.Notification;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FriendNotifyAdapter extends BaseAdapter {

    private FriendsPage<Notification> list;
    private int id;
    private Context context;
    private Handler resultYesHandler;
    private Handler resultNoHandler;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        FriendViewHolder friendViewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context,id,null);
            friendViewHolder = new FriendViewHolder();
            friendViewHolder.photo = convertView.findViewById(R.id.request_photo);
            friendViewHolder.nickName = convertView.findViewById(R.id.request_nickname);
            friendViewHolder.id = convertView.findViewById(R.id.request_id);
            friendViewHolder.yes = convertView.findViewById(R.id.accept);
            friendViewHolder.no = convertView.findViewById(R.id.refuse);
            friendViewHolder.result = convertView.findViewById(R.id.result);
            friendViewHolder.result_li = convertView.findViewById(R.id.result_li);
            convertView.setTag(friendViewHolder);
        }else{
            friendViewHolder = (FriendViewHolder)convertView.getTag();
        }
        final FriendViewHolder finalFriendViewHolder = friendViewHolder;
        friendViewHolder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YesForFriend(position, finalFriendViewHolder);
            }
        });
        friendViewHolder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoForFriend(position,finalFriendViewHolder);
            }
        });
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.photo)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(list.getList().get(position).getFrom().getPhoto()).apply(requestOptions).into(friendViewHolder.photo);
        final ImageView view = friendViewHolder.photo;
        friendViewHolder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMessagePopUp userMessagePopUp = new UserMessagePopUp(context,list.getList().get(position).getFrom());
                userMessagePopUp.showAtLocation(view, Gravity.CENTER,0,0);
            }
        });
        friendViewHolder.id.setText("ID："+list.getList().get(position).getFrom().getId()+"");
        friendViewHolder.nickName.setText(list.getList().get(position).getFrom().getNickName()+" 申请添加你为他的好友");
        notifyDataSetChanged();
        return convertView;
    }

    private void NoForFriend(final int position, final FriendViewHolder friendViewHolder){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/userfriend/deleteUserFriend?account="
                                +list.getList().get(position).getTo().getAccount()+"&userId="+list.getList().get(position).getFrom().getId()).build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("通知信息", "请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String messages = response.body().string();
                        Message message = Message.obtain();
                        message.obj = messages;
                        resultNoHandler.sendMessage(message);
                    }
                });
            }
        }.start();
        resultNoHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                if(!result.equals("") && result.equals("true")){
                    friendViewHolder.result_li.setVisibility(View.GONE);
                    friendViewHolder.result.setText("已拒绝");
                    friendViewHolder.result.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(context,"网络出了点问题",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void YesForFriend(final int position, final FriendViewHolder friendViewHolder){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/userfriend/addUserFriend?account="
                                +list.getList().get(position).getTo().getAccount()+"&userId="+list.getList().get(position).getFrom().getId()).build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("通知信息", "请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String messages = response.body().string();
                        Message message = Message.obtain();
                        message.obj = messages;
                        resultYesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
        resultYesHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                if(!result.equals("") && result.equals("true")){
                    friendViewHolder.result_li.setVisibility(View.GONE);
                    friendViewHolder.result.setText("已同意");
                    friendViewHolder.result.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(context,"网络出了点问题",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private class FriendViewHolder{
        private LinearLayout result_li;
        private TextView result;
        private ImageView photo;
        private TextView nickName;
        private TextView id;
        private ImageView yes;
        private ImageView no;
    }
}
