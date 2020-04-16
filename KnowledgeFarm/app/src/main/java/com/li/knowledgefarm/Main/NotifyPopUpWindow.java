package com.li.knowledgefarm.Main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.li.knowledgefarm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 孙建旺
 * @description
 * @date 2020/04/15 下午2:51
 */

public class NotifyPopUpWindow extends PopupWindow {

    private ListView listView;

    public NotifyPopUpWindow(Context context) {
        super(context);
        this.setHeight(800);
        this.setWidth(1400);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.notify_popup_layout,
                null, false);
        listView = contentView.findViewById(R.id.notify_list_view);
        Map<String,String> map = new HashMap<>();
        map.put("title","测试1");
        map.put("content","主体内容1");
        Map<String,String> map2 = new HashMap<>();
        map2.put("title","测试2");
        map2.put("content","主体内容2");
        List<Map<String,String>> list = new ArrayList<>();
        list.add(map);
        list.add(map2);
        NotifyListAdapter adapter = new NotifyListAdapter(list,R.layout.notify_item_layout,context);
        listView.setAdapter(adapter);
        this.setContentView(contentView);
    }
}
