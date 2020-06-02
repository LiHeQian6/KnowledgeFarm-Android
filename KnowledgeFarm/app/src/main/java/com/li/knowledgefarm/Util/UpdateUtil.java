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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.R;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.service.OnFileDownloadListener;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateUtil {

    private static Handler check_update;
    public static int isStart=0;

    public static void checkUpdate(final Context context){
        isStart=1;
        new Thread(){
            @Override
            public void run() {
                super.run();
                PackageInfo pi=null;
                try {
                    pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Request request = new Request.Builder()
                        .url(context.getResources().getString(R.string.URL)+"/user/checkVersion?version="+pi.versionName).build();
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
                        Type type = new TypeToken<Map<String,String>>() {}.getType();
                        Map<String, String> apkInfo =new Gson().fromJson(message, type);
                        apkInfo.put("url",context.getResources().getString(R.string.URL)+"/user/downloadVersion");
                        ifUpdate(context,apkInfo);
                    }else{
                        if (isStart!=0){
                            CustomerToast.getInstance(context, "已经是最新版本啦！", Toast.LENGTH_SHORT).show();
                        }
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
     * @param info
     * @return void
     * @Description 询问是否进行更新
     * @Date 15:21 2020/5/31
     **/
    private static void ifUpdate(final Context context, final Map<String, String> info) {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
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
        waring.setText("检查到有新的版本是否进行更新？\n\n当前版本："+pi.versionName+"\n最新版本："+info.get("versionName")+"\n更新内容："+info.get("description"));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XUpdate.newBuild(context)
                        .apkCacheDir(PathUtils.getAppExtDownloadPath()) //设置下载缓存的根目录
                        .build()
                        .download(info.get("url"), new OnFileDownloadListener() {   //设置下载的地址和下载的监听
                            @Override
                            public void onStart() {
                                HProgressDialogUtils.showHorizontalProgressDialog(context, "下载进度", false);
                            }

                            @Override
                            public void onProgress(float progress, long total) {
                                HProgressDialogUtils.setProgress(Math.round(progress*100));
                            }

                            @Override
                            public boolean onCompleted(File file) {
                                HProgressDialogUtils.cancel();
//                                ToastUtils.toast("apk下载完毕，文件路径：" + file.getPath());
                                _XUpdate.startInstallApk(context, FileUtils.getFileByPath(file.getPath())); //填写文件所在的路径
                                return false;
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                HProgressDialogUtils.cancel();
                            }
                        });
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
