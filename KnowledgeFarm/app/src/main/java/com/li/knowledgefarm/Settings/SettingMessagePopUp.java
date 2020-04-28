package com.li.knowledgefarm.Settings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.li.knowledgefarm.R;

public class SettingMessagePopUp extends PopupWindow {

    private Context context;
    private Button my_message; //我的信息按钮
    private Button system_setting; //系统设置按钮
    private LinearLayout user_mess_li; //个人信息展示块
    private LinearLayout system_setting_li; //系统设置展示块

    public SettingMessagePopUp(Context context) {
        super(context);
        this.context = context;
        this.setOutsideTouchable(true);
        this.setFocusable(false);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.change_nickname,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
        registListener();
    }

    private void registListener(){
        my_message.setOnClickListener(new CustomerOnclickListener());
        system_setting.setOnClickListener(new CustomerOnclickListener());
    }

    /**
     * @Description 获取控件ID
     * @Author 孙建旺
     * @Date 下午9:32 2020/04/28
     * @Param []
     * @return void
     */
    private void getViews(View view) {
        my_message = view.findViewById(R.id.my_message_btn);
        system_setting = view.findViewById(R.id.system_setting);
        user_mess_li = view.findViewById(R.id.user_mess_li);
        system_setting_li = view.findViewById(R.id.system_setting_li);
    }

    private class CustomerOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.my_message_btn:
                    user_mess_li.setVisibility(View.VISIBLE);
                    system_setting_li.setVisibility(View.GONE);
                    break;
                case R.id.system_setting:
                    user_mess_li.setVisibility(View.GONE);
                    system_setting_li.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
