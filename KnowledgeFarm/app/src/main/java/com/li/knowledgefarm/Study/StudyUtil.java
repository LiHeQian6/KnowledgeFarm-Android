package com.li.knowledgefarm.Study;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.WindowManager;

import com.li.knowledgefarm.R;

import java.io.IOException;

import androidx.core.content.ContextCompat;

/**
 * @auther 孙建旺
 * @description 学习页面工具类
 * @date 2020/03/17 下午 2:55
 */

public class StudyUtil {

    /**
     * @Description 播放回答正确音效
     * @Auther 孙建旺
     * @Date 下午 3:04 2020/03/17
     * @Param [context]
     * @return void
     */
    public static void PlayTrueSound(Context context){
        MediaPlayer player = new MediaPlayer();
        AssetFileDescriptor file = context.getResources().openRawResourceFd(R.raw.yinxiao1041);
        try {
            player.setDataSource(file.getFileDescriptor(),file.getStartOffset(),file.getLength());
            file.close();
            if(!player.isPlaying()){
                player.prepare();
                player.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 播放回答错误音效
     * @Auther 孙建旺
     * @Date 下午 3:04 2020/03/17
     * @Param [context]
     * @return void
     */
    public static void PlayFalseSound(Context context){
        MediaPlayer player = new MediaPlayer();
        AssetFileDescriptor file = context.getResources().openRawResourceFd(R.raw.cuowu);
        try {
            player.setDataSource(file.getFileDescriptor(),file.getStartOffset(),file.getLength());
            file.close();
            if(!player.isPlaying()){
                player.prepare();
                player.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 设置标题栏
     * @Auther 孙建旺
     * @Date 下午 3:23 2020/03/17
     * @Param [activity]
     * @return void
     */
    public static void setStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色
        }
    }
}
