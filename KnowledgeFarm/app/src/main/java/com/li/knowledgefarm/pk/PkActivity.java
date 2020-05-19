package com.li.knowledgefarm.pk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.knowledgefarm.R;

public class PkActivity extends AppCompatActivity {

    public static int ROUND_COUNT;//回合数计数器
    private RelativeLayout my_pet; //我的宠物展示块
    private RelativeLayout other_pet; //对手宠物展示块
    private Button start_battle_btn;//开始PK按钮
    private PetPkTimeLimit pkTimeLimit;//倒计时任务处理类
    //我的宠物相关
    private ImageView my_pet_small_image;//我的宠物头像
    private ProgressBar my_pet_bar; //我的宠物进度条
    private TextView my_pet_intelligence_value; //我的宠物智力值
    private ImageView my_pet_image;//我的宠物图像
    private TextView less_my_pet;//减少我的宠物血量文字
    private TextView my_pet_value;//我的宠物进度条文字
    //对方宠物相关
    private ImageView other_pet_small_image;//对方宠物头像
    private ProgressBar other_pet_bar;//对方宠物进度条
    private TextView other_pet_intelligence_value;//对方宠物智力值
    private ImageView other_pet_image;//对方宠物图像
    private TextView less_other_pet;//减少对方宠物血量文字
    private TextView other_pet_value;//对方宠物进度条文字

    private TextView time_limit;//时间限制文字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p);

        getViews();
        setViewSize();
    }

    /**
     * @Description 设置控件大小
     * @Author 孙建旺
     * @Date 上午10:56 2020/05/17
     * @Param []
     * @return void
     */
    private void setViewSize() {
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) my_pet.getLayoutParams();
        params.width = (int)(0.5*ds.widthPixels);
        my_pet.setLayoutParams(params);

        RelativeLayout.LayoutParams other_pet_param = (RelativeLayout.LayoutParams) other_pet.getLayoutParams();
        other_pet_param.width = (int)(0.5*ds.widthPixels);
        other_pet.setLayoutParams(other_pet_param);
    }

    /**
     * @Description 获取控件ID及初始化对象
     * @Author 孙建旺
     * @Date 上午10:53 2020/05/17
     * @Param []
     * @return void
     */
    private void getViews() {
        start_battle_btn = findViewById(R.id.start_battle_btn);
        my_pet = findViewById(R.id.my_pet);
        other_pet = findViewById(R.id.other_pet);
        my_pet_small_image = findViewById(R.id.my_dog_small_image);
        my_pet_bar = findViewById(R.id.my_pet_bar);
        my_pet_value = findViewById(R.id.my_pet_value);
        my_pet_intelligence_value = findViewById(R.id.my_pet_intelligence_value);
        my_pet_image = findViewById(R.id.my_pet_image);
        less_my_pet = findViewById(R.id.less_my_pet);

        other_pet_small_image = findViewById(R.id.other_pet_small_image);
        other_pet_intelligence_value = findViewById(R.id.other_pet_intelligence_value);
        other_pet_bar = findViewById(R.id.other_pet_bar);
        other_pet_value = findViewById(R.id.other_pet_value);
        other_pet_image = findViewById(R.id.other_pet_image);
        less_other_pet = findViewById(R.id.less_other_pet);

        time_limit = findViewById(R.id.time_limit);

        registerListener();
    }

    /**
     * @Description 注册点击事件监听器
     * @Author 孙建旺
     * @Date 下午3:41 2020/05/19
     * @Param []
     * @return void
     */
    private void registerListener(){
        start_battle_btn.setOnClickListener(new CustomOnclickListener());
    }

    /**
     * @Description 自定义点击事件实现类
     * @Author 孙建旺
     * @Date 下午3:39 2020/05/19
     * @Param
     * @return
     */
    private class CustomOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start_battle_btn:
                    pkTimeLimit = new PetPkTimeLimit(time_limit);
                    pkTimeLimit.execute();
                   // start_battle_btn.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
