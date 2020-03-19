package com.li.knowledgefarm.Study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Settings.SettingActivity;

public class SubjectListActivity extends AppCompatActivity {
    /** 返回*/
    private ImageView iv_return;
    /** 数学*/
    private ImageView iv_math;
    private ImageView iv_english;
    private ImageView iv_chinese;
    /** 自定义点击事件监听器*/
    private CustomerListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        /** 加载视图*/
        getViews();
        /** 注册点击事件监听器*/
        registListener();
        StudyUtil.setStatusBar(this);
    }

    class CustomerListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_return:
                    finish();
                    break;
                case R.id.iv_math:
                    Intent intent1 = new Intent();
                    intent1.setClass(SubjectListActivity.this,MathActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.iv_english:
                    Intent intent2 = new Intent(SubjectListActivity.this,EnglishActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.chinese:
                    Intent intent3 = new Intent(SubjectListActivity.this,ChineseActivity.class);
                    startActivity(intent3);
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
