package com.li.knowledgefarm.Jpush;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.CustomMessage;
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
        Log.e("自定义消息：",title+","+message+","+extra);
    }
}
