package com.li.knowledgefarm.Shop;

import android.os.Bundle;
import android.widget.GridView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Map<String, List> itemList;
    private String[] tab = {"植物","宠物"};

    public MyFragmentPagerAdapter(@NonNull FragmentManager fm,Map<String, List> itemList) {
        super(fm);
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        MyFragment myFragment = new MyFragment();
        Bundle bundle = new Bundle();
        switch (position){
            case 0:
                bundle.putSerializable("plant", (Serializable) itemList.get("plant"));
                break;
            case 1:
                bundle.putSerializable("pet", (Serializable) itemList.get("pet"));
                break;
        }
        bundle.putInt(MyFragment.SHOP,position+1);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public int getCount() {
        return tab.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab[position];
    }
}
