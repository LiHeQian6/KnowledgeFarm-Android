package com.li.knowledgefarm.pk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FromJson;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.QuestionEntity.Question;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PkActivity extends AppCompatActivity {

    public static int ROUND_COUNT = 1;//回合数计数器
    private RelativeLayout my_pet; //我的宠物展示块
    private RelativeLayout other_pet; //对手宠物展示块
    private Button start_battle_btn;//开始PK按钮
    private PetPkTimeLimit pkTimeLimit;//倒计时任务处理类
    private OkHttpClient okHttpClient;
    private List<Question> list;//题目集
    private Handler resolveList;
    private Toast toast; //Toast
    private int position = 0; //题目位置
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
    private PetPkQuestionDialog pkQuestionDialog;//问题展示弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p);

        getViews();
        setViewSize();
        getQuestion();
    }


    /**
     * @Description 获取题目
     * @Author 孙建旺
     * @Date 下午8:13 2020/05/19
     * @Param []
     * @return void
     */
    private void getQuestion(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/answer/fightQuestions?grade="+ UserUtil.getUser().getId()).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        Message message = new Message();
                        message.obj = response.body().string();
                        message.what = response.code();
                        resolveList.sendMessage(message);
                    }
                });
            }
        }.start();
        resolveList = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = (String)msg.obj;
                if(msg.what == 200) {
                    list = FromJson.JsonToEntity(data);
                }else {
                    if(toast == null){
                        toast = Toast.makeText(PkActivity.this,"获取题目失败",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM,0,0);
                        toast.show();
                    }
                }
            }
        };
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

        okHttpClient = OkHttpUtils.getInstance(this);
        list = new ArrayList<>();
        pkQuestionDialog = new PetPkQuestionDialog(PkActivity.this);
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
        pkQuestionDialog.setOnAnswerSelectListener(new PetPkQuestionDialog.OnAnswerSelectListener() {
            @Override
            public void select(boolean isRight, long time) {

            }
        });
    }

    /**
     * @Description 设置自定义弹窗相关属性
     * @Author 孙建旺
     * @Date 下午5:25 2020/05/19
     * @Param []
     * @return void
     */
    private void setCustomDialog(){
        pkQuestionDialog.show();
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        WindowManager.LayoutParams params = pkQuestionDialog.getWindow().getAttributes();
        params.width = (int)(0.7*ds.widthPixels);
        params.height = (int)(0.95*ds.heightPixels);
        pkQuestionDialog.getWindow().setAttributes(params);
        pkQuestionDialog.setCancelable(false);
//        pkQuestionDialog.getWindow().setAttributes();
    }

    /**
     * @Description 自定义点击事件实现类
     * @Author 孙建旺
     * @Date 下午3:39 2020/05/19
     * @Param
     * @return
     */
    private class CustomOnclickListener implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start_battle_btn:
//                    pkTimeLimit = new PetPkTimeLimit(time_limit);
//                    pkTimeLimit.execute();
                    pkQuestionDialog.setQuestion(list.get(position));
                    setCustomDialog();
                   // start_battle_btn.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
