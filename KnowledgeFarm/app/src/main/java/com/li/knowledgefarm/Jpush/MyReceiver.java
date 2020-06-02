package com.li.knowledgefarm.Jpush;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.li.knowledgefarm.Login.LoginActivity;
import com.li.knowledgefarm.entity.EventBean;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * @author 孙建旺
 * @description
 * @date 2020/04/21 下午3:03
 */

public class MyReceiver extends JPushMessageReceiver {

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        String title = customMessage.title;
        String message = customMessage.message;
        String extra = customMessage.extra;
        String type = customMessage.contentType;
        Log.e("自定义消息：",title+","+message+","+extra);
        EventBean eventBean = new EventBean();
        eventBean.setMessage(title);
        eventBean.setNotifyType(message);
        EventBus.getDefault().post(eventBean);
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
    }
}
