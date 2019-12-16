package com.li.knowledgefarm.Study;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Settings.SettingActivity;
import com.li.knowledgefarm.entity.Question3Num;

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
    private TextView question;
    private TextView isFalse;
    private ImageView isTrue;
    private EditText answer;
    private Handler getMath;
    private Handler getWAF;
    private Gson gson;
    private List<Question3Num> datalist;
    private int position=0;
    private int TrueAnswerNumber = 0;
    private Dialog ifReturn;
    private Boolean returnHandlerFinish = false;
    private int displayWidth;
    private int displayHeight;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        /** 加载视图*/
        getViews();
        setViewSize();
        /** 注册点击事件监听器*/
        registListener();
        setStatusBar();
        getMaths();
        getMathHandler();
    }

    /**
     * @Description 设置控件适配屏幕
     * @Auther 孙建旺
     * @Date 上午 8:12 2019/12/16
     * @Param []
     * @return void
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setViewSize() {
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayWidth = ds.widthPixels;
        displayHeight = ds.heightPixels;

        TextView btnPre = findViewById(R.id.btnPreQuestion);
        TextView btnNext = findViewById(R.id.btnNextQuestion);
        LinearLayout question  = findViewById(R.id.linearQuestion);

        LinearLayout.LayoutParams params_btn = new LinearLayout.LayoutParams((int)(displayWidth*0.5),(int)(displayHeight*0.1));
        btnPre.setLayoutParams(params_btn);
        btnPre.setTextSize((int)(displayWidth*0.015));
        btnPre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnNext.setLayoutParams(params_btn);
        btnNext.setTextSize((int)(displayWidth*0.015));
        btnNext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        RelativeLayout.LayoutParams params_question = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)(displayHeight*0.2));
        params_question.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params_question.bottomMargin = (int)(displayHeight*0.2);
        question.setLayoutParams(params_question);
    }


    /**
     * @Description  确认是否返回
     * @Auther 孙建旺
     * @Date 下午 5:00 2019/12/11
     * @Param []
     * @return void
     */
    private void showIfReturn(){
        ifReturn = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.math_return_dialog,null);
        Button cancel = layout.findViewById(R.id.cancel_return);
        Button sure = layout.findViewById(R.id.sure_return);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifReturn.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnHandlerFinish = true;
                ifReturn.dismiss();
                if(getWAF == null)
                    getWandFCallBack();
                getWaterAndFertilizer();
            }
        });
        ifReturn.setContentView(layout);
        ifReturn.show();
        WindowManager.LayoutParams attrs = ifReturn.getWindow().getAttributes();
        attrs.gravity = Gravity.CENTER;
        final float scale = this.getResources().getDisplayMetrics().density;
        attrs.width = (int)(300*scale+0.5f);
        attrs.height =(int)(300*scale+0.5f);
        ifReturn.getWindow().setAttributes(attrs);
    }

    /**
     * @Description 获得水和肥料成功
     * @Auther 孙建旺
     * @Date 下午 4:58 2019/12/11
     * @Param []
     * @return void
     */
    private void getWandFCallBack(){
        getWAF = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = (String)msg.obj;
                if(data!= null){
                    if(!data.equals("-1")){
                        LoginActivity.user.setRewardCount(LoginActivity.user.getRewardCount() - 1);
                        answer.setVisibility(View.GONE);
                        isFalse.setVisibility(View.INVISIBLE);
                        isTrue.setVisibility(View.GONE);
                        question.setText("你获得了水和肥料哦，快去照顾你的植物吧！");
                        question.setTextSize((int)(displayWidth*0.012));
                        question.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        if(returnHandlerFinish)
                            finish();
                    }else{
                        isFalse.setText("获得奖励失败了哦！");
                    }
                }
            }
        };
    }

    /**
     * @Description 答题正确获取奖励
     * @Auther 孙建旺
     * @Date 下午 2:17 2019/12/11
     * @Param []
     * @return void
     */
    private void getWaterAndFertilizer(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("userId", LoginActivity.user.getId()+"")
                        .add("water",TrueAnswerNumber*2+"")
                        .add("fertilizer",TrueAnswerNumber*2+"").build();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/user/addUserWater").post(formBody).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        getWAF.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        getWAF.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    /**
     * @Description 展示下一道题目
     * @Auther 孙建旺
     * @Date 上午 11:33 2019/12/11
     * @Param [pos]
     * @return void
     */
    private void showQuestion(int pos){
        if(datalist.get(pos).getIfDone().equals("true")) {
            isFalse.setText(" ");
            isTrue.setVisibility(View.INVISIBLE);
            answer.setVisibility(View.INVISIBLE);
            question.setText(datalist.get(pos).toString()+ datalist.get(pos).getResult());
        }else{
            if(answer.getVisibility() == View.INVISIBLE){
                answer.setVisibility(View.VISIBLE);
            }
            isFalse.setText("");
            isTrue.setVisibility(View.INVISIBLE);
            question.setText(datalist.get(pos).toString());
        }
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
                    Type type = new TypeToken<List<Question3Num>>() {
                    }.getType();
                    datalist = gson.fromJson(data, type);
                    showQuestion(position);
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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                super.run();
                if (LoginActivity.user.getRewardCount() <= 0) {
                    question.setText("今天的任务都做完了哦！");
                    question.setTextSize((int)(displayWidth*0.015));
                    question.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    answer.setVisibility(View.GONE);
                    btnNextQuestion.setVisibility(View.GONE);
                    btnPreQuestion.setVisibility(View.GONE);
                } else {
                    Request request = null;
                    switch (LoginActivity.user.getGrade()) {
                        case 1:
                            request = new Request.Builder().url(getResources().getString(R.string.URL) + "/answer/OneUpMath").build();
                            break;
                        case 2:
                            request = new Request.Builder().url(getResources().getString(R.string.URL) + "/answer/OneDownMath").build();
                    }
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
            }
        }.start();

    }

    class CustomerListener implements View.OnClickListener{

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_return:
                    if(TrueAnswerNumber>0 && TrueAnswerNumber<datalist.size() && LoginActivity.user.getRewardCount()>0)
                        showIfReturn();
                    else
                        finish();
                    break;
                case R.id.btnPreQuestion:
                    if((position-1)>=0) {
                        position = --position;
                        showQuestion(position);
                    }
                    break;
                case R.id.btnNextQuestion:
                    String inputRes = answer.getText().toString().trim();
                    if(answer.getVisibility() == View.INVISIBLE){
                        position++;
                        showQuestion(position);
                        return;
                    }
                    if(inputRes.equals(datalist.get(position).getResult()+"")) {
                        TrueAnswerNumber++;
                        isTrue.setImageDrawable(getResources().getDrawable(R.drawable.duigou,null));
                        isTrue.setVisibility(View.VISIBLE);
                        isFalse.setText("答对啦！获得了奖励哦！");
                        isFalse.setVisibility(View.VISIBLE);
                        if((position+1)<=datalist.size()-1) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    answer.setText("");
                                    datalist.get(position).setIfDone("true");
                                    position = ++position;
                                    showQuestion(position);
                                }
                            }, 1000);
                        }else{
                            getWandFCallBack();
                            getWaterAndFertilizer();
                            btnNextQuestion.setClickable(false);
                        }
                    }else{
                        isTrue.setImageDrawable(getResources().getDrawable(R.drawable.cha,null));
                        isTrue.setVisibility(View.VISIBLE);
                        isFalse.setText("你还差一点就答对了哦！");
                        isFalse.setVisibility(View.VISIBLE);
                    }
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
        question = findViewById(R.id.tvQuestion);
        answer = findViewById(R.id.tvAnswer);
        isTrue = findViewById(R.id.isTrue);
        isFalse = findViewById(R.id.isFalse);
    }

    /**
     * 注册点击事件监听器
     */
    private void registListener(){
        listener = new CustomerListener();
        iv_return.setOnClickListener(listener);
        btnPreQuestion.setOnClickListener(listener);
        btnNextQuestion.setOnClickListener(listener);
        answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(position==datalist.size()-1)
                    btnNextQuestion.setText("我做完啦!");
            }
        });
    }
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }

}
