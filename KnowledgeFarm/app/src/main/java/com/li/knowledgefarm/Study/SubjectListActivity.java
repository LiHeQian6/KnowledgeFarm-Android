package com.li.knowledgefarm.Study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.li.knowledgefarm.MainActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Settings.SettingActivity;

public class SubjectListActivity extends AppCompatActivity {
    /** 返回按钮*/
    private Button btnReturn;
    private Button btnMath;
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
    }

    class CustomerListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnReturn:
                    Intent intent = new Intent();
                    intent.setClass(SubjectListActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnMath:
                    Intent intent1 = new Intent();
                    intent1.setClass(SubjectListActivity.this,MathActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    }

    /**
     * 加载视图
     */
    private void getViews(){
        btnReturn = findViewById(R.id.btnReturn);
        btnMath = findViewById(R.id.btnMath);
    }

    /**
     * 注册点击事件监听器
     */
    private void registListener(){
        listener = new CustomerListener();
        btnReturn.setOnClickListener(listener);
        btnMath.setOnClickListener(listener);
    }
}
