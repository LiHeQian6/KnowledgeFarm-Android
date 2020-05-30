package com.li.knowledgefarm.Study.Subject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Study.Interface.StudyInterface;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.QuestionEntity.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements StudyInterface {
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
    private Toast toast;
    private TextView number_tip;//显示回答正确数量及题目总数

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
    private CheckBox checkBox_A;//A选项单选框
    private CheckBox checkBox_B;//B选项单选框
    private CheckBox checkBox_C;//C选项单选框

    //判断题
    private TextView judge_question;//判断题问题
    private TextView judge_A; //A选项
    private TextView judge_B; //B选项
    private CheckBox judgeBox_A; //A选项单选框
    private CheckBox judgeBox_B; //B选项单选框
    private ImageView judge_isTrue; //是否判断正确提示

    private QuestionUtil questionUtil;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        okHttpClient = OkHttpUtils.getInstance(this);
        /** 加载视图*/
        getViews();
        setViewSize();
        datalist = (List<Question>) getIntent().getSerializableExtra("question");
        QuestionUtil.CURRENT_SUBJECT = datalist.get(0).getSubject();
        questionUtil = new QuestionUtil(this, QuestionActivity.this,datalist);
        /** 注册点击事件监听器*/
        registListener();
        FullScreen.NavigationBarStatusBar(QuestionActivity.this,true);
        questionUtil.showQuestion();
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
        //填空题
        completion_question = findViewById(R.id.completion_Question); //填空题问题
        completion_answer = findViewById(R.id.completion_Answer); //填空题答案输入框
        isTrue = findViewById(R.id.isTrue); //是否正确文字提示
//        isFalse = findViewById(R.id.isFalse); //是否正确图片提示
        //选择题
        choice_question = findViewById(R.id.choice_Question);
        choice_isTrue = findViewById(R.id.choice_isTrue);
        choice_A = findViewById(R.id.choice_A);
        choice_B = findViewById(R.id.choice_B);
        choice_C = findViewById(R.id.choice_C);
        checkBox_A = findViewById(R.id.checkbox_A);
        checkBox_B = findViewById(R.id.checkbox_B);
        checkBox_C = findViewById(R.id.checkbox_C);
        //判断题
        judge_question = findViewById(R.id.judge_Question);
        judge_isTrue = findViewById(R.id.judge_isTrue);
        judge_A = findViewById(R.id.judge_A);
        judge_B = findViewById(R.id.judge_B);
        judgeBox_A = findViewById(R.id.judge_box_A);
        judgeBox_B = findViewById(R.id.judge_box_B);
    }

    /**
     * @Description 设置控件适配屏幕
     * @Auther 孙建旺
     * @Date 上午 8:12 2019/12/16
     * @Param []
     * @return void
     */
    private void setViewSize() {
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayWidth = ds.widthPixels;
        displayHeight = ds.heightPixels;
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
        checkBox_A.setWidth((int)(displayWidth * 0.1));
        checkBox_B.setWidth((int)(displayWidth * 0.1));
        checkBox_C.setWidth((int)(displayWidth * 0.1));
        choice_A.setWidth((int)(displayWidth * 0.25));
        choice_B.setWidth((int)(displayWidth * 0.25));
        choice_C.setWidth((int)(displayWidth * 0.25));
        completion_question.setWidth((int)(displayWidth * 0.7));
    }

    class CustomerListener implements View.OnClickListener{

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_return:
                    int reward_number = 0;
                    switch (QuestionUtil.CURRENT_SUBJECT){
                        case "Math":
                            reward_number = UserUtil.getUser().getMathRewardCount();
                            break;
                        case "Chinese":
                            reward_number = UserUtil.getUser().getChineseRewardCount();
                            break;
                        case "English":
                            reward_number = UserUtil.getUser().getEnglishRewardCount();
                            break;
                    }
                    if ((QuestionUtil.TRUE_ANSWER_COUNT > 0 && QuestionUtil.TRUE_ANSWER_COUNT < datalist.size() && reward_number > 0)) {
                        questionUtil.showIfReturn();
                    } else {
                        finish();
                    }
                    break;
                case R.id.btnPreQuestion:
                    if((QuestionUtil.POSITION - 1)>=0) {
                        completion_answer.setText("");
                        QuestionUtil.PositionLess();
                        questionUtil.showQuestion();
                    }
                    if(QuestionUtil.POSITION == datalist.size() - 1) {
                        btnNextQuestion.setText("我答完啦");
                    }else
                        btnNextQuestion.setText("下一题");
                    break;
                case R.id.btnNextQuestion:
                    if(QuestionUtil.TRUE_ANSWER_COUNT == datalist.size()){
                        questionUtil.getWaterAndFertilizer();
                        return;
                    }else {
                        switch (datalist.get(QuestionUtil.POSITION).getQuestionType().getId()){
                            case 3:
                            case 1:
                                if(QuestionUtil.POSITION < datalist.size() - 1) {
                                    QuestionUtil.PositionAdd();
                                    questionUtil.showQuestion();
                                }
                                break;
                            case 2:
                                questionUtil.CompletionIfTrue();
                                break;
                        }
                        if(btnNextQuestion.getText().toString().equals("我答完啦"))
                            CustomerToast.getInstance(QuestionActivity.this,"你还没有答完哦！",Toast.LENGTH_SHORT).show();
                    }
                    if(QuestionUtil.POSITION == datalist.size() - 1) {
                        btnNextQuestion.setText("我答完啦");
                    }else
                        btnNextQuestion.setText("下一题");
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
        choice_A.setOnClickListener(new CustomerListener());
        choice_B.setOnClickListener(new CustomerListener());
        choice_C.setOnClickListener(new CustomerListener());
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
        checkBox_A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBox_B.setChecked(false);
                    checkBox_C.setChecked(false);
                    btnPreQuestion.setClickable(false);
                    btnNextQuestion.setClickable(false);
                    questionUtil.ChoiceIfTrue();
                }
            }
        });
        checkBox_B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBox_A.setChecked(false);
                    checkBox_C.setChecked(false);
                    btnPreQuestion.setClickable(false);
                    btnNextQuestion.setClickable(false);
                    questionUtil.ChoiceIfTrue();
                }
            }
        });
        checkBox_C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBox_A.setChecked(false);
                    checkBox_B.setChecked(false);
                    btnPreQuestion.setClickable(false);
                    btnNextQuestion.setClickable(false);
                    questionUtil.ChoiceIfTrue();
                }
            }
        });
        judgeBox_A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    judgeBox_B.setChecked(false);
                    btnPreQuestion.setClickable(false);
                    btnNextQuestion.setClickable(false);
                    questionUtil.JudgementIfTrue();
                }
            }
        });

        judgeBox_B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    judgeBox_A.setChecked(false);
                    btnPreQuestion.setClickable(false);
                    btnNextQuestion.setClickable(false);
                    questionUtil.JudgementIfTrue();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            questionUtil.exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
