package com.li.knowledgefarm.Study.Subject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Study.Interface.StudyInterface;
import com.li.knowledgefarm.Study.Util.StudyUtil;
import com.li.knowledgefarm.entity.English;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnglishActivity extends AppCompatActivity implements StudyInterface {
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
    private ImageView isTrue2;
    private Handler getMath;
    private Handler getWAF;
    private Gson gson;
    private List<English> datalist;
    private int position=0;
    private int TrueAnswerNumber = 0;
    private Dialog ifReturn;
    private Boolean returnHandlerFinish = false;
    private TextView answer1;
    private TextView answer2;
    private int displayWidth;
    private int displayHeight;
    private LinearLayout answerA;
    private LinearLayout answerB;
    private TextView trueAnswer;
    private LinearLayout tipText;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english);

        /** 加载视图*/
        getViews();
        setViewSize();
        /** 注册点击事件监听器*/
        registListener();
        StudyUtil.setStatusBar(this);
        datalist = (List<English>) getIntent().getSerializableExtra("english");
        showQuestion(position);
    }

    /**
     * @Description 设置控件大小
     * @Auther 孙建旺
     * @Date 下午 4:11 2019/12/16
     * @Param []
     * @return void
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setViewSize() {
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayWidth = ds.widthPixels;
        displayHeight = ds.heightPixels;

        TextView tip = findViewById(R.id.tip);
        ImageView isTrue = findViewById(R.id.englishIsTrue);
        TextView trans2 = findViewById(R.id.transTwo);
        ImageView isTrue2 = findViewById(R.id.englishIsTrue2);

        RelativeLayout.LayoutParams params_tip = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_tip.setMargins((int)(displayWidth*0.1),(int)(displayHeight*0.2),0,0);
        tipText.setLayoutParams(params_tip);

        LinearLayout.LayoutParams params_tiptext = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_tiptext.gravity = Gravity.CENTER_HORIZONTAL;
        tip.setLayoutParams(params_tiptext);
        tip.setTextSize((int)(displayWidth*0.01));
        tip.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.ShopTextColor));

        LinearLayout.LayoutParams params_isTrue = new LinearLayout.LayoutParams((int)(displayWidth*0.05),(int)(displayHeight*0.1));
        params_isTrue.setMargins(0,0,(int)(displayWidth*0.01),0);
        isTrue.setLayoutParams(params_isTrue);

        LinearLayout.LayoutParams params_trans2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_trans2.setMargins((int)(displayWidth*0.06),0,0,0);
        trans2.setLayoutParams(params_trans2);

        LinearLayout.LayoutParams params_isTrue2 = new LinearLayout.LayoutParams((int)(displayWidth*0.05),(int)(displayHeight*0.1));
        params_isTrue2.setMargins(0,0,0,0);
        isTrue2.setLayoutParams(params_isTrue2);
    }

    /**
     * @Description 设置弹窗控件大小
     * @Auther 孙建旺
     * @Date 下午 4:13 2019/12/18
     * @Param [view]
     * @return void
     */
    private void setDialogSize(View view){
        ImageView cancel = view.findViewById(R.id.cancel_return);
        ImageView sure = view.findViewById(R.id.sure_return);
        TextView warning = view.findViewById(R.id.waringText);
        LinearLayout panduan = view.findViewById(R.id.panduan);

        LinearLayout.LayoutParams params_cancel = new LinearLayout.LayoutParams((int)(displayWidth*0.065),(int)(displayWidth*0.065));
        params_cancel.setMargins(0,0,(int)(displayWidth*0.08),0);
        cancel.setLayoutParams(params_cancel);

        LinearLayout.LayoutParams params_sure = new LinearLayout.LayoutParams((int)(displayWidth*0.065),(int)(displayWidth*0.065));
        sure.setLayoutParams(params_sure);

        warning.setTextSize((int)(displayWidth*0.012));

        LinearLayout.LayoutParams params_layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_layout.setMargins(0,(int)(displayHeight*0.12),0,0);
        panduan.setLayoutParams(params_layout);
    }

    /**
     * @Description  确认是否返回
     * @Auther 景光赞
     * @Date 下午 5:00 2019/12/11
     * @Param []
     * @return void
     */
    @Override
    public void showIfReturn(){
        ifReturn = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.math_return_dialog,null);
        ImageView cancel = layout.findViewById(R.id.cancel_return);
        ImageView sure = layout.findViewById(R.id.sure_return);
        setDialogSize(layout);
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
        Window dialogWindow = ifReturn.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * @Description 获得水和肥料成功
     * @Auther 景光赞
     * @Date 下午 4:58 2019/12/11
     * @Param []
     * @return void
     */
    @Override
    @SuppressLint("HandlerLeak")
    public void getWandFCallBack(){
        getWAF = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = (String)msg.obj;
                if(data!= null){
                    if(!data.equals("-1")){
                        LoginActivity.user.setEnglishRewardCount(LoginActivity.user.getEnglishRewardCount() - 1);
                        answer1.setVisibility(View.INVISIBLE);
                        answer2.setVisibility(View.INVISIBLE);
                        isFalse.setVisibility(View.INVISIBLE);
                        tipText.setVisibility(View.GONE);
                        isTrue.setVisibility(View.GONE);
                        isTrue2.setVisibility(View.GONE);
                        btnNextQuestion.setVisibility(View.GONE);
                        btnPreQuestion.setVisibility(View.GONE);
                        question.setText("你获得了水和肥料哦，快去照顾你的植物吧！");
                        question.setTextSize((int)(displayWidth*0.011));
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
     * @Auther 景光赞
     * @Date 下午 2:17 2019/12/11
     * @Param []
     * @return void
     */
    @Override
    public void getWaterAndFertilizer(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("userId", LoginActivity.user.getId()+"")
                        .add("water",TrueAnswerNumber*1+"")
                        .add("fertilizer",TrueAnswerNumber*1+"")
                        .add("subject","english").build();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/user/lessRewardCount").post(formBody).build();
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
     * @Auther 景光赞
     * @Date 上午 11:33 2019/12/11
     * @Param [pos]
     * @return void
     */
    @Override
    public void showQuestion(int pos){
        if(position == datalist.size()-1){
            btnNextQuestion.setText("我答完啦");
        }else{
            btnNextQuestion.setText("下一题");
        }
        if(!datalist.get(pos).getIfDone().equals("true")) {
            isFalse.setText("");
            answerA.setVisibility(View.VISIBLE);
            answerB.setVisibility(View.VISIBLE);
            trueAnswer.setVisibility(View.GONE);
            isTrue.setVisibility(View.INVISIBLE);
            isTrue2.setVisibility(View.INVISIBLE);
            question.setText(datalist.get(pos).getWord());
            if(new Random().nextInt(2) == 0) {
                answer1.setText(datalist.get(pos).getTrans());
                String trans = null;
                do{
                    trans = datalist.get(new Random().nextInt(datalist.size())).getTrans();
                }while (trans.equals(datalist.get(pos).getTrans()) && trans != null);
                answer2.setText(trans);
            }else{
                answer2.setText(datalist.get(pos).getTrans());
                String trans = null;
                do{
                    trans = datalist.get(new Random().nextInt(datalist.size())).getTrans();
                }while (trans.equals(datalist.get(pos).getTrans()) && trans != null);
                answer1.setText(trans);
            }
        }else {
            isTrue.setVisibility(View.VISIBLE);
            answerA.setVisibility(View.GONE);
            answerB.setVisibility(View.GONE);
            trueAnswer.setVisibility(View.VISIBLE);
            question.setText(datalist.get(pos).getWord());
            trueAnswer.setText(datalist.get(pos).getTrans());

            LinearLayout.LayoutParams params_trueanswer = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params_trueanswer.gravity = Gravity.CENTER_HORIZONTAL;
            trueAnswer.setLayoutParams(params_trueanswer);
            trueAnswer.setTextSize((int)(displayWidth*0.02));
            trueAnswer.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.ShopTextColor));
        }
    }

    class CustomerListener implements View.OnClickListener{

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_return:
                    if(TrueAnswerNumber>0 && TrueAnswerNumber<datalist.size() && LoginActivity.user.getEnglishRewardCount()>0)
                        showIfReturn();
                    else
                        finish();
                    break;
                case R.id.btnPreEnglish:
                    if((position-1)>=0) {
                        position = --position;
                        showQuestion(position);
                    }
                    break;
                case R.id.transOne:
                    String t1 = answer1.getText().toString().trim();
                    if(t1.equals(datalist.get(position).getTrans())){
                        TrueAnswerNumber++;
                        isTrue.setImageDrawable(getResources().getDrawable(R.drawable.duigou,null));
                        isTrue.setVisibility(View.VISIBLE);
                        isTrue2.setVisibility(View.INVISIBLE);
                        isFalse.setText("答对啦！获得了奖励哦！");
                        StudyUtil.PlayTrueSound(getApplicationContext());
                        isFalse.setVisibility(View.VISIBLE);
                        if((position+1)<=datalist.size()-1) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    datalist.get(position).setIfDone("true");
                                    position = ++position;
                                    showQuestion(position);
                                }
                            }, 1000);
                        }
                    }else{
                        isTrue.setImageDrawable(getResources().getDrawable(R.drawable.cha,null));
                        isTrue.setVisibility(View.VISIBLE);
                        isFalse.setText("哎呀，选错了！");
                        StudyUtil.PlayFalseSound(getApplicationContext());
                        isFalse.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.transTwo:
                    String t2 = answer2.getText().toString().trim();
                    if(t2.equals(datalist.get(position).getTrans())){
                        datalist.get(position).setIfDone("true");
                        TrueAnswerNumber++;
                        isTrue2.setImageDrawable(getResources().getDrawable(R.drawable.duigou,null));
                        isTrue2.setVisibility(View.VISIBLE);
                        isTrue.setVisibility(View.INVISIBLE);
                        isFalse.setText("答对啦！获得了奖励哦！");
                        StudyUtil.PlayTrueSound(getApplicationContext());
                        isFalse.setVisibility(View.VISIBLE);
                        if((position+1)<=datalist.size()-1) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    datalist.get(position).setIfDone("true");
                                    position = ++position;
                                    showQuestion(position);
                                }
                            }, 1000);
                        }
                    }else{
                        isTrue2.setImageDrawable(getResources().getDrawable(R.drawable.cha,null));
                        isTrue2.setVisibility(View.VISIBLE);
                        isFalse.setText("哎呀，选错了！");
                        StudyUtil.PlayFalseSound(getApplicationContext());
                        isFalse.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btnNextEnglish:
                    if((position+1)<=datalist.size()-1) {
                        position = ++position;
                        showQuestion(position);
                    }else{
                        if(TrueAnswerNumber < datalist.size()){
                            Toast.makeText(EnglishActivity.this,"你还没有答完哦",Toast.LENGTH_SHORT).show();;
                        }else {
                            getWandFCallBack();
                            getWaterAndFertilizer();
                            btnNextQuestion.setClickable(false);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 加载视图
     */
    @Override
    public void getViews(){
        iv_return = findViewById(R.id.iv_return);
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        datalist = new ArrayList<>();
        btnPreQuestion = findViewById(R.id.btnPreEnglish);
        btnNextQuestion = findViewById(R.id.btnNextEnglish);
        question = findViewById(R.id.englishQuestion);
        answer1 = findViewById(R.id.transOne);
        answer2 = findViewById(R.id.transTwo);
        isTrue = findViewById(R.id.englishIsTrue);
        isTrue2 = findViewById(R.id.englishIsTrue2);
        isFalse = findViewById(R.id.englishIsFalse);
        answerA = findViewById(R.id.AnswerA);
        answerB = findViewById(R.id.AnswerB);
        trueAnswer = findViewById(R.id.trueAnswer);
        tipText = findViewById(R.id.tipText);
    }

    /**
     * 注册点击事件监听器
     */
    @Override
    public void registListener(){
        listener = new CustomerListener();
        iv_return.setOnClickListener(listener);
        btnPreQuestion.setOnClickListener(listener);
        btnNextQuestion.setOnClickListener(listener);
        answer1.setOnClickListener(listener);
        answer2.setOnClickListener(listener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if(TrueAnswerNumber>0 && TrueAnswerNumber<datalist.size() && LoginActivity.user.getMathRewardCount()>0)
            showIfReturn();
        else
            finish();
    }

}
