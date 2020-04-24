package com.li.knowledgefarm.Settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateGradeDialog extends DialogFragment {
    private View view;
    /** 取消*/
    private Button btnReturn;
    /** 下拉选框*/
    private Spinner spinner;
    /** 保存*/
    private Button btnSave;
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
                        LoginActivity.user.setGrade(transmit(newGrade));
                        Toast.makeText(getContext(),"年级修改成功",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else{
                        Toast.makeText(getContext(),"年级修改失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_grade,container,false);

        /** 初始化*/
        init();

        /** 得出当前年级的position值，并设置*/
        int position = LoginActivity.user.getGrade() - 1;
        newGrade = spin[position];
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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        /** 点击返回*/
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    /**
     * 初始化
     */
    private void init(){
        btnReturn = view.findViewById(R.id.btnReturn);
        spinner = view.findViewById(R.id.spinner);
        btnSave = view.findViewById(R.id.btnSave);
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_expandable_list_item_1, spin);
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
                FormBody formBody = new FormBody.Builder().add("accout",LoginActivity.user.getAccount()).add("grade",""+transmit(newGrade)).build();
                final Request request = new Request.Builder().post(formBody).url(getContext().getResources().getString(R.string.URL)+"/user/updateUserGrade").build();
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
