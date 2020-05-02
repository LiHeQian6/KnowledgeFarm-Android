package com.knowledge_farm.front.notification.service;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.knowledge_farm.entity.JpushConfig;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.jpush.service.JpushService;
import com.knowledge_farm.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @ClassName FrontNotificationService
 * @Description
 * @Author 张帅华
 * @Date 2020-04-30 16:47
 */
@Service
@Transactional(readOnly = true)
public class FrontNotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(JpushService.class);
    @Resource
    private JpushConfig jpushConfig;// 注入配置信息
    @Resource
    private JpushService jpushService;
    @Resource
    private NotificationService notificationService;

    /**
     * 发送自定义推送，由APP端拦截信息后再决定是否创建通知(目前APP用此种方式)
     *
     * @param title     App通知栏标题
     * @param content   App通知栏内容（为了单行显示全，尽量保持在22个汉字以下）
     * @param extrasMap 额外推送信息（不会显示在通知栏，传递数据用）
     * @param alias     别名数组，设定哪些用户手机能接收信息（为空则所有用户都推送）
     * @return PushResult
     */
    @Transactional(readOnly = false)
    public PushResult sendCustomMessage(JPushClient jPushClient, PushPayload pushPayload, String title, String content, Map extrasMap, String... alias) throws Exception{
        PushResult result = jPushClient.sendPush(pushPayload);
        this.notificationService.addSystemNotification(title, content, extrasMap, alias);
        return result;
    }

}
