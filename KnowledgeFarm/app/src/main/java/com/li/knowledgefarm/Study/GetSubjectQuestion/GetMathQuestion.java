package com.li.knowledgefarm.Study.GetSubjectQuestion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Study.Interface.SubjectInterface;
import com.li.knowledgefarm.Util.CustomerToast;
import com.li.knowledgefarm.Util.FromJson;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.QuestionEntity.Completion;
import com.li.knowledgefarm.entity.QuestionEntity.Math23;
import com.li.knowledgefarm.entity.QuestionEntity.Question;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 孙建旺
 * @description 获取数学题目
 * @date 2020/03/23 下午 8:10
 */

public class GetMathQuestion extends SubjectInterface {
    private OkHttpClient okHttpClient;
    private Gson gson = new Gson();
    private List<Question> list = null;
    private Handler getMath;
    private final Activity context;
    private Intent intent;
    private Toast toast;

    public GetMathQuestion(Activity activity, Intent intent) {
        this.context = activity;
        this.intent = intent;
        okHttpClient = OkHttpUtils.getInstance(context);
    }

    /**
     * @Description 获取数学题
     * @Auther 孙建旺
     * @Date 上午 8:56 2019/12/11
     * @Param []
     * @return void
     */
    @Override
    public void getQuestion() {
        getMathHandler(intent);
        new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                super.run();
                if (UserUtil.getUser().getMathRewardCount() <= 0) {
                    Looper.prepare();
                    CustomerToast.getInstance(context, "今天的任务做完了哦", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    Request request = null;
                    switch (UserUtil.getUser().getGrade()) {
                        case 1:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/mathOneUp").build();
                            break;
                        case 2:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/mathOneDown").build();
                            break;
                        case 3:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/mathTwoUp").build();
                            break;
                        case 4:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/mathTwoDown").build();
                            break;
                        case 5:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/mathThreeUp").build();
                            break;
                        case 6:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/mathThreeDown").build();
                            break;
                    }
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Message message = Message.obtain();
                            message.obj = "Fail";
                            message.what = 0;
                            getMath.sendMessage(message);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                            Message message = Message.obtain();
                            message.obj = response.body().string();
                            message.arg1 = response.code();
                            message.what = UserUtil.getUser().getGrade();
                            getMath.sendMessage(message);
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * @Description 处理返回值
     * @Author 孙建旺
     * @Date 下午 8:28 2020/03/23
     * @Param []
     * @return java.util.List<com.li.knowledgefarm.entity.QuestionEntity.Question3Num>
     */
    @SuppressLint("HandlerLeak")
    @Override
    public void getMathHandler(final Intent intent) {
        getMath = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = (String)msg.obj;
                Log.i("data",data);
                if(!data.equals("Fail") && !data.equals("") && msg.arg1 == 200) {
                    list = FromJson.JsonToEntity(data);
                    if(list != null){
                        intent.putExtra("question",(Serializable) list);
                        context.startActivity(intent);
                    }else{
                        CustomerToast.getInstance(context,"网络出了点问题",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    CustomerToast.getInstance(context,"网络出了点问题",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

}
