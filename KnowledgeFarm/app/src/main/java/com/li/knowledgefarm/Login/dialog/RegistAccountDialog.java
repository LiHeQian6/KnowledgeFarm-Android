package com.li.knowledgefarm.Login.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.R;

import org.json.JSONException;
import org.json.JSONObject;

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
    private RelativeLayout closeImg;
    private LinearLayout linearAccount;
    private LinearLayout linearRegist;
    private TextView newAccount;
    private String[] array;
    private SpinnerAdapter arrayAdapter;
    private int displayWidth;
    private int displayHeight;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 4:
                    Toast.makeText(getContext(),"注册成功！",Toast.LENGTH_SHORT).show();
                    linearRegist.setVisibility(View.GONE);
                    linearAccount.setVisibility(View.VISIBLE);
                    newAccount.setText(msg.obj.toString());
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

        setViewSize(view);
        array = getResources().getStringArray(R.array.sarry);
        Spinner spinner = view.findViewById(R.id.spinner);
        arrayAdapter = new SpinnerAdapter(getContext(),array);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new ProvOnItemSelectedListener());
        linearAccount = view.findViewById(R.id.linearCount);
        linearRegist = view.findViewById(R.id.linearRegist);
        closeImg = view.findViewById(R.id.closeImg);
        newAccount = view.findViewById(R.id.newAccount);
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
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    /**
     * @Description 设置控件适配屏幕
     * @Auther 孙建旺
     * @Date 下午 1:04 2019/12/15
     * @Param []
     * @return void
     */
    private void setViewSize(View view) {
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayWidth = ds.widthPixels;
        displayHeight = ds.heightPixels;

        EditText nickname = view.findViewById(R.id.registName);
        EditText pwd = view.findViewById(R.id.registPwd2);
        EditText configPwd = view.findViewById(R.id.configPwd2);
        Spinner grade = view.findViewById(R.id.spinner);
        Button registe = view.findViewById(R.id.btnRegist2);

        LinearLayout.LayoutParams params_nickname = new LinearLayout.LayoutParams((int)(displayWidth*0.4),(int)(displayHeight*0.1));
        params_nickname.gravity = Gravity.CENTER_HORIZONTAL;
        params_nickname.setMargins(0,(int)(displayHeight*0.03),0,0);
        nickname.setLayoutParams(params_nickname);
        pwd.setLayoutParams(params_nickname);
        configPwd.setLayoutParams(params_nickname);

        LinearLayout.LayoutParams params_spinner = new LinearLayout.LayoutParams((int)(displayWidth*0.3),(int)(displayHeight*0.1));
        params_spinner.gravity = Gravity.CENTER_HORIZONTAL;
        params_spinner.setMargins(0,(int)(displayHeight*0.05),0,0);
        grade.setLayoutParams(params_spinner);

        LinearLayout.LayoutParams params_registe = new LinearLayout.LayoutParams((int)(displayWidth*0.2),(int)(displayHeight*0.1));
        params_registe.setMargins(0,(int)(displayHeight*0.05),0,0);
        params_registe.gravity = Gravity.CENTER_HORIZONTAL;
        registe.setLayoutParams(params_registe);

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
                String result = response.body().string();
                Message message = new Message();
                if(result.equals("fail")){
                    message.what = 5;
                    handler.sendMessage(message);
                }else {
                    message.what = 4;
                    try {
                        message.obj = new JSONObject(result).getString("accout");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
