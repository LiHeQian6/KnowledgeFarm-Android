package com.li.knowledgefarm.Settings;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateGradeDialog extends PopupWindow {
    private View view;
    private Context context;
    /** 返回*/
    private ImageView iv_return;
    /** 下拉选框*/
    private Spinner spinner;
    /** 保存*/
    private TextView tv_save;
    /** OkHttpClient*/
    private OkHttpClient okHttpClient;
    /** 下拉选框数据源*/
    private String spin[] = {"一年级上","一年级下","二年级上","二年级下","三年级上","三年级下"};
    /** 适配器*/
    private ArrayAdapter<String> arrayAdapter;
    /** 选中的年级*/
    private String newGrade;
    /** 线程服务端返回处理*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1: // 修改年级判断
                    if(msg.obj.equals("true")){
                        Toast.makeText(context,"年级修改成功",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else{
                        Toast.makeText(context,"年级修改失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public UpdateGradeDialog(final Context context, String grade) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.update_grade, null);

        /** 设置设置popupWindow样式*/
        setpopupWndow();

        /** 初始化*/
        init();

        /** 得出当前年级的position值，并设置*/
        int position = Integer.parseInt(grade) - 1;
        spinner.setSelection(position,true);

        /** 监听下拉选框*/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                newGrade = spin[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** 点击保存*/
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        /** 点击返回*/
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * 设置popupWindow样式
     */
    private void setpopupWndow(){
        this.setContentView(view);
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        //this.setAnimationStyle(R.style.pop_animation);
        //ColorDrawable dw = new ColorDrawable(0xb0000000);//背景半透明
        ColorDrawable d = new ColorDrawable(Color.parseColor("#f5f5f5"));
        this.setBackgroundDrawable(d);
    }

    /**
     * 初始化
     */
    private void init(){
        iv_return = view.findViewById(R.id.iv_return);
        spinner = view.findViewById(R.id.spinner);
        tv_save = view.findViewById(R.id.tv_save);
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_expandable_list_item_1, spin);
        spinner.setAdapter(arrayAdapter);
        okHttpClient = new OkHttpClient();
    }

    /**
     * 保存
     */
    private void save(){
        new Thread(){
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("accout","71007839").add("grade",""+transmit(newGrade)).build();
                final Request request = new Request.Builder().post(formBody).url("http://"+context.getResources().getString(R.string.IP)+":8080/FarmKnowledge/user/updateUserGrade").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww","请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        sendMessage(1,result);
                    }
                });
            }
        }.start();
    }

    /**
     * 年级形式转换(string -> double)
     */
    private int transmit(String grade){
        switch (grade){
            case "一年级上":
                return 1;
            case "一年级下":
                return 2;
            case "二年级上":
                return 3;
            case "二年级下":
                return 4;
            case "三年级上":
                return 5;
            case "三年级下":
                return 6;
        }
        return 0;
    }

    /**
     * handler发送message
     */
    private void sendMessage(int what ,Object obj){
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        handler.sendMessage(message);
    }

}
