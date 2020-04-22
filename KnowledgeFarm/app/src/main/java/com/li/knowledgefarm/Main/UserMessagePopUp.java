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

import com.li.knowledgefarm.R;

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
    private Activity activity;
//    private ImageView back;
//    private Button changeName;

    public UserMessagePopUp(Context context, Activity activity) {
        super(context);
        this.context  = context;
        this.activity = activity;
        Init();
    }

    private void Init(){
        this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.activity_setting,
                null, false);
        this.setContentView(contentView);
//        getViews(contentView);
//        registListener();
    }

//    private void registListener(){
//        back.setOnClickListener(new CustomerOnclickListener());
//        changeName.setOnClickListener(new CustomerOnclickListener());
//    }
//
//    private void getViews(View view){
//        back = view.findViewById(R.id.iv_return);
//        changeName = view.findViewById(R.id.btnUpdateNickName);
//    }
//
//    private void disMissPopup(){
//        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//        lp.alpha = 1;
//        activity.getWindow().setAttributes(lp);
//        this.dismiss();
//    }

//    private void showChangeNamePopUp(){
//        View contentView = LayoutInflater.from(context).inflate(R.layout.activity_setting,
//                null, false);
//        PopupWindow popupWindow = new PopupWindow(,300,300);
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
//                    showChangeNamePopUp();
//                    break;
//            }
//        }
//    }
}
