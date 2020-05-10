package com.li.knowledgefarm.daytask;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.Main.MainActivity;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.Task;
import com.li.knowledgefarm.entity.TaskItem;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private OkHttpClient okHttpClient;
    private ListView task;
    private Context context;
    private Gson gson;
    private Handler get_day_task;
    private View contentView;
    private ArrayList<TaskItem> tasks = new ArrayList<>();

    public DayTaskPopUpWindow(Context context) {
        super(context);
        this.context = context;
        okHttpClient = OkHttpUtils.getInstance(context);
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
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                300, context.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                500, context.getResources().getDisplayMetrics());
        this.setHeight(height);
        this.setWidth(width);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        contentView = LayoutInflater.from(context).inflate(R.layout.daytask_popup_layout,
                null, false);
        this.setContentView(contentView);
        getViews(contentView);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setFocusable(false);
        super.showAtLocation(parent, gravity, x, y);
        final View view = getContentView();
        FullScreen.hideBottomUIMenu(view);
        setFocusable(true);
        update();
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
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/task/getTask2?userId="+LoginActivity.user.getId()).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("每日任务信息", "请求失败");
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        get_day_task.sendMessage(message);
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
                Log.e("每日任务信息",message);
                if (!message.equals("Fail")){
                    Task myTask = gson.fromJson(message,Task.class);
                    initTask(myTask);
                    task.setAdapter(new DayTaskAdapter(context,R.layout.daytask_item_layout,tasks));
                }else {
                    Toast.makeText(context, "网络异常！", Toast.LENGTH_SHORT).show();
                }
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
                Method method = task.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                int status = (int) method.invoke(task);
                taskItem.setStatus(status);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            tasks.add(taskItem);
        }
        Collections.sort(tasks);
    }

}
