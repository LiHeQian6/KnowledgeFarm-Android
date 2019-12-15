package com.li.knowledgefarm.Login.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.li.knowledgefarm.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @auther 孙建旺
 * @description 自定义Spinner适配器
 * @date 2019/12/15 下午 1:55
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] stringArray;

    public SpinnerAdapter(Context context, String[] stringArray) {
        super(context, android.R.layout.simple_spinner_item, stringArray);
        this.context = context;
        this.stringArray=stringArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(stringArray[position]);
        tv.setTextSize(25);
        tv.setTextColor(context.getColor(R.color.editTextCorlor));
        tv.setBackgroundColor(context.getColor(R.color.ShopTextColor));
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(stringArray[position]);
        tv.setTextSize(25);
        tv.setTextColor(context.getColor(R.color.editTextCorlor));
        tv.setBackgroundColor(context.getColor(R.color.ShopTextColor));

        return convertView;
    }
}
