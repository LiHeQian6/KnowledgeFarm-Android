package com.li.knowledgefarm.Main;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 孙建旺
 * @description
 * @date 2020/04/20 下午3:35
 */

public class UserMessagePopUp extends PopupWindow {

    private Context context;
    private ImageView back;
    private User user;
    private TextView nickName;
    private TextView id;
    private TextView level;
    private TextView grade;

    public UserMessagePopUp(Context context,User user) {
        super(context);
        this.context  = context;
        this.user = user;
        Init();
    }

    private void Init(){
        this.setHeight(600);
        this.setWidth(1000);
        this.setOutsideTouchable(true);
        this.setFocusable(false);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.user_message_popup,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
        setText();
        registListener();
    }

    private void setText(){
        nickName.setText("昵称："+user.getNickName());
        id.setText("ID："+user.getId()+"");
        level.setText("等级："+user.getLevel());
        grade.setText("年级："+user.getGrade());
    }

    private void registListener(){
//        back.setOnClickListener(new CustomerOnclickListener());
//        changeName.setOnClickListener(new CustomerOnclickListener());
    }

    private void getViews(View view){
        nickName = view.findViewById(R.id.this_user_nickname);
        id = view.findViewById(R.id.this_user_id);
        level = view.findViewById(R.id.this_user_level);
        grade = view.findViewById(R.id.this_user_grade);
    }

//    private void disMissPopup(){
//        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//        lp.alpha = 1;
//        activity.getWindow().setAttributes(lp);
//        this.dismiss();
//    }

//    private void showChangeNamePopUp(){
//        View contentView = LayoutInflater.from(context).inflate(R.layout.activity_setting,
//                null, false);
//        PopupWindow popupWindow = new PopupWindow(context,300,300);
//        popupWindow.setHeight(300);
//        popupWindow.setWidth(300);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setFocusable(true);
//        popupWindow.setAnimationStyle(R.style.notify_pop_animation);
//        popupWindow.setContentView(contentView);
//        popupWindow.showAtLocation(changeName, Gravity.CENTER,0,0);
//    }

//    class CustomerOnclickListener implements View.OnClickListener{
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//                case R.id.iv_return:
//                    disMissPopup();
//                    break;
//                case R.id.btnUpdateNickName:
////                    showChangeNamePopUp();
//                    break;
//            }
//        }
//    }
}
