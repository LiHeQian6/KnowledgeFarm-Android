package com.li.knowledgefarm.Study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Study.GetSubjectQuestion.GetChineseQuestion;
import com.li.knowledgefarm.Study.GetSubjectQuestion.GetEnglishQuestion;
import com.li.knowledgefarm.Study.GetSubjectQuestion.GetMathQuestion;
import com.li.knowledgefarm.Study.Subject.QuestionActivity;
import com.li.knowledgefarm.Util.FullScreen;


public class SubjectListActivity extends AppCompatActivity {
    /** 返回*/
    private ImageView iv_return;
    /** 数学*/
    private ImageView iv_math;
    private ImageView iv_english;
    private ImageView iv_chinese;
    /** 自定义点击事件监听器*/
    private CustomerListener listener;
    private long lastClickTime = 0L;
    // 两次点击间隔不能少于1000ms
    private static final int FAST_CLICK_DELAY_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        /** 加载视图*/
        getViews();
        /** 注册点击事件监听器*/
        registListener();
        FullScreen.NavigationBarStatusBar(SubjectListActivity.this,true);
    }

    class CustomerListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                return;
            }
            lastClickTime = System.currentTimeMillis();
            switch (view.getId()){
                case R.id.iv_return:
                    finish();
                    break;
                case R.id.iv_math:
                    Intent intent = new Intent();
                    intent.setClass(SubjectListActivity.this, QuestionActivity.class);
                    GetMathQuestion getMathQuestion = new GetMathQuestion(SubjectListActivity.this,intent);
                    getMathQuestion.getQuestion();
                    break;
                case R.id.iv_english:
                    Intent intent1 = new Intent(SubjectListActivity.this, QuestionActivity.class);
                    GetEnglishQuestion getEnglishQuestion = new GetEnglishQuestion(SubjectListActivity.this,intent1);
                    getEnglishQuestion.getQuestion();
                    break;
                case R.id.chinese:
                    Intent intent2 = new Intent(SubjectListActivity.this, QuestionActivity.class);
                    GetChineseQuestion getChineseQuestion = new GetChineseQuestion(SubjectListActivity.this,intent2);
                    getChineseQuestion.getQuestion();
                    break;
            }
        }
    }

    /**
     * 加载视图
     */
    private void getViews(){
        iv_return = findViewById(R.id.iv_return);
        iv_math = findViewById(R.id.iv_math);
        iv_english = findViewById(R.id.iv_english);
        iv_chinese = findViewById(R.id.chinese);
    }

    /**
     * 注册点击事件监听器
     */
    private void registListener(){
        listener = new CustomerListener();
        iv_return.setOnClickListener(listener);
        iv_math.setOnClickListener(listener);
        iv_english.setOnClickListener(listener);
        iv_chinese.setOnClickListener(listener);
    }

}
