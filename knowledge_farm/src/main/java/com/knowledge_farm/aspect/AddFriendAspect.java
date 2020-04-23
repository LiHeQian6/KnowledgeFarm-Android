package com.knowledge_farm.aspect;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.jpush.service.JpushService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Type;

import java.util.Map;

/**
 * @ClassName AddFriendAspect
 * @Description
 * @Author 张帅华
 * @Date 2020-04-22 21:02
 */
@Component
@Aspect
public class AddFriendAspect {
    @Resource
    private JpushService jpushService;

    @AfterReturning(pointcut = "execution(* com.knowledge_farm.notification.service.NotificationService.addUserFriendNotification(..))", returning="result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        Notification notification = (Notification) result;
        try {
            if(result != null){
                Type map = new TypeToken<Map<String, String>>(){}.getType();
                Map extra = new Gson().fromJson(notification.getExtra(), map);
                jpushService.sendCustomPush(notification.getTitle(), notification.getContent(), extra, notification.getTo().getAccount());
                return;
            }
            System.out.println("消息创建失败");
        }catch (Exception e){
            System.out.println("自定义消息发送失败");
        }

    }

}
