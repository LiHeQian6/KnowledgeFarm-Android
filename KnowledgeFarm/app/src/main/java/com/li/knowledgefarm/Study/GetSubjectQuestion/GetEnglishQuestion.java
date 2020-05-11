package com.li.knowledgefarm.Study.GetSubjectQuestion;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Study.Interface.SubjectInterface;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.English;
import com.li.knowledgefarm.entity.Question3Num;
import com.li.knowledgefarm.entity.QuestionPage;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 孙建旺
 * @description
 * @date 2020/03/23 下午 10:21
 */

public class GetEnglishQuestion extends SubjectInterface {
    private OkHttpClient okHttpClient;
    private Gson gson = new Gson();
    private List<English> list = null;
    private Handler getMath;
    private final Activity context;
    private Intent intent;

    public GetEnglishQuestion(Activity context, Intent intent) {
        this.context = context;
        this.intent = intent;
        okHttpClient = OkHttpUtils.getInstance(context);
    }

    /**
     * @Description 获取英语题
     * @Auther 景光赞
     * @Date 上午 8:56 2019/12/11
     * @Param []
     * @return void
     */
    @Override
    public void getQuestion() {
        this.getMathHandler(intent);
        new Thread(){
            @Override
            public void run() {
                super.run();
                if (LoginActivity.user.getEnglishRewardCount() <= 0) {
                    Toast.makeText(context,"今天的英语任务做完了哦",Toast.LENGTH_SHORT).show();
                } else {
                    Request request = null;
                    switch (LoginActivity.user.getGrade()) {
                        case 1:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/englishOneUp").build();
                            break;
                        case 2:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/englishOneDown").build();
                            break;
                        case 3:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/englishTwoUp").build();
                            break;
                        case 4:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/englishTwoDown").build();
                            break;
                        case 5:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/englishThreeUp").build();
                            break;
                        case 6:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/englishThreeDown").build();
                            break;
                    }
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Message message = Message.obtain();
                            message.obj = "Fail";
                            getMath.sendMessage(message);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Message message = Message.obtain();
                            message.obj = response.body().string();
                            message.arg1 = response.code();
                            getMath.sendMessage(message);
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * @Description 处理返回的Json串
     * @Auther 景光赞
     * @Date 上午 9:10 2019/12/11
     * @Param []
     * @return void
     */
    @Override
    public void getMathHandler(final Intent intent) {
        getMath = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = (String)msg.obj;
                if(!data.equals("Fail") && !data.equals("") && msg.arg1 == 200) {
                    Type type = new TypeToken<List<English>>() {
                    }.getType();
                    list = gson.fromJson(data, type);
                    if(list != null){
                        intent.putExtra("english",(Serializable) list);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"网络出了点问题",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,"网络出了点问题",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
