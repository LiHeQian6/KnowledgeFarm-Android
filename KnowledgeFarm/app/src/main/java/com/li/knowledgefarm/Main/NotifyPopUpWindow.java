package com.li.knowledgefarm.Main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.Notification;

public class NotifyPopUpWindow extends PopupWindow {

    private Button delete;
    private Notification notification;
    private TextView not_title;
    private TextView not_content;
    private TextView not_time;



    public NotifyPopUpWindow(final Context context,Notification notification) {
        super(context);
        this.notification = notification;
        this.setHeight(800);
        this.setWidth(1200);
        this.setOutsideTouchable(true);
        this.setFocusable(false);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        final View contentView = LayoutInflater.from(context).inflate(R.layout.notify_message_pop,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
        setText();
    }

    private void getViews(View view){
        not_title = view.findViewById(R.id.not_title);
        not_time = view.findViewById(R.id.not_time);
        not_content = view.findViewById(R.id.not_content);
        delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setText(){
        not_title.setText(notification.getTitle());
        not_content.setText(notification.getContent());
        not_time.setText(notification.getCreateTime().toString());
    }
}
