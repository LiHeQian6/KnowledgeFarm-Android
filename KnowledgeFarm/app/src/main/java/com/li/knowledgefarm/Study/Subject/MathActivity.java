package com.li.knowledgefarm.Study.Subject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Study.Interface.StudyInterface;
import com.li.knowledgefarm.Study.Util.StudyUtil;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.QuestionEntity.Completion;
import com.li.knowledgefarm.entity.QuestionEntity.Judgment;
import com.li.knowledgefarm.entity.QuestionEntity.Question;
import com.li.knowledgefarm.entity.QuestionEntity.Question3Num;
import com.li.knowledgefarm.entity.QuestionEntity.SingleChoice;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathActivity extends AppCompatActivity implements StudyInterface {
    private ImageView iv_return; //返回
    private CustomerListener listener; //自定义点击事件监听器
    private OkHttpClient okHttpClient; //Okhttp
    private TextView btnPreQuestion; //下一题
    private TextView btnNextQuestion; //上一题
    private Handler getWAF; //接收增加水和肥料结果
    private Gson gson; //Gson
    private List<Question> datalist; //题目List
    private int position=0; //题目位置
    private int TrueAnswerNumber = 0; //回答正确计数器
    private Dialog ifReturn; //询问是否返回弹窗
    private Boolean returnHandlerFinish = false; //返回条件
    private int displayWidth; //屏幕宽度
    private int displayHeight; //屏幕高度
    private TextView number_tip;//显示回答正确数量及题目总数

    private View completion_layout; //填空题布局
    private View judgement_layout; //判断题布局
    private View choice_layout; //选择题布局

    //填空题
    private TextView completion_question; //填空题问题
    private TextView isFalse; //回答是否错误文字提示
    private ImageView isTrue; //回答是否正确图片提示
    private EditText completion_answer; //填空题答案输入框

    //选择题
    private TextView choice_question; //选择题问题
    private TextView choice_A; //A选项
    private TextView choice_B; //B选项
    private TextView choice_C; //C选项
    private ImageView choice_isTrue; //是否选择正确提示

    //判断题
    private TextView judge_question;//判断题问题
    private TextView judge_A; //A选项
    private TextView judge_B; //B选项
    private ImageView judge_isTrue; //是否判断正确提示

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        okHttpClient = OkHttpUtils.getInstance(this);
        /** 加载视图*/
        getViews();
        setViewSize();
        if(UserUtil.getUser().getGrade() == 1 || UserUtil.getUser().getGrade() == 2){
//            thisGrade = getLayoutInflater().inflate(R.layout.math_grade_one,null);
            datalist = (List<Question>) getIntent().getSerializableExtra("math");
        }else if(UserUtil.getUser().getGrade() ==2 || UserUtil.getUser().getGrade() == 3){

        }
        /** 注册点击事件监听器*/
        registListener();
        FullScreen.NavigationBarStatusBar(MathActivity.this,true);
        showQuestion(position);
    }

    /**
     * 加载视图
     */
    @Override
    public void getViews(){
        iv_return = findViewById(R.id.iv_return);
        gson = new Gson();
        datalist = new ArrayList<>();
        btnPreQuestion = findViewById(R.id.btnPreQuestion);
        btnNextQuestion = findViewById(R.id.btnNextQuestion);
        number_tip = findViewById(R.id.number_tip);
        //填空题布局
        completion_layout = findViewById(R.id.completion_layout);
        //选择题布局
        choice_layout = findViewById(R.id.choice_layout);
        //判断题布局
        judgement_layout = findViewById(R.id.judge_layout);
        //填空题
        completion_question = findViewById(R.id.completion_Question); //填空题问题
        completion_answer = findViewById(R.id.completion_Answer); //填空题答案输入框
        isTrue = findViewById(R.id.isTrue); //是否正确文字提示
        isFalse = findViewById(R.id.isFalse); //是否正确图片提示
        //选择题
        choice_question = findViewById(R.id.choice_Question);
        choice_isTrue = findViewById(R.id.choice_isTrue);
        choice_A = findViewById(R.id.choice_A);
        choice_B = findViewById(R.id.choice_B);
        choice_C = findViewById(R.id.choice_C);
        //判断题
        judge_question = findViewById(R.id.judge_Question);
        judge_isTrue = findViewById(R.id.judge_isTrue);
        judge_A = findViewById(R.id.judge_A);
        judge_B = findViewById(R.id.judge_B);
    }

    /**
     * @Description 展示判断题
     * @Author 孙建旺
     * @Date 下午5:36 2020/05/12
     * @Param []
     * @return void
     */
    private void ShowJudgement(int pos){
        judgement_layout.setVisibility(View.VISIBLE);
        choice_layout.setVisibility(View.GONE);
        completion_layout.setVisibility(View.GONE);
        if(position == datalist.size()-1){
            btnNextQuestion.setText("我答完啦");
        }else{
            btnNextQuestion.setText("下一题");
        }
        if(datalist.get(pos).getIfDone() == 1){
            judge_isTrue.setVisibility(View.GONE);
            judge_A.setVisibility(View.GONE);
            judge_B.setText(((Judgment)datalist.get(pos)).getAnswer());
        }else {
            judge_isTrue.setVisibility(View.GONE);
            judge_A.setVisibility(View.VISIBLE);
            switch (new Random().nextInt(1)){
                case 0:
                    judge_A.setText(((Judgment)datalist.get(pos)).getAnswer()+"");
                    judge_B.setText(((Judgment)datalist.get(pos)).getChoice()+"");
                    break;
                case 1:
                    judge_B.setText(((Judgment)datalist.get(pos)).getAnswer()+"");
                    judge_A.setText(((Judgment)datalist.get(pos)).getChoice()+"");
                    break;
            }
        }
    }

    /**
     * @Description 展示单选题
     * @Author 孙建旺
     * @Date 下午5:35 2020/05/12
     * @Param [pos]
     * @return void
     */
    private void ShowSingleChoice(int pos){
        choice_layout.setVisibility(View.VISIBLE);
        completion_layout.setVisibility(View.GONE);
        judgement_layout.setVisibility(View.GONE);
        if(position == datalist.size()-1){
            btnNextQuestion.setText("我答完啦");
        }else{
            btnNextQuestion.setText("下一题");
        }
        if(datalist.get(pos).getIfDone() == 1){
            choice_isTrue.setVisibility(View.GONE);
            choice_A.setVisibility(View.INVISIBLE);
            choice_C.setVisibility(View.INVISIBLE);
            choice_B.setText(((SingleChoice)datalist.get(pos)).getAnswer());
        }else {
            choice_A.setVisibility(View.VISIBLE);
            choice_C.setVisibility(View.VISIBLE);
            choice_isTrue.setVisibility(View.INVISIBLE);
            switch (new Random().nextInt(2)){
                case 0:
                    choice_A.setText(((SingleChoice)datalist.get(pos)).getAnswer());
                    choice_B.setText(((SingleChoice)datalist.get(pos)).getChoice2());
                    choice_C.setText(((SingleChoice)datalist.get(pos)).getChoice1());
                    break;
                case 1:
                    choice_B.setText(((SingleChoice)datalist.get(pos)).getAnswer());
                    choice_A.setText(((SingleChoice)datalist.get(pos)).getChoice2());
                    choice_C.setText(((SingleChoice)datalist.get(pos)).getChoice1());
                    break;
                case 2:
                    choice_C.setText(((SingleChoice)datalist.get(pos)).getAnswer());
                    choice_A.setText(((SingleChoice)datalist.get(pos)).getChoice2());
                    choice_B.setText(((SingleChoice)datalist.get(pos)).getChoice1());
                    break;
            }
        }
    }

    /**
     * @Description 展示填空题
     * @Author 孙建旺
     * @Date 下午5:31 2020/05/12
     * @Param [pos]
     * @return void
     */
    private void ShowCompletion(int pos){
        completion_layout.setVisibility(View.VISIBLE);
        choice_layout.setVisibility(View.GONE);
        judgement_layout.setVisibility(View.GONE);
        number_tip.setText(pos+1 + " / " + datalist.size());
        btnNextQuestion.setClickable(true);
        if(position == datalist.size()-1){
            btnNextQuestion.setText("我答完啦");
        }else{
            btnNextQuestion.setText("下一题");
        }
        if(datalist.get(pos).getIfDone() == 1) {
            isFalse.setText(" ");
            isTrue.setVisibility(View.INVISIBLE);
            completion_answer.setVisibility(View.GONE);
            Completion completion = (Completion) datalist.get(pos);
            completion_question.setText(datalist.get(pos).getQuestionTitle().getTitle()+ completion.getAnswer());
        }else{
            if(completion_answer.getVisibility() == View.INVISIBLE){
                completion_answer.setVisibility(View.VISIBLE);
            }
            isFalse.setText("");
            isTrue.setVisibility(View.INVISIBLE);
            completion_answer.setVisibility(View.VISIBLE);
            completion_question.setText(datalist.get(pos).getQuestionTitle().getTitle());
        }
    }

    /**
     * @Description 展示下一道题目
     * @Auther 孙建旺
     * @Date 上午 11:33 2019/12/11
     * @Param [pos]
     * @return void
     */
    public void showQuestion(int pos){
        switch (datalist.get(pos).getQuestionType().getId()){
            case 1:
                ShowSingleChoice(pos);
                break;
            case 2:
                ShowCompletion(pos);
                break;
            case 3:
                ShowJudgement(pos);
                break;
        }
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
//        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics ds = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(ds);
//        displayWidth = ds.widthPixels;
//        displayHeight = ds.heightPixels;
//
//        TextView btnPre = findViewById(R.id.btnPreQuestion);
//        TextView btnNext = findViewById(R.id.btnNextQuestion);
//        RelativeLayout question  = findViewById(R.id.relative_question);
//
//        LinearLayout.LayoutParams params_btn = new LinearLayout.LayoutParams((int)(displayWidth*0.2),(int)(displayHeight*0.1));
//        params_btn.gravity = Gravity.CENTER_HORIZONTAL;
//        btnPre.setLayoutParams(params_btn);
//        params_btn.setMargins(0,0,0,(int)(displayHeight*0.02));
//        btnPre.setTextSize((int)(displayWidth*0.015));
//        btnPre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        btnNext.setLayoutParams(params_btn);
//        btnNext.setTextSize((int)(displayWidth*0.015));
//        btnNext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//
//        RelativeLayout.LayoutParams params_question = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)(displayHeight*0.3));
//        params_question.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        params_question.bottomMargin = (int)(displayHeight*0.2);
//        question.setLayoutParams(params_question);
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
     * @Auther 孙建旺
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
     * @Auther 孙建旺
     * @Date 下午 4:58 2019/12/11
     * @Param []
     * @return void
     */
    @Override
    public void getWandFCallBack(){
        getWAF = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = (String)msg.obj;
                if(data!= null){
                    if(!data.equals("-1")){
                        UserUtil.getUser().setMathRewardCount(UserUtil.getUser().getMathRewardCount() - 1);
                        completion_answer.setVisibility(View.GONE);
                        isFalse.setVisibility(View.INVISIBLE);
                        isTrue.setVisibility(View.GONE);
                        btnPreQuestion.setVisibility(View.GONE);
                        btnNextQuestion.setVisibility(View.GONE);
                        completion_question.setText("你获得了水和肥料哦，快去照顾你的植物吧！");
                        completion_question.setTextSize((int)(displayWidth*0.011));
                        completion_question.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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
    @Override
    public void getWaterAndFertilizer(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("water",TrueAnswerNumber*1+"")
                        .add("fertilizer",TrueAnswerNumber*1+"")
                        .add("subject","math").build();
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
                        OkHttpUtils.unauthorized(response.code());
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        getWAF.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    class CustomerListener implements View.OnClickListener{

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_return:
                    if ((TrueAnswerNumber > 0 && TrueAnswerNumber < datalist.size() && UserUtil.getUser().getMathRewardCount() > 0)) {
                        showIfReturn();
                    } else {
                        finish();
                    }
                    break;
                case R.id.btnPreQuestion:
                    if((position-1)>=0) {
                        completion_answer.setText("");
                        position = --position;
                        showQuestion(position);
                    }
                    break;
                case R.id.btnNextQuestion:
                    String inputRes = completion_answer.getText().toString().trim();
                    if(inputRes.equals("")) {
                        completion_answer.setText("");
                        if((position+1)<=datalist.size()-1) {
                            position = ++position;
                            showQuestion(position);
                        }else{
                            if(TrueAnswerNumber < datalist.size()){
                                Toast.makeText(MathActivity.this,"你还没有答完哦",Toast.LENGTH_SHORT).show();;
                            }
                        }
                        return;
                    }
                    if(inputRes.equals(((Completion)datalist.get(position)).getAnswer()+"")) {
                        TrueAnswerNumber++;
                        isTrue.setImageDrawable(getResources().getDrawable(R.drawable.duigou,null));
                        isTrue.setVisibility(View.VISIBLE);
                        isFalse.setText("答对啦！获得了奖励哦！");
                        isFalse.setVisibility(View.VISIBLE);
                        btnNextQuestion.setClickable(false);
                        StudyUtil.PlayTrueSound(getApplicationContext());
                        if((position+1)<=datalist.size()-1) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    completion_answer.setText("");
                                    datalist.get(position).setIfDone(1);
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
                        StudyUtil.PlayFalseSound(getApplicationContext());
                        isFalse.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    /**
     * 注册点击事件监听器
     */
    @Override
    public void registListener() {
        listener = new CustomerListener();
        iv_return.setOnClickListener(listener);
        btnPreQuestion.setOnClickListener(listener);
        btnNextQuestion.setOnClickListener(listener);
        completion_answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (position == datalist.size() - 1)
                    btnNextQuestion.setText("我做完啦 ");
            }
        });
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
        if(TrueAnswerNumber>0 && TrueAnswerNumber<datalist.size() && UserUtil.getUser().getMathRewardCount()>0)
            showIfReturn();
        else
            finish();
    }
}
