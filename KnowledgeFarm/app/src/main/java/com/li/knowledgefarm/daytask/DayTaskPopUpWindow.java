package com.li.knowledgefarm.daytask;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.entity.Task;
import com.li.knowledgefarm.entity.TaskItem;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author li
 * @Description
 * @Date 10:49 2020/4/22
 **/
public class DayTaskPopUpWindow extends PopupWindow {
    private ListView task;
    private ListView tasked;
    private Context context;
    private Gson gson;
    private Handler get_day_task;
    private View contentView;
    private ArrayList<TaskItem> tasks = new ArrayList<>();
    private ArrayList<TaskItem> taskeds = new ArrayList<>();

    public DayTaskPopUpWindow(Context context) {
        super(context);
        this.context = context;
        Init();
        getDayTask();
    }

    /**
     * @Author li
     * @param
     * @return void
     * @Description 初始化弹出框布局
     * @Date 10:52 2020/4/22
     **/
    private void Init(){
        this.setHeight(900);
        this.setWidth(1600);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        contentView = LayoutInflater.from(context).inflate(R.layout.daytask_popup_layout,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
    }

    /**
     * @Author li
     * @param view
     * @return void
     * @Description 获取控件
     * @Date 10:58 2020/4/22
     **/
    private void getViews(View view){
        gson = new Gson();
        task=contentView.findViewById(R.id.task);
        tasked=contentView.findViewById(R.id.tasked);

    }

    /**
     * @Author li
     * @return void
     * @Description 获取每日任务信息
     * @Date 10:53 2020/4/22
     **/
    private void getDayTask(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FormBody formBody = new FormBody.Builder()
                        .add("userId", LoginActivity.user.getId()+"").build();
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(context.getResources().getString(R.string.URL)+"/task/getTask").build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("每日任务信息", "请求失败");
                        Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        get_day_task.sendMessage(message);
                    }
                });
            }
        }.start();
        get_day_task = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
//                Task myTask = gson.fromJson(message,Task.class);
                Task task1 = new Task();
                task1.setSignIn(0);
                task1.setCrop(0);
                task1.setFertilize(0);
                task1.setHarvest(0);
                task1.setHelpFertilize(0);
                task1.setHelpWater(2);
                task1.setWater(1);
//                initTask(myTask);
                initTask(task1);
                task.setAdapter(new DayTaskAdapter(context,R.layout.daytask_item_layout,tasks));
                tasked.setAdapter(new DayTaskAdapter(context,R.layout.daytask_item_layout,taskeds));
            }
        };
    }

    /**
     * @Author li
     * @param task
     * @return void
     * @Description 初始化任务数据
     * @Date 11:56 2020/4/22
     **/
    public void initTask(Task task){
        Field[] fields = task.getClass().getDeclaredFields();
        for (int i = fields.length - 1; i >= 0; i--) {
            TaskItem taskItem = new TaskItem();
            String name = fields[i].getName();
            taskItem.setType(name);
            try {
                Method method = task.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1), null);
                taskItem.setStatus((Integer) method.invoke(task,null));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (taskItem.getStatus()==2){
                taskeds.add(taskItem);
            }else{
                tasks.add(taskItem);
            }
        }
    }

}
