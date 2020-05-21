package com.li.knowledgefarm.Study.Subject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Study.Util.StudyUtil;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.QuestionEntity.Completion;
import com.li.knowledgefarm.entity.QuestionEntity.Judgment;
import com.li.knowledgefarm.entity.QuestionEntity.Question;
import com.li.knowledgefarm.entity.QuestionEntity.SingleChoice;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionUtil {

    public static int POSITION = 0; //
    public static int TRUE_ANSWER_COUNT = 0;
    public static String CURRENT_SUBJECT;

    private Context context;
    private Activity activity;
    private List<Question> datalist; //题目List
    private OkHttpClient okHttpClient;
    private Handler getWAF; //接收增加水和肥料结果
    private Boolean returnHandlerFinish = false; //返回条件
    private Toast toast;
    private Dialog ifReturn; //询问是否返回弹窗
    private View completion_layout; //填空题布局
    private View judgement_layout; //判断题布局
    private View choice_layout; //选择题布局
    private TextView btnPreQuestion; //下一题
    private TextView btnNextQuestion; //上一题
    private TextView number_tip;//显示回答正确数量及题目总数
    private TextView finish_do; //获得奖励提示
    private TextView your_answer;
    //填空题
    private TextView completion_question; //填空题问题
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

    public QuestionUtil(Context context, Activity activity, List<Question> datalist) {
        this.context = context;
        this.activity = activity;
        this.datalist = datalist;
        POSITION = 0;
        TRUE_ANSWER_COUNT = 0;
        getViews();
    }

    /**
     * @Description 获取控件Id
     * @Author 孙建旺
     * @Date 下午8:22 2020/05/13
     * @Param []
     * @return void
     */
    private void getViews(){
        okHttpClient = OkHttpUtils.getInstance(context);
        number_tip = activity.findViewById(R.id.number_tip);
        btnPreQuestion = activity.findViewById(R.id.btnPreQuestion);
        btnNextQuestion = activity.findViewById(R.id.btnNextQuestion);
        finish_do = activity.findViewById(R.id.finish_do);
        //填空题布局
        completion_layout = activity.findViewById(R.id.completion_layout);
        //选择题布局
        choice_layout = activity.findViewById(R.id.choice_layout);
        //判断题布局
        judgement_layout = activity.findViewById(R.id.judge_layout);
        //填空题
        completion_question = activity.findViewById(R.id.completion_Question); //填空题问题
        completion_answer = activity.findViewById(R.id.completion_Answer); //填空题答案输入框
        isTrue = activity.findViewById(R.id.isTrue); //是否正确文字提示
//        isFalse = activity.findViewById(R.id.isFalse); //是否正确图片提示
        //选择题
        choice_question = activity.findViewById(R.id.choice_Question);
        choice_isTrue = activity.findViewById(R.id.choice_isTrue);
        choice_A = activity.findViewById(R.id.choice_A);
        choice_B = activity.findViewById(R.id.choice_B);
        choice_C = activity.findViewById(R.id.choice_C);
        checkBox_A = activity.findViewById(R.id.checkbox_A);
        checkBox_B = activity.findViewById(R.id.checkbox_B);
        checkBox_C = activity.findViewById(R.id.checkbox_C);
        //判断题
        judge_question = activity.findViewById(R.id.judge_Question);
        judge_isTrue = activity.findViewById(R.id.judge_isTrue);
        judge_A = activity.findViewById(R.id.judge_A);
        judge_B = activity.findViewById(R.id.judge_B);
        your_answer = activity.findViewById(R.id.your_answer);
    }

    /**
     * @Description 位置减1
     * @Author 孙建旺
     * @Date 下午5:21 2020/05/13
     * @Param []
     * @return void
     */
    public static void PositionLess(){
        POSITION = POSITION - 1;
    }

    /**
     * @Description 位置加1
     * @Author 孙建旺
     * @Date 下午5:21 2020/05/13
     * @Param []
     * @return void
     */
    public static void PositionAdd(){
        POSITION = POSITION + 1;
    }

    /**
     * @Description 展示下一道题目
     * @Auther 孙建旺
     * @Date 上午 11:33 2019/12/11
     * @Param [pos]
     * @return void
     */
    public void showQuestion(){
        switch (datalist.get(POSITION).getQuestionType().getId()){
            case 1:
                choice_layout.setVisibility(View.VISIBLE);
                completion_layout.setVisibility(View.GONE);
                judgement_layout.setVisibility(View.GONE);
                number_tip.setText(POSITION+1 + " / " + datalist.size());
                checkBox_A.setChecked(false);
                checkBox_B.setChecked(false);
                checkBox_C.setChecked(false);
                if(POSITION == datalist.size()-1){
                    btnNextQuestion.setText("我答完啦");
                }else{
                    btnNextQuestion.setText("下一题");
                }
                ShowSingleChoice();
                break;
            case 2:
                completion_layout.setVisibility(View.VISIBLE);
                choice_layout.setVisibility(View.GONE);
                judgement_layout.setVisibility(View.GONE);
                number_tip.setText(POSITION+1 + " / " + datalist.size());
                if(POSITION == datalist.size()-1){
                    btnNextQuestion.setText("我答完啦");
                }else{
                    btnNextQuestion.setText("下一题");
                }
                ShowCompletion();
                break;
            case 3:
                judgement_layout.setVisibility(View.VISIBLE);
                choice_layout.setVisibility(View.GONE);
                completion_layout.setVisibility(View.GONE);
                if(POSITION == datalist.size()-1){
                    btnNextQuestion.setText("我答完啦");
                }else{
                    btnNextQuestion.setText("下一题");
                }
                ShowJudgement();
                break;
        }
    }

    /**
     * @Description 展示填空题
     * @Author 孙建旺
     * @Date 下午5:31 2020/05/12
     * @Param [pos]
     * @return void
     */
    public void ShowCompletion(){
        btnPreQuestion.setClickable(true);
        btnNextQuestion.setClickable(true);
        if(datalist.get(POSITION).getIfDone() == 1) {
            isTrue.setVisibility(View.INVISIBLE);
            completion_answer.setVisibility(View.GONE);
            your_answer.setVisibility(View.GONE);
            Completion completion = (Completion) datalist.get(POSITION);
            completion_question.setText(datalist.get(POSITION).getQuestionTitle().getTitle()+ completion.getAnswer());
        }else{
            if(completion_answer.getVisibility() == View.INVISIBLE){
                completion_answer.setVisibility(View.VISIBLE);
            }
            isTrue.setVisibility(View.INVISIBLE);
            your_answer.setVisibility(View.VISIBLE);
            completion_answer.setVisibility(View.VISIBLE);
            completion_question.setText(datalist.get(POSITION).getQuestionTitle().getTitle());
        }
    }

    /**
     * @Description 展示单选题
     * @Author 孙建旺
     * @Date 上午11:45 2020/05/13
     * @Param [pos]
     * @return void
     */
    public void ShowSingleChoice(){
        btnPreQuestion.setClickable(true);
        btnNextQuestion.setClickable(true);
        if(datalist.get(POSITION).getIfDone() == 1){
            choice_isTrue.setVisibility(View.INVISIBLE);
            choice_question.setVisibility(View.VISIBLE);
            choice_question.setText(datalist.get(POSITION).getQuestionTitle().getTitle());
            choice_A.setVisibility(View.INVISIBLE);
            checkBox_C.setVisibility(View.INVISIBLE);
            checkBox_A.setVisibility(View.INVISIBLE);
            checkBox_B.setVisibility(View.GONE);
            choice_C.setVisibility(View.INVISIBLE);
            choice_B.setText(((SingleChoice)datalist.get(POSITION)).getAnswer());
        }else {
            checkBox_C.setVisibility(View.VISIBLE);
            checkBox_A.setVisibility(View.VISIBLE);
            checkBox_B.setVisibility(View.VISIBLE);
            choice_A.setVisibility(View.VISIBLE);
            choice_C.setVisibility(View.VISIBLE);
            choice_isTrue.setVisibility(View.INVISIBLE);
            choice_question.setText(datalist.get(POSITION).getQuestionTitle().getTitle());
            switch (new Random().nextInt(3)){
                case 0:
                    choice_A.setText(((SingleChoice)datalist.get(POSITION)).getAnswer());
                    choice_B.setText(((SingleChoice)datalist.get(POSITION)).getChoice2());
                    choice_C.setText(((SingleChoice)datalist.get(POSITION)).getChoice1());
                    break;
                case 1:
                    choice_B.setText(((SingleChoice)datalist.get(POSITION)).getAnswer());
                    choice_A.setText(((SingleChoice)datalist.get(POSITION)).getChoice2());
                    choice_C.setText(((SingleChoice)datalist.get(POSITION)).getChoice1());
                    break;
                case 2:
                    choice_C.setText(((SingleChoice)datalist.get(POSITION)).getAnswer());
                    choice_A.setText(((SingleChoice)datalist.get(POSITION)).getChoice2());
                    choice_B.setText(((SingleChoice)datalist.get(POSITION)).getChoice1());
                    break;
            }
        }
    }

    /**
     * @Description 展示判断题
     * @Author 孙建旺
     * @Date 下午5:36 2020/05/12
     * @Param []
     * @return void
     */
    public void ShowJudgement(){
        btnPreQuestion.setClickable(true);
        btnNextQuestion.setClickable(true);
        if(datalist.get(POSITION).getIfDone() == 1){
            judge_isTrue.setVisibility(View.GONE);
            judge_A.setVisibility(View.GONE);
            judge_B.setText(((Judgment)datalist.get(POSITION)).getAnswer());
        }else {
            judge_isTrue.setVisibility(View.GONE);
            judge_A.setVisibility(View.VISIBLE);
            switch (new Random().nextInt(1)){
                case 0:
                    judge_A.setText(((Judgment)datalist.get(POSITION)).getAnswer()+"");
                    judge_B.setText(((Judgment)datalist.get(POSITION)).getChoice()+"");
                    break;
                case 1:
                    judge_B.setText(((Judgment)datalist.get(POSITION)).getAnswer()+"");
                    judge_A.setText(((Judgment)datalist.get(POSITION)).getChoice()+"");
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ChoiceIfTrue(){
        String choose_answer = "";
        if(checkBox_A.isChecked())
            choose_answer = choice_A.getText().toString();
        if(checkBox_B.isChecked())
            choose_answer = choice_B.getText().toString();
        if(checkBox_C.isChecked())
            choose_answer = choice_C.getText().toString();
        if(choose_answer.equals(((SingleChoice)datalist.get(POSITION)).getAnswer())){
            TRUE_ANSWER_COUNT++;
            choice_isTrue.setVisibility(View.VISIBLE);
            choice_isTrue.setImageDrawable(context.getResources().getDrawable(R.drawable.duigou,null));
            StudyUtil.PlayTrueSound(context);
            if((POSITION+1)<=datalist.size()-1) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        datalist.get(POSITION).setIfDone(1);
                        PositionAdd();
                        showQuestion();
                    }
                }, 1000);
            }else {
                getWaterAndFertilizer();
                btnNextQuestion.setClickable(false);
            }
        }else {
            btnPreQuestion.setClickable(true);
            btnNextQuestion.setClickable(true);
            choice_isTrue.setImageDrawable(context.getResources().getDrawable(R.drawable.cha,null));
            choice_isTrue.setVisibility(View.VISIBLE);
            StudyUtil.PlayFalseSound(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void CompletionIfTrue(){
        String inputRes = completion_answer.getText().toString().trim();
        if(inputRes.equals("")) {
            completion_answer.setText("");
            if((POSITION+1)<=datalist.size()-1) {
                PositionAdd();
                showQuestion();
            }else{
                if(TRUE_ANSWER_COUNT < datalist.size()){
                    if(toast!=null){
                        toast = Toast.makeText(context,"你还没有答完哦",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
            return;
        }
        if(inputRes.equals(((Completion)datalist.get(POSITION)).getAnswer()+"")) {
            TRUE_ANSWER_COUNT++;
            isTrue.setImageDrawable(context.getResources().getDrawable(R.drawable.duigou,null));
            isTrue.setVisibility(View.VISIBLE);
            btnNextQuestion.setClickable(false);
            StudyUtil.PlayTrueSound(context);
            if((POSITION+1)<=datalist.size()-1) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        completion_answer.setText("");
                        datalist.get(POSITION).setIfDone(1);
                        PositionAdd();
                        showQuestion();
                    }
                }, 1000);
            }else{
                getWaterAndFertilizer();
                btnNextQuestion.setClickable(false);
            }
        }else{
            btnNextQuestion.setClickable(true);
            btnPreQuestion.setClickable(true);
            isTrue.setImageDrawable(context.getResources().getDrawable(R.drawable.cha,null));
            isTrue.setVisibility(View.VISIBLE);
            StudyUtil.PlayFalseSound(context);
            completion_answer.setText("");
        }
    }

    public void getWaterAndFertilizer(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("water",TRUE_ANSWER_COUNT*1+"")
                        .add("fertilizer",TRUE_ANSWER_COUNT*1+"")
                        .add("subject",datalist.get(POSITION).getSubject()).build();
                Request request = new Request.Builder().url(context.getResources().getString(R.string.URL)+"/user/lessRewardCount").post(formBody).build();
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
        getWAF = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = (String)msg.obj;
                if(data!= null){
                    if(!data.equals("-1")){
                        switch (CURRENT_SUBJECT){
                            case "Math":
                                UserUtil.getUser().setMathRewardCount(UserUtil.getUser().getMathRewardCount() - 1);
                                break;
                            case "Chinese":
                                UserUtil.getUser().setChineseRewardCount(UserUtil.getUser().getChineseRewardCount() - 1);
                                break;
                            case "English":
                                UserUtil.getUser().setEnglishRewardCount(UserUtil.getUser().getEnglishRewardCount() - 1);
                                break;

                        }
                        finish_do.setVisibility(View.VISIBLE);
                        btnPreQuestion.setVisibility(View.GONE);
                        btnNextQuestion.setVisibility(View.GONE);
                        number_tip.setVisibility(View.GONE);
                        choice_layout.setVisibility(View.GONE);
                        completion_layout.setVisibility(View.GONE);
                        judgement_layout.setVisibility(View.GONE);
                        if(returnHandlerFinish) {
                            activity.finish();
                            return;
                        }
                    }else{
                        if(toast == null){
                            toast = Toast.makeText(context,"服务器开小差了",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM,0,0);
                            toast.show();
                        }
                    }
                }
            }
        };
    }

    /**
     * @Description  确认是否返回
     * @Auther 孙建旺
     * @Date 下午 5:00 2019/12/11
     * @Param []
     * @return void
     */
    public void showIfReturn(){
        ifReturn = new Dialog(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.math_return_dialog,null);
        ImageView cancel = layout.findViewById(R.id.cancel_return);
        ImageView sure = layout.findViewById(R.id.sure_return);
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
                getWaterAndFertilizer();
            }
        });
        ifReturn.setContentView(layout);
        ifReturn.show();
        WindowManager.LayoutParams attrs = ifReturn.getWindow().getAttributes();
        attrs.gravity = Gravity.CENTER;
        final float scale = context.getResources().getDisplayMetrics().density;
        attrs.width = (int)(300*scale+0.5f);
        attrs.height =(int)(300*scale+0.5f);
        ifReturn.getWindow().setAttributes(attrs);
        Window dialogWindow = ifReturn.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void exit() {
        if(QuestionUtil.TRUE_ANSWER_COUNT>0 && QuestionUtil.TRUE_ANSWER_COUNT<datalist.size() && UserUtil.getUser().getMathRewardCount()>0)
            showIfReturn();
        else
            activity.finish();
    }
}
