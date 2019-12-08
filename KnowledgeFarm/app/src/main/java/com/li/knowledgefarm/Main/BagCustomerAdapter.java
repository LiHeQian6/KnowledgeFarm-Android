package com.li.knowledgefarm.Main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @auther 孙建旺
 * @description 背包信息自定义Adapter
 * @date 2019/12/08 下午 2:21
 */

public class BagCustomerAdapter extends BaseAdapter {

    private Context context;
    private List<BagMessagesBean> dataList;
    private int resource;

    public BagCustomerAdapter(Context context, List<BagMessagesBean> dataList, int resource) {
        this.context = context;
        this.dataList = dataList;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
