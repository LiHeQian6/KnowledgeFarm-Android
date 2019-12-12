package com.li.knowledgefarm.Login.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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


public class RegistAccountDialog extends DialogFragment {

    private String rName;
    private String grade;
    private String password;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 4:
                    Toast.makeText(getContext(),"注册成功！",Toast.LENGTH_SHORT).show();
                    dismiss();
                    break;
                case 5:
                    Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    dismiss();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.regist_dialog,container,false);

        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new ProvOnItemSelectedListener());
        final EditText registName = view.findViewById(R.id.registName);
        final EditText pwd = view.findViewById(R.id.registPwd2);
        final EditText configPwd = view.findViewById(R.id.configPwd2);
        Button button = view.findViewById(R.id.btnRegist2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rName = registName.getText().toString();
                password = pwd.getText().toString();
                String config = configPwd.getText().toString();
                if(rName.equals("")||password.equals("")||config.equals("")){
                    Toast.makeText(getContext(),"请完善注册信息！",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!password.equals(config)){
                    Toast.makeText(getContext(),"密码输入不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    new Thread(){
                        @Override
                        public void run() {
                            registToServer();
                        }
                    }.start();
                }
            }
        });

        return view;
    }

    private void registToServer() {
        //Request对象(Post、FormBody)
        FormBody formBody = new FormBody.Builder()
                .add("nickName",rName)
                .add("grade",grade)
                .add("password",password)
                .build();
        Request request = new Request.Builder().post(formBody).url(getResources().getString(R.string.URL)+"/user/registAccout").build();
        //Call
        Call call = new OkHttpClient().newCall(request);
        //异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("jing", "请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                if(response.body().string().equals("fail")){
                    message.what = 5;
                    handler.sendMessage(message);
                }else {
                    message.what = 4;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class ProvOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    grade = "1";
                    break;
                case 1:
                    grade = "2";
                    break;
                case 2:
                    grade = "3";
                    break;
                case 3:
                    grade = "4";
                    break;
                case 4:
                    grade = "5";
                    break;
                case 5:
                    grade = "6";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            grade = "1";
        }
    }
}
