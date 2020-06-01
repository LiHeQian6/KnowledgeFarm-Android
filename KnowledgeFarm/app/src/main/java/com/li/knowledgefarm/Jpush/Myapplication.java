package com.li.knowledgefarm.Jpush;

import android.app.Application;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.Util.OKHttpUpdateHttpService;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.tip.ToastUtils;

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
        XUpdate.get()
                .isWifiOnly(false)     //默认设置只在wifi下检查版本更新
                .isGet(true)          //默认设置使用get请求检查版本
                .isAutoMode(false)    //默认设置非自动模式，可根据具体使用配置
                .param("VersionCode", UpdateUtils.getVersionCode(this)) //设置默认公共请求参数
                .param("AppKey", this.getPackageName())
//                .debug(true)
                .setOnUpdateFailureListener(new OnUpdateFailureListener() { //设置版本更新出错的监听
                    @Override
                    public void onFailure(UpdateError error) {
                        ToastUtils.toast(error.toString());
                    }
                })
                .supportSilentInstall(false)
                .setIUpdateHttpService(new OKHttpUpdateHttpService()) //这个必须设置！实现网络请求功能。
                .init(this);
        XUtil.init(this);
    }
}
