package com.li.knowledgefarm.bag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.R;
import com.li.knowledgefarm.Util.FullScreen;
import com.li.knowledgefarm.Util.OkHttpUtils;
import com.li.knowledgefarm.entity.BagCropNumber;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BagPopUpWindow extends PopupWindow {

    private int displayWidth;
    private int displayHeight;
    private Context context;
    private OkHttpClient okHttpClient;
    private Handler bagMessagesHandler;
    private Gson gson;
    private List<BagCropNumber> dataList;
    private GridView gridView;

    public BagPopUpWindow(Context context) {
        super(context);
        this.context = context;
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.notify_pop_animation);
        View contentView = LayoutInflater.from(context).inflate(R.layout.bag_girdview,
                null, false);
        this.setContentView(contentView);
        okHttpClient = OkHttpUtils.getInstance(context);
        getViews(contentView);
        Init(contentView);
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

    private void getViews(View contentView) {
        gridView = contentView.findViewById(R.id.bag_grid_view);
        gson = new Gson();
    }

    public GridView getGridView() {
        return gridView;
    }
    public List<BagCropNumber> getDataList() {
        return dataList;
    }

    /**
     * @Description 获取背包数据
     * @Author 孙建旺
     * @Date 下午4:00 2020/04/26
     * @Param [view]
     * @return void
     */
    private void Init(View view){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics ds = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(ds);
        displayHeight = ds.heightPixels;
        displayWidth = ds.widthPixels;
        LinearLayout.LayoutParams params_gridview = new LinearLayout.LayoutParams((int) (displayWidth * 0.3), (int) (displayHeight * 0.8));
        params_gridview.gravity = Gravity.CENTER_HORIZONTAL;
        params_gridview.setMargins((int) (displayWidth * 0.005), (int) (displayHeight * 0.08), 0, 0);
        gridView.setColumnWidth((int) (displayWidth * 0.2));
        gridView.setLayoutParams(params_gridview);
        gridView.setVerticalSpacing((int) (displayHeight * 0.02));
        new Thread() {
            @Override
            public void run() {
                super.run();
                final Request request = new Request.Builder().url(context.getResources().getString(R.string.URL) + "/bag/initUserBag").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        bagMessagesHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        OkHttpUtils.unauthorized(response.code());
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        bagMessagesHandler.sendMessage(message);
                    }
                });
            }
        }.start();
        bagMessagesHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String messages = (String) msg.obj;
                Log.e("背包", messages);
                if (!messages.equals("Fail")) {
                    Type type = new TypeToken<List<BagCropNumber>>() {
                    }.getType();
                    dataList = gson.fromJson(messages, type);
                    BagCustomerAdapter customerAdapter = new BagCustomerAdapter(context, dataList, R.layout.gird_adapteritem);
                    gridView.setAdapter(customerAdapter);
                } else {
                    Toast toast = Toast.makeText(context, "获取数据失败！", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
    }
}
