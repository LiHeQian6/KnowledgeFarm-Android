package com.li.knowledgefarm.notify;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.EventBean;
import com.li.knowledgefarm.entity.FriendsPage;
import com.li.knowledgefarm.entity.Notification;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SendNotifyAdapter extends BaseAdapter {

    private final OkHttpClient okHttpClient;
    private FriendsPage<Notification> list;
    private int id;
    private Context context;
    private String notifyIds_str = "";
    private Handler handler;
    private Toast toast;

    public SendNotifyAdapter(FriendsPage<Notification> list, int id, Context context) {
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
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context,id,null);
            viewHolder = new ViewHolder();
            viewHolder.photo = convertView.findViewById(R.id.send_photo);
            viewHolder.id = convertView.findViewById(R.id.send_id);
            viewHolder.nickName = convertView.findViewById(R.id.send_nickname);
            viewHolder.result = convertView.findViewById(R.id.send_result);
            viewHolder.red = convertView.findViewById(R.id.system_send_red);
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
                viewHolder.red.setVisibility(View.GONE);
                break;
            case -1:
                viewHolder.result.setText("已拒绝");
                viewHolder.red.setVisibility(View.GONE);
                break;
            case 2:
                viewHolder.result.setText("已同意");
                viewHolder.red.setVisibility(View.VISIBLE);
                if(notifyIds_str.length() == 0){
                    notifyIds_str += list.getList().get(position).getId();
                }else{
                    notifyIds_str += "," + list.getList().get(position).getId();
                }
                break;
            case -2:
                viewHolder.result.setText("已拒绝");
                viewHolder.red.setVisibility(View.VISIBLE);
                if(notifyIds_str.length() == 0){
                    notifyIds_str += list.getList().get(position).getId();
                }else{
                    notifyIds_str += "," + list.getList().get(position).getId();
                }
                break;
        }
        return convertView;
    }

    /**
     * @Description 修改已读入口
     * @Author 孙建旺
     * @Date 下午3:47 2020/05/15
     * @Param []
     * @return void
     */
    public void AllHaveRead(){
        if(!notifyIds_str.equals("")){
            ChangeReadStatus(notifyIds_str);
        }
    }

    /**
     * @Description 修改已读
     * @Author 孙建旺
     * @Date 下午3:46 2020/05/15
     * @Param [if_read, ids]
     * @return void
     */
    private void ChangeReadStatus(final String ids){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("flag","1")
                        .add("ids",ids).build();
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(context.getResources().getString(R.string.URL)+"/notification/editNotificationReadStatus").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
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
                        if (list.getList().get(i).isHaveRead() == 2)
                            list.getList().get(i).setHaveRead(1);
                        if (list.getList().get(i).isHaveRead() == -2)
                            list.getList().get(i).setHaveRead(-1);
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
                    }
                }
            }
        };
    }

    private class ViewHolder{
        private TextView result;
        private ImageView photo;
        private TextView nickName;
        private TextView id;
        private ImageView red;
    }
}
