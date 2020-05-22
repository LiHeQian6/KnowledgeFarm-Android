package com.li.knowledgefarm.notify;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.EventBean;
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.Notification;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OtherNotifyAdapter extends BaseAdapter {

    private FriendsPage<Notification> list;
    private int id;
    private Context context;
    private String ids = "";
    private final OkHttpClient okHttpClient;
    private Handler handler;
    private Toast toast;

    public OtherNotifyAdapter(FriendsPage<Notification> list, int id, Context context) {
        this.list = list;
        this.id = id;
        this.context = context;
        okHttpClient = OkHttpUtils.getInstance(context);
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
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = View.inflate(context,id,null);
            viewHolder = new ViewHolder();
            viewHolder.other_title = convertView.findViewById(R.id.other_notify_title);
            viewHolder.other_content = convertView.findViewById(R.id.other_notify_content);
            viewHolder.other_red_point = convertView.findViewById(R.id.other_notify_item_red);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.other_title.setText(list.getList().get(position).getTitle());
        viewHolder.other_content.setText(list.getList().get(position).getContent());
        if(list.getList().get(position).isHaveRead() == 0) {
            viewHolder.other_red_point.setVisibility(View.VISIBLE);
            if (position == 0) {
                ids += list.getList().get(position).getId();
            } else {
                ids += "," + list.getList().get(position).getId();
            }
        }else {
            viewHolder.other_red_point.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * @Description 修改状态
     * @Author 孙建旺
     * @Date 上午9:30 2020/05/22
     * @Param []
     * @return void
     */
    public void changeStatus(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("flag","0")
                        .add("ids",ids).build();
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(context.getResources().getString(R.string.URL)+"/notification/editNotificationReadStatus").build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(((String)msg.obj).equals("true")) {
                    for (int i = 0; i < list.getList().size(); i++) {
                        if (list.getList().get(i).isHaveRead() == 0)
                            list.getList().get(i).setHaveRead(1);
                    }
                    notifyDataSetChanged();
                    EventBean eventBean = new EventBean();
                    eventBean.setIfRead(true);
                    EventBus.getDefault().post(eventBean);
                }else {
                    if(toast == null){
                        toast = Toast.makeText(context,"操作失败",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM,0,0);
                        toast.show();
                        toast = null;
                    }
                }
            }
        };
    }

    private static class ViewHolder{
        private TextView other_title;
        private TextView other_content;
        private ImageView other_red_point;
    }
}
