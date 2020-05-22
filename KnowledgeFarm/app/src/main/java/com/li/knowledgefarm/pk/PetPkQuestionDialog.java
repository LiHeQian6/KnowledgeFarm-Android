package com.li.knowledgefarm.pk;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.entity.QuestionEntity.Completion;
import com.li.knowledgefarm.entity.QuestionEntity.Judgment;
import com.li.knowledgefarm.entity.QuestionEntity.Question;
import com.li.knowledgefarm.entity.QuestionEntity.SingleChoice;

import java.util.Random;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

public class PetPkQuestionDialog extends Dialog {

    private OnAnswerSelectListener listener;
    private QuestionTimeLimit timeLimit;//倒计时
    private TextView time_limit;//倒计时文字提示
    private Button battle; //提交按钮
    private Question question;//问题
    private long startTime = 0L;
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
    private CheckBox checkBox_A;//A选项单选框
    private CheckBox checkBox_B;//B选项单选框
    private CheckBox checkBox_C;//C选项单选框

    //判断题
    private TextView judge_question;//判断题问题
    private TextView judge_A; //A选项
    private TextView judge_B; //B选项
    private ImageView judge_isTrue; //是否判断正确提示

    public PetPkQuestionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_question_layout);
        startTime = System.currentTimeMillis();
        getViews();
        showQuestion();
        registerListener();
    }

    @Override
    protected void onStart() {
        checkBox_A.setChecked(false);
        checkBox_B.setChecked(false);
        checkBox_C.setChecked(false);
        completion_answer.setText("");
        timeLimit = new QuestionTimeLimit(time_limit,listener,this,startTime);
        timeLimit.execute();
        super.onStart();
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public void show() {
        super.show();
        setDialogSize();
    }

    private void setDialogSize(){
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int)(0.7*ds.widthPixels);
        params.height = (int)(0.95*ds.heightPixels);
        this.getWindow().setAttributes(params);
        this.setCancelable(false);
    }

    /**
     * @Description 判断判断题是否正确
     * @Author 孙建旺
     * @Date 上午10:15 2020/05/20
     * @Param []
     * @return void
     */
    private void judgementIfTrue(){
        String choose_answer = "";
        if(checkBox_A.isChecked())
            choose_answer = choice_A.getText().toString();
        if(checkBox_B.isChecked())
            choose_answer = choice_B.getText().toString();
        if(checkBox_C.isChecked())
            choose_answer = choice_C.getText().toString();
    }

    /**
     * @Description 判断单选题是否正确
     * @Author 孙建旺
     * @Date 上午9:55 2020/05/20
     * @Param []
     * @return void
     */
    private void choiceIfTrue(){
        String choose_answer = "";
        if(checkBox_A.isChecked())
            choose_answer = choice_A.getText().toString();
        if(checkBox_B.isChecked())
            choose_answer = choice_B.getText().toString();
        if(checkBox_C.isChecked())
            choose_answer = choice_C.getText().toString();
        if(choose_answer.equals(((SingleChoice)question).getAnswer())){
            listener.select(true,System.currentTimeMillis()-startTime);
            timeLimit.cancel(true);
            dismiss();
        }else {
            listener.select(false,System.currentTimeMillis()-startTime);
            timeLimit.cancel(true);
            dismiss();
        }
    }

    /**
     * @Description 判断填空题是否正确
     * @Author 孙建旺
     * @Date 上午9:48 2020/05/20
     * @Param []
     * @return void
     */
    private void completionIfTrue(){
        String your_answer = completion_answer.getText().toString().trim();
        if(your_answer.equals(((Completion)question).getAnswer())){
            listener.select(true,System.currentTimeMillis()-startTime);
            timeLimit.cancel(true);
            dismiss();
        }else{
            listener.select(false,System.currentTimeMillis()-startTime);
            timeLimit.cancel(true);
            dismiss();
        }
    }

    /**
     * @Description 展示判断题
     * @Author 孙建旺
     * @Date 下午7:56 2020/05/19
     * @Param []
     * @return void
     */
    private void showJudgement(){
        judge_question.setText(question.getQuestionTitle().getTitle());
        switch (new Random().nextInt(2)){
            case 0:
                judge_A.setText(((Judgment)question).getAnswer()+"");
                judge_B.setText(((Judgment)question).getChoice()+"");
                break;
            case 1:
                judge_B.setText(((Judgment)question).getAnswer()+"");
                judge_A.setText(((Judgment)question).getChoice()+"");
                break;
        }
    }

    /**
     * @Description 展示单选题
     * @Author 孙建旺
     * @Date 下午7:53 2020/05/19
     * @Param []
     * @return void
     */
    private void showChoice(){
        choice_question.setText(question.getQuestionTitle().getTitle());
        switch (new Random().nextInt(3)){
            case 0:
                choice_A.setText(((SingleChoice)question).getAnswer());
                choice_B.setText(((SingleChoice)question).getChoice2());
                choice_C.setText(((SingleChoice)question).getChoice1());
                break;
            case 1:
                choice_B.setText(((SingleChoice)question).getAnswer());
                choice_A.setText(((SingleChoice)question).getChoice2());
                choice_C.setText(((SingleChoice)question).getChoice1());
                break;
            case 2:
                choice_C.setText(((SingleChoice)question).getAnswer());
                choice_A.setText(((SingleChoice)question).getChoice2());
                choice_B.setText(((SingleChoice)question).getChoice1());
                break;
        }
    }

    /**
     * @Description 展示填空题
     * @Author 孙建旺
     * @Date 下午7:53 2020/05/19
     * @Param []
     * @return void
     */
    private void showCompletion(){
        completion_question.setText(question.getQuestionTitle().getTitle());
    }

    /**
     * @Description 展示问题
     * @Author 孙建旺
     * @Date 下午7:49 2020/05/19
     * @Param []
     * @return void
     */
    private void showQuestion() {
        switch (question.getQuestionType().getId()){
            case 1:
                choice_layout.setVisibility(View.VISIBLE);
                completion_layout.setVisibility(View.GONE);
                judgement_layout.setVisibility(View.GONE);
                showChoice();
                break;
            case 2:
                choice_layout.setVisibility(View.GONE);
                completion_layout.setVisibility(View.VISIBLE);
                judgement_layout.setVisibility(View.GONE);
                showCompletion();
                break;
//            case 3:
//                choice_layout.setVisibility(View.GONE);
//                completion_layout.setVisibility(View.GONE);
//                judgement_layout.setVisibility(View.VISIBLE);
//                showJudgement();
//                break;
        }
    }

    /**
     * @Description 获取控件Id
     * @Author 孙建旺
     * @Date 下午6:37 2020/05/19
     * @Param []
     * @return void
     */
    private void getViews() {

        battle = findViewById(R.id.commit_battle);
        time_limit = findViewById(R.id.time_limit);
        //填空题布局
        completion_layout = findViewById(R.id.battle_completion_layout);
        //选择题布局
        choice_layout = findViewById(R.id.battle_choice_layout);
        //判断题布局
        judgement_layout = findViewById(R.id.battle_judge_layout);
        //填空题
        completion_question = findViewById(R.id.completion_Question); //填空题问题
        completion_answer = findViewById(R.id.completion_Answer); //填空题答案输入框
        isTrue = findViewById(R.id.isTrue); //是否正确文字提示
//        isFalse = findViewById(R.id.isFalse); //是否正确图片提示
        //选择题
        choice_question = findViewById(R.id.choice_Question);
        choice_isTrue = findViewById(R.id.choice_isTrue);
        choice_isTrue.setVisibility(View.INVISIBLE);
        choice_A = findViewById(R.id.choice_A);
        choice_B = findViewById(R.id.choice_B);
        choice_C = findViewById(R.id.choice_C);
        checkBox_A = findViewById(R.id.checkbox_A);
        checkBox_B = findViewById(R.id.checkbox_B);
        checkBox_C = findViewById(R.id.checkbox_C);
//        //判断题
//        judge_question = findViewById(R.id.judge_Question);
//        judge_isTrue = findViewById(R.id.judge_isTrue);
//        judge_A = findViewById(R.id.judge_A);
//        judge_B = findViewById(R.id.judge_B);
    }

    /**
     * @Description 注册监听器
     * @Author 孙建旺
     * @Date 上午10:09 2020/05/20
     * @Param []
     * @return void
     */
    private void registerListener(){
        battle.setOnClickListener(new CustomerOnclickListener());
        checkBox_A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBox_B.setChecked(false);
                    checkBox_C.setChecked(false);
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
                }
            }
        });
    }

    /**
     * @Description 自定义监听器
     * @Author 孙建旺
     * @Date 下午8:04 2020/05/19
     * @Param [listener]
     * @return void
     */
    public void setOnAnswerSelectListener(OnAnswerSelectListener listener){
        this.listener = listener;
    }

    private class CustomerOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.commit_battle:
                    switch (question.getQuestionType().getId()){
                        case 1:
                            choiceIfTrue();
                            break;
                        case 2:
                            completionIfTrue();
                            break;
                    }
                    break;
            }
        }
    }

    public static interface OnAnswerSelectListener {
        public void select(boolean isRight,long time);
    }
}