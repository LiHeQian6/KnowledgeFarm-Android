package com.li.knowledgefarm.Main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.li.knowledgefarm.R;

/**
 * @author 孙建旺
 * @description
 * @date 2020/04/15 下午2:51
 */

public class NotifyPopUpWindow extends PopupWindow {

    public NotifyPopUpWindow(Context context) {
        super(context);
        this.setHeight(300);
        this.setWidth(300);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.notify_popup_layout,
                null, false);
        this.setContentView(contentView);
    }
}
