package com.li.knowledgefarm.MyFriends;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.NavigationBarUtil;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.User;
import com.li.knowledgefarm.pk.PkActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class FriendsCustomerAdapter extends BaseAdapter {

    private int searchSelectedItem;
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

    public FriendsCustomerAdapter(Context context, List<User> dataList, int resource, int searchSelectedItem) {
        this.context = context;
        this.dataList = dataList;
        this.resource = resource;
        this.searchSelectedItem=searchSelectedItem;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
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
            viewHolder.add=convertView.findViewById(R.id.add);
            viewHolder.delete=convertView.findViewById(R.id.delete);
            viewHolder.pk=convertView.findViewById(R.id.pk);
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
        viewHolder.name.setTextSize(14);
        viewHolder.level.setTextSize(10);
        viewHolder.account.setTextSize(8);
        viewHolder.account.setText("账号:"+dataList.get(position).getAccount());
        viewHolder.level.setText("lv:"+dataList.get(position).getLevel());
        viewHolder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dataList.get(position).getAccount().equals(UserUtil.getUser().getAccount())) {
                    Intent go = new Intent(context, MyFriendActivity.class);
                    go.putExtra("friend", (Serializable) dataList.get(position));
                    context.startActivity(go);
                }else{
                    Intent go = new Intent(context, MainActivity.class);
                    context.startActivity(go);
                }
            }
        });
        if (searchSelectedItem==0){
            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<Object, Object> event = new HashMap<>();
                    event.put(dataList.get(position).getAccount(),false);
                    EventBus.getDefault().post(event);
                    notifyDataSetChanged();
                }
            });
            viewHolder.pk.setVisibility(View.VISIBLE);
            viewHolder.pk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showIsPk(dataList.get(position));
                }
            });
        }else{
            viewHolder.add.setVisibility(View.VISIBLE);
            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<Object, Object> event = new HashMap<>();
                    event.put(dataList.get(position).getAccount(),true);
                    EventBus.getDefault().post(event);
                }
            });
        }
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
        viewHolder.name.setTextColor(ContextCompat.getColor(context,R.color.ShopTextColor));

        LinearLayout.LayoutParams params_level = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_level.setMargins(0,(int)(displayHeight*0.014),0,(int)(displayHeight*0.014));
        viewHolder.level.setLayoutParams(params_level);
        viewHolder.level.setTextSize((int)(displayHeight*0.013));
        viewHolder.level.setTextColor(ContextCompat.getColor(context,R.color.ShopTextColor));

        viewHolder.account.setTextSize((int)(displayHeight*0.013));
        viewHolder.account.setTextColor(ContextCompat.getColor(context,R.color.ShopTextColor));

        LinearLayout.LayoutParams params_go = new LinearLayout.LayoutParams((int)(displayWidth*0.07),(int)(displayHeight*0.09));
        params_go.gravity = Gravity.CENTER_VERTICAL;
        viewHolder.go.setLayoutParams(params_go);
        viewHolder.go.setTextSize((int)(displayHeight*0.014));
        viewHolder.go.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    private class ViewHolder{
        public Button go;
        private ImageView photo;
        private ImageView add;
        private ImageView delete;
        private ImageView pk;
        private TextView name;
        private TextView level;
        private TextView account;
    }

    private void showIsPk(final User user){
        final Dialog dialog = new Dialog(context);
        View view = View.inflate(context, R.layout.math_return_dialog, null);
        TextView text = view.findViewById(R.id.waringText);
        ImageView cancel = view.findViewById(R.id.cancel_return);
        ImageView sure = view.findViewById(R.id.sure_return);
        text.setText("确定花费10点体力值对他发起pk吗？");
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getPetHouses().get(0).getPhysical()!=0){
                    Intent go = new Intent(context, PkActivity.class);
                    go.putExtra("friend", user);
                    dialog.dismiss();
                    context.startActivity(go);
                }else{
                    Toast.makeText(context,"宠物没有体力了，快去补充体力吧！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setContentView(view);
        NavigationBarUtil.focusNotAle(dialog.getWindow());
        dialog.show();
        NavigationBarUtil.hideNavigationBar(dialog.getWindow());
        NavigationBarUtil.clearFocusNotAle(dialog.getWindow());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
        if (dialog.getWindow() != null) {
            //bagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0f);//去除遮罩
        }
        attrs.gravity = Gravity.CENTER;
        final float scale = context.getResources().getDisplayMetrics().density;
        attrs.width = (int) (300 * scale + 0.5f);
        attrs.height = (int) (200 * scale + 0.5f);
        dialog.getWindow().setAttributes(attrs);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }
}

