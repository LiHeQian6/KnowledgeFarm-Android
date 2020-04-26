package com.li.knowledgefarm.Jpush;

import android.app.Application;

import com.li.knowledgefarm.Login.LoginActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * @author 孙建旺
 * @description
 * @date 2020/04/21 下午3:00
 */

public class Myapplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
