package com.li.knowledgefarm.pk;

import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FromJson;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.QuestionEntity.Question;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.User;
import com.li.knowledgefarm.entity.UserPkPet;


public class PkActivity extends AppCompatActivity {

    public static int ROUND_COUNT=1;//回合数计数器
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


    private UserPkPet friend_pet;
    private UserPkPet user_pet;
    private TextView center_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p);
        getViews();
        setViewSize();
        getQuestion();
        initData();
        beginPK();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullScreen.NavigationBarStatusBar(PkActivity.this,true);
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
        center_text=findViewById(R.id.center_text);

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
                    pkQuestionDialog.show();
                   // start_battle_btn.setVisibility(View.GONE);
                    break;
            }
        }
    }
    /**
     * @Author li
     * @param
     * @return void
     * @Description 初始化数据
     * @Date 12:25 2020/5/19
     **/
    private void initData(){
        Intent intent = getIntent();
        User friend = (User) intent.getSerializableExtra("friend");
        User user = UserUtil.getUser();
        friend_pet = new UserPkPet(friend.getPetHouses().get(0));
        user_pet =new UserPkPet(user.getPetHouses().get(0));
        friend_pet.setUser(friend);
        user_pet.setUser(user);
        showData();
        Glide.with(this).load(user_pet.getPet().getImg1()).into(my_pet_image);
        Glide.with(this).load(friend_pet.getPet().getImg1()).into(other_pet_image);
    }

    /**
     * @Author li
     * @param
     * @return void
     * @Description 给布局设置数据
     * @Date 12:26 2020/5/19
     **/
    private void showData() {
        Glide.with(this).load(user_pet.getUser().getPhoto()).apply(new RequestOptions().circleCrop()).into(my_pet_small_image);
        Glide.with(this).load(friend_pet.getUser().getPhoto()).apply(new RequestOptions().circleCrop()).into(other_pet_small_image);
        my_pet_bar.setMax(user_pet.getLife());
        other_pet_bar.setMax(friend_pet.getLife());
        my_pet_bar.setProgress(user_pet.getNowLife());
        other_pet_bar.setProgress(friend_pet.getNowLife());
        my_pet_value.setText(user_pet.getNowLife()+"/"+user_pet.getLife());
        other_pet_value.setText(friend_pet.getNowLife()+"/"+friend_pet.getLife());
        my_pet_intelligence_value.setText("智力值:"+user_pet.getIntelligence());
        other_pet_intelligence_value.setText("智力值:"+friend_pet.getIntelligence());
        less_my_pet.setVisibility(View.INVISIBLE);
        less_other_pet.setVisibility(View.INVISIBLE);
    }

    private void beginPK() {
        //第几回合、开始倒计时3秒                    动画效果先由大变小
        pkTimeLimit = new PetPkTimeLimit(time_limit, new PetPkTimeLimit.Stop() {
            @Override
            public void onStop() {
                pkTimeLimit.cancel(true);
                pkTimeLimit=null;
                pkTimeLimit=new PetPkTimeLimit(time_limit,this);
                //弹出答题框
                AlertDialog show = new AlertDialog.Builder(PkActivity.this).setMessage("66666").show();
//            PetPkQuestionPopUpWindow petPkQuestionPopUpWindow = new PetPkQuestionPopUpWindow(this);
//            petPkQuestionPopUpWindow.showAtLocation(center_text, Gravity.CENTER,0,0);
                //设置答对或错
//            petPkQuestionPopUpWindow.setOnAnswerSelectListener(new PetPkQuestionPopUpWindow.OnAnswerSelectListener(){
//                public void select(boolean isRight,int userTime){
//
//                }
//            });
                show.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //展示攻击、被攻击动画

                        //得到扣除的血量并设置
                        int pk = user_pet.pk(friend_pet);
                        if (pk==0){
                            center_text.setVisibility(View.VISIBLE);
                        }else if(pk>0){
                            less_other_pet.setVisibility(View.VISIBLE);
                            less_other_pet.setText("-"+pk);
                        }else {
                            less_my_pet.setVisibility(View.VISIBLE);
                            less_my_pet.setText(""+pk);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                center_text.setVisibility(View.GONE);
                                showData();
                            }
                        },2000);
                        //判断是否结束
                        ROUND_COUNT++;
                        if (user_pet.getNowLife()>0&&friend_pet.getNowLife()>0){
                            pkTimeLimit.execute();
                        }else{
                            //结束展示胜利动画
                            //访问后台减少体力值、增加智力值
                        }
                    }
                });
            }
        });
        pkTimeLimit.execute();

    }
}
