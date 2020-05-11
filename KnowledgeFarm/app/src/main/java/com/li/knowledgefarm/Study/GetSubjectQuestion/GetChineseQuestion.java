package com.li.knowledgefarm.Study.GetSubjectQuestion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Study.Interface.SubjectInterface;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.Util.UserUtil;
import com.li.knowledgefarm.entity.Chinese;
import com.li.knowledgefarm.entity.English;
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
 * @date 2020/03/23 下午 10:20
 */

public class GetChineseQuestion extends SubjectInterface {
    private OkHttpClient okHttpClient;
    private Gson gson = new Gson();
    private List<Chinese> list = null;
    private Handler getMath;
    private final Activity context;
    private final Intent intent;

    public GetChineseQuestion(Activity context, Intent intent) {
        this.context = context;
        this.intent = intent;
        okHttpClient = OkHttpUtils.getInstance(context);
    }

    @Override
    public void getQuestion() {
        this.getMathHandler(intent);
        new Thread(){
            @Override
            public void run() {
                super.run();
                if (UserUtil.getUser().getChineseRewardCount() <= 0) {
                    Toast.makeText(context,"今天的语文任务做完了哦",Toast.LENGTH_SHORT).show();
                } else {
                    Request request = null;
                    switch (UserUtil.getUser().getGrade()) {
                        case 1:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/ChineseOneUp").build();
                            break;
                        case 2:
                            request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/answer/ChineseOneDown").build();
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
                            OkHttpUtils.unauthorized(response.code());
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
    public void getMathHandler(final Intent intent){
        getMath = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String data = (String)msg.obj;
                if(!data.equals("Fail") && !data.equals("") && msg.arg1 == 200) {
                    Type type = new TypeToken<List<Chinese>>() {
                    }.getType();
                    System.out.println(data);
                    list = gson.fromJson(data, type);
                    if(list != null){
                        intent.putExtra("chinese",(Serializable) list);
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
