package com.knowledge_farm.aspect;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.jpush.service.JpushService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut(value = "execution(* com.knowledge_farm.notification.controller.NotificationController.addUserFriendNotification(..))")
    private void pointcutLog() {

    }

    @AfterReturning(pointcut = "pointcutLog()", returning="result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Notification notification = (Notification) request.getAttribute("addFriendNotification");
        if(result == Result.TRUE){
            try {
                Type map = new TypeToken<Map<String, String>>(){}.getType();
                Map<String, String> extra = new Gson().fromJson(notification.getExtra(), map);
                jpushService.sendCustomPush(notification.getTitle(), notification.getContent(), extra, notification.getTo().getAccount());
                return;
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
        logger.info("消息创建失败");
    }

}
