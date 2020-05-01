package com.li.knowledgefarm.Shop;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class PagerViewAdapter extends PagerAdapter {

    private Map<String,GridView> map;
    private Context context;
    private String tag;

    public PagerViewAdapter(Map<String,GridView> map, Context context) {
        this.map = map;
        this.context = context;
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        switch (position){
            case 0:
                tag = "plant";
                break;
            case 1:
                tag = "pet";
                break;
        }
        container.addView(map.get(tag));
        return map.get(tag);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(map.get(tag));
    }
}
