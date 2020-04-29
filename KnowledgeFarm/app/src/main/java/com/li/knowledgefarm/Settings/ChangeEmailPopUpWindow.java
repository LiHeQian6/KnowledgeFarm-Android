package com.li.knowledgefarm.Settings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;

public class ChangeEmailPopUpWindow extends PopupWindow {
    private Context context;
    private String type;
    private TextView show_title;
    private EditText new_message;
    private EditText vertical;
    private Button get_vertical_btn;
    private Button commit_btn;
    private LinearLayout get_vertical_li;

    public ChangeEmailPopUpWindow(Context context,String type) {
        super(context);
        this.context = context;
        this.type = type;
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        this.setAnimationStyle(R.style.pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.change_message_pop,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
        showTitle();
    }

    /**
     * @Description 更改标题
     * @Author 孙建旺
     * @Date 下午10:34 2020/04/29
     * @Param []
     * @return void
     */
    private void showTitle() {
        if(type.equals("Email")){
            show_title.setText("绑定邮箱");
            new_message.setHint("请输入邮箱");
        }else {
            show_title.setText("修改昵称");
            new_message.setHint("请输入新的昵称");
            get_vertical_li.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * @Description 获取控件ID
     * @Author 孙建旺
     * @Date 下午10:28 2020/04/29
     * @Param []
     * @return void
     */
    private void getViews(View view) {
        show_title = view.findViewById(R.id.show_title);
        new_message = view.findViewById(R.id.new_message);
        vertical = view.findViewById(R.id.vertical_edit);
        get_vertical_btn = view.findViewById(R.id.getVertical_btn);
        commit_btn = view.findViewById(R.id.commit_btn);
        get_vertical_li = view.findViewById(R.id.getVertical_li);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setFocusable(false);
        super.showAtLocation(parent, gravity, x, y);
        final View view = getContentView();
        FullScreen.hideBottomUIMenu(view);
        setFocusable(true);
        update();
    }
}
