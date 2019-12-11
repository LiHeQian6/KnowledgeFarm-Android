package com.li.knowledgefarm.Study;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.MathBean;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MathActivity extends AppCompatActivity {
    /** 返回*/
    private ImageView iv_return;
    /** 自定义点击事件监听器*/
    private CustomerListener listener;
    private OkHttpClient okHttpClient;
    private TextView btnPreQuestion;
    private TextView btnNextQuestion;
    private Handler getMath;
    private Gson gson;
    private List<MathBean> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        /** 加载视图*/
        getViews();
        /** 注册点击事件监听器*/
        registListener();
        setStatusBar();
        getMaths();
        getMathHandler();
    }

    private void showQuestion(int position){

    }

    /**
     * @Description 处理返回的Json串
     * @Auther 孙建旺
     * @Date 上午 9:10 2019/12/11
     * @Param []
     * @return void
     */
    private void getMathHandler(){
        getMath = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = (String)msg.obj;
                if(data != null) {
                    Type type = new TypeToken<List<MathBean>>() {
                    }.getType();
                    datalist = gson.fromJson(data, type);
                }
            }
        };
    }

    /**
     * @Description 获取数学题
     * @Auther 孙建旺
     * @Date 上午 8:56 2019/12/11
     * @Param []
     * @return void
     */
    private void getMaths() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/answer/OneUpMath").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        getMath.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        getMath.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    class CustomerListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_return:
                    finish();
                    break;
                case R.id.btnPreQuestion:

                    break;
                case R.id.btnNextQuestion:

                    break;
            }
        }
    }

    /**
     * 加载视图
     */
    private void getViews(){
        iv_return = findViewById(R.id.iv_return);
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        datalist = new ArrayList<>();
        btnPreQuestion = findViewById(R.id.btnPreQuestion);
        btnNextQuestion = findViewById(R.id.btnNextQuestion);
    }

    /**
     * 注册点击事件监听器
     */
    private void registListener(){
        listener = new CustomerListener();
        iv_return.setOnClickListener(listener);
        btnPreQuestion.setOnClickListener(listener);
    }
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }
}
