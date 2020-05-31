package com.li.knowledgefarm.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.li.knowledgefarm.R;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import androidkun.com.versionupdatelibrary.entity.VersionUpdateConfig;
import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateUtil {

    private static Handler check_update;
    private static int isStart=0;

    public static void checkUpdate(final Context context){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/userpethouse/showUserPetHouse").build();
                Call call = OkHttpUtils.getInstance(context).newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("检查更新", "请求失败");
                        Message message = Message.obtain();
                        message.obj = "Fail";
                        check_update.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String notify_message = response.body().string();
                        Message message = Message.obtain();
                        message.obj = notify_message;
                        check_update.sendMessage(message);
                    }
                });
            }
        }.start();
        check_update = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String)msg.obj;
                Log.e("检查更新",message);
                if (!message.equals("Fail")){
                    if (!message.equals("false")){
//                        ifUpdate(context,"xxx");
                    }else{
                        if (isStart!=0){
                            CustomerToast.getInstance(context, "已经是最新版本啦！", Toast.LENGTH_SHORT).show();
                        }
                        isStart=1;
                    }
                }else {
                    CustomerToast.getInstance(context, "网络异常！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * @Author li
     * @param context
     * @return void
     * @Description 询问有新版本是否进行更新
     * @Date 11:30 2020/5/31
     **/
    private static void ifUpdate(final Context context, final String url) {
        final Dialog dialog = new Dialog(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.math_return_dialog, null);
        ImageView cancel = layout.findViewById(R.id.cancel_return);
        ImageView sure = layout.findViewById(R.id.sure_return);
        TextView waring = layout.findViewById(R.id.waringText);
//        setDialogSize(layout);
        PackageInfo pi=null;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        waring.setText("检查到有新的版本是否进行更新？\n当前版本："+pi.versionName+"\n最新版本：");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersionUpdateConfig.getInstance()//获取配置实例
                        .setContext(context)//设置上下文
                        .setDownLoadURL(url)//设置文件下载链接
                        .setNewVersion("1.0.1")//设置即将下载的APK的版本号,避免重复下载
//                        .setFileSavePath(savePath)//设置文件保存路径（可不设置）
                        .setNotificationIconRes(R.drawable.farmimg)//设置通知图标
                        .setNotificationSmallIconRes(R.drawable.farmimg)//设置通知小图标
                        .setNotificationTitle("知识农场正在更新...")//设置通知标题
                        .startDownLoad();//开始下载
            }
        });
        dialog.setContentView(layout);
        NavigationBarUtil.focusNotAle(dialog.getWindow());
        dialog.show();
        NavigationBarUtil.hideNavigationBar(dialog.getWindow());
        NavigationBarUtil.clearFocusNotAle(dialog.getWindow());
        WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
        if (dialog.getWindow() != null) {
            //bagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0f);//去除遮罩
        }
        attrs.gravity = Gravity.CENTER;
        final float scale = context.getResources().getDisplayMetrics().density;
        attrs.width = (int) (300 * scale + 0.5f);
        attrs.height = (int) (300 * scale + 0.5f);
        dialog.getWindow().setAttributes(attrs);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

}
