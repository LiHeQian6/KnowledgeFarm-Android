package com.li.knowledgefarm.Main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.li.knowledgefarm.R;

public class NotifyPopUpWindow extends PopupWindow {

    public NotifyPopUpWindow(Context context) {
        super(context);
        this.setHeight(800);
        this.setWidth(1200);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.notify_message_pop,
                null, false);
        this.setContentView(contentView);
    }
}
