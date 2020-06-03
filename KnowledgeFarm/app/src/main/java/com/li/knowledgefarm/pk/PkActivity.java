package com.li.knowledgefarm.pk;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.BitmapCut;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.FromJson;
import com.li.knowledgefarm.Util.NavigationBarUtil;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.EventBean;
import com.li.knowledgefarm.entity.QuestionEntity.Question;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.entity.User;
import com.li.knowledgefarm.entity.UserPetHouse;
import com.li.knowledgefarm.entity.UserPkPet;


public class PkActivity extends AppCompatActivity{

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
    private AlertDialog alertDialog;//弹出框
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


    private UserPkPet friend_pet;//朋友的宠物
    private UserPkPet user_pet;//自己的宠物
    private TextView center_text;//中部文字
    private Button run_away_btn;//逃跑按钮
    private ImageView attack;
    private Bitmap attack_image;
    private Handler result_handler;

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
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/answer/fightQuestions?grade="+ UserUtil.getUser().getGrade()).build();
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
                    CustomerToast.getInstance(PkActivity.this,"获取题目失败",Toast.LENGTH_SHORT).show();
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
        run_away_btn=findViewById(R.id.run_away);
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
        attack=findViewById(R.id.attack);

        okHttpClient = OkHttpUtils.getInstance(this);
        list = new ArrayList<>();

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
        run_away_btn.setOnClickListener(new CustomOnclickListener());
    }

    /**
     * @Description 询问是否确定逃跑弹窗
     * @Author 孙建旺
     * @Date 下午4:16 2020/05/21
     * @Param []
     * @return void
     */
    private void showAlertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(PkActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_layout,null);
        Button cancel = view.findViewById(R.id.cancel_go_away);
        Button sure = view.findViewById(R.id.sure_go_away);
        cancel.setOnClickListener(new CustomOnclickListener());
        sure.setOnClickListener(new CustomOnclickListener());
        dialog.setView(view);
        alertDialog = dialog.create();
        alertDialog.show();
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = (int)(0.4*ds.widthPixels);
        params.height = (int)(0.6*ds.heightPixels);
        alertDialog.getWindow().setAttributes(params);
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
                case R.id.run_away:
//                    showAlertDialog();
                    finish();
                    break;
                case R.id.cancel_go_away:
                    alertDialog.dismiss();
                    break;
                case R.id.sure_go_away:
                    finish();
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
        attack_image = BitmapFactory.decodeResource(getResources(), R.drawable.attack);
        ROUND_COUNT=1;
        showData();
        UserPetHouse userPetHouse = UserUtil.getUser().getPetHouses().get(0);
        String url = userPetHouse.getGrowPeriod() == 0 ? userPetHouse.getPet().getGif1() : userPetHouse.getGrowPeriod()==1? userPetHouse.getPet().getGif2() : userPetHouse.getPet().getGif3();
        Glide.with(this).asGif().load(url).diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.drawable.erhah).into(my_pet_image);
        String url2=friend_pet.getGrowPeriod() == 0 ? friend_pet.getPet().getGif1() : friend_pet.getGrowPeriod()==1? friend_pet.getPet().getGif2() : friend_pet.getPet().getGif3();
        Glide.with(this).asGif().load(url2).diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.drawable.erhah).into(other_pet_image);
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
        center_text.setVisibility(View.VISIBLE);
        center_text.setText("  准备就绪！\n  触摸开始");
        run_away_btn.setVisibility(View.GONE);
        center_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pkTimeLimit.execute();
                center_text.setVisibility(View.GONE);
                center_text.setOnClickListener(null);
                center_text.setText("平局啦！");
                run_away_btn.setVisibility(View.VISIBLE);
            }
        });
//        new Handler(){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//
//            }
//        }.postDelayed(null,1000);
        //第几回合、开始倒计时3秒                    动画效果先由大变小
        pkTimeLimit = new PetPkTimeLimit(time_limit, new PetPkTimeLimit.Stop() {
            @Override
            public void onStop() {
                pkTimeLimit.cancel(true);
                pkTimeLimit=null;
                pkTimeLimit=new PetPkTimeLimit(time_limit,this);
                //弹出答题框
                pkQuestionDialog = new PetPkQuestionDialog(PkActivity.this);
                pkQuestionDialog.setOnAnswerSelectListener(new PetPkQuestionDialog.OnAnswerSelectListener() {
                    @Override
                    public void select(boolean isRight, long time) {
                        user_pet.setRight(isRight);
                        user_pet.setUseTime(time);
                    }
                });
                pkQuestionDialog.setQuestion(list.get(position));
                NavigationBarUtil.focusNotAle(pkQuestionDialog.getWindow());
                pkQuestionDialog.show();
                NavigationBarUtil.hideNavigationBar(pkQuestionDialog.getWindow());
                NavigationBarUtil.clearFocusNotAle(pkQuestionDialog.getWindow());
                pkQuestionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        position++;
                        if (position==list.size()){
                            position=0;
                            getQuestion();
                        }
                        final int pk = user_pet.pk(friend_pet);
                        //展示攻击、被攻击动画
                        for (int i = 0; i < 5; i++) {
                            final int finalI = i;
                            new Handler(){
                                @Override
                                public void handleMessage(@NonNull Message msg) {
                                    super.handleMessage(msg);
                                    Bitmap bitmap = BitmapCut.cutBitmap(attack_image, finalI,5); //调用裁剪图片工具类进行裁剪
                                    attack.setVisibility(View.VISIBLE);
                                    attack.setScaleX(1);
                                    if (pk<0){
                                        attack.setScaleX(-1);
                                    }else if (pk==0){
                                        attack.setVisibility(View.GONE);
                                    }
                                    attack.setImageBitmap(bitmap);
                                }
                            }.postDelayed(null,200*i);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //得到扣除的血量并设置
                                attack.setVisibility(View.GONE);
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
                                },1000);
                                //判断是否结束
                                ROUND_COUNT++;
                                if (user_pet.getNowLife()>0&&friend_pet.getNowLife()>0){
                                    pkTimeLimit.execute();
                                }else{
                                    //结束展示胜利动画
//                                    center_text.setVisibility(View.VISIBLE);
//                                    if(user_pet.getNowLife()>0) {
//                                        center_text.setText("胜利啦！");
//                                    } else{
//                                        center_text.setText("要继续加油哦！");
//                                    }
                                    new Handler(){
                                        @Override
                                        public void handleMessage(@NonNull Message msg) {
                                            super.handleMessage(msg);
                                            finish();
                                        }
                                    }.postDelayed(null,2000);
                                }
                            }
                        },1000);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pkTimeLimit.cancel(true);
        if (user_pet.getNowLife()>0&&friend_pet.getNowLife()>0)
            sendResult(false);
        if (user_pet.getNowLife()>0&&friend_pet.getNowLife()<=0){
            sendResult(true);
        }
        if (user_pet.getNowLife()<=0&&friend_pet.getNowLife()>0)
            sendResult(false);
    }

    /**
     * @Author li
     * @param
     * @return void
     * @Description 发送对战结果到后台
     * @Date 11:36 2020/5/21
     **/
    @SuppressLint("HandlerLeak")
    public void sendResult(final boolean result){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder().url(getResources().getString(R.string.URL)+"/pet/fightResult?result="+(result?1:0)).build();
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
                        result_handler.sendMessage(message);
                    }
                });
            }
        }.start();
        result_handler = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = (String)msg.obj;
                if(msg.what == 200) {
                    if (data.equals("true")){
                        EventBean event = new EventBean();
                        event.setResult(result?"1":"0");
                        EventBus.getDefault().post(event);
                    }else if(data.equals("up")){
                        EventBean event = new EventBean();
                        event.setResult("2");
                        EventBus.getDefault().post(event);
                    }else if(data.equals("false")){
                        CustomerToast.getInstance(PkActivity.this,"数据异常",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(toast == null){
                        CustomerToast.getInstance(PkActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && position >= 1) {

            showAlertDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
