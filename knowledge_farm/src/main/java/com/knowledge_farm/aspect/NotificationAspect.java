package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.jpush.service.JpushService;
import com.knowledge_farm.user.service.UserServiceImpl;
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

import java.util.HashMap;

/**
 * @ClassName AddFriendAspect
 * @Description
 * @Author 张帅华
 * @Date 2020-04-22 21:02
 */
@Component
@Aspect
public class NotificationAspect {
    @Resource
    private UserServiceImpl userService;
    @Resource
    private JpushService jpushService;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut(value = "execution(* com.knowledge_farm.notification.controller.NotificationController.addUserFriendNotification(..))")
    private void addUserFriendNotification() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_friend.controller.UserFriendController.addUserFriend(..))")
    private void addUserFriend() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_friend.controller.UserFriendController.refuseUserFriend(..))")
    private void refuseUserFriend() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_friend.controller.UserFriendController.waterForFriend(..))")
    private void waterForFriend() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_friend.controller.UserFriendController.fertilizerForFriend(..))")
    private void fertilizerForFriend() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.pet.controller.PetController.fightResult(..))")
    private void fightResult() {
    }

    @AfterReturning(pointcut = "addUserFriendNotification()", returning="result")
    public void addUserFriendNotification(JoinPoint joinPoint, Object result) {
        if(result == Result.TRUE){
            try {
                Object[] args = joinPoint.getArgs();
                jpushService.sendCustomPush("notification", "receive", new HashMap<>(), (String) args[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        logger.info("消息创建失败");
    }

    @AfterReturning(pointcut = "addUserFriend()", returning="result")
    public void addUserFriend(JoinPoint joinPoint, Object result) {
        if(result == Result.TRUE){
            try {
                Object[] args = joinPoint.getArgs();
                jpushService.sendCustomPush("notification", "send", new HashMap<>(), (String) args[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        logger.info("添加好友失败");
    }

    @AfterReturning(pointcut = "refuseUserFriend()", returning="result")
    public void refuseUserFriend(JoinPoint joinPoint, Object result) {
        if(result == Result.TRUE){
            try {
                Object[] args = joinPoint.getArgs();
                jpushService.sendCustomPush("notification", "send", new HashMap<>(), (String) args[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        logger.info("拒绝好友申请失败");
    }

    @AfterReturning(pointcut = "waterForFriend()", returning="result")
    public void waterForFriend(JoinPoint joinPoint, Object result) {
        if(result == Result.TRUE){
            try {
                Object[] args = joinPoint.getArgs();
                User user = this.userService.findUserById((Integer) args[0]);
                jpushService.sendCustomPush("notification", "message", new HashMap<>(), user.getAccount());
            }catch (Exception e){
                e.printStackTrace();
                logger.info("给好友浇水后发送通知失败");
            }
            return;
        }
        logger.info("给好友浇水失败");
    }

    @AfterReturning(pointcut = "fertilizerForFriend()", returning="result")
    public void fertilizerForFriend(JoinPoint joinPoint, Object result) {
        if(result == Result.TRUE){
            try {
                Object[] args = joinPoint.getArgs();
                User user = this.userService.findUserById((Integer) args[0]);
                jpushService.sendCustomPush("notification", "message", new HashMap<>(), user.getAccount());
            }catch (Exception e){
                e.printStackTrace();
                logger.info("给好友施肥后发送通知失败");
            }
            return;
        }
        logger.info("给好友施肥失败");
    }

    @AfterReturning(pointcut = "fightResult()", returning="result")
    public void fightResult(JoinPoint joinPoint, Object result) {
        if (result != Result.FALSE) {
            try {
                Object[] args = joinPoint.getArgs();
                jpushService.sendCustomPush("notification", "message", new HashMap<>(), (String) args[0]);
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("对战结束发送通知失败");
            }
            return;
        }
        logger.info("对战结束操作失败");
    }

}
