package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.notification.service.NotificationService;
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
 * @ClassName WelcomeAspect
 * @Description
 * @Author 张帅华
 * @Date 2020-05-06 15:52
 */
@Component
@Aspect
public class WelcomeAspect {
    @Resource
    private NotificationService notificationService;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut(value = "execution(* com.knowledge_farm.user.controller.UserController.addQQUser(..))")
    private void addQQUser() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user.controller.UserController.registerAccount(..))")
    private void registerAccount() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.front.user.controller.FrontUserController.addUser(..))")
    private void frontAddUser() {
    }

    @AfterReturning(pointcut = "addQQUser()", returning="result")
    public void addQQUser(JoinPoint joinPoint, Object result){
        if(result instanceof User){
            try {
                this.notificationService.addSystemNotification("Welcome", "欢迎您加入知识农场", new HashMap(), ((User) result).getAccount());
            }catch (Exception e){
                e.printStackTrace();
                logger.info("添加欢迎消息失败");
            }
        }
    }

    @AfterReturning(pointcut = "registerAccount()", returning="result")
    public void registerAccount(JoinPoint joinPoint, Object result){
        if(result instanceof User){
            try {
                this.notificationService.addSystemNotification("Welcome", "欢迎您加入知识农场", new HashMap(), ((User) result).getAccount());
            }catch (Exception e){
                e.printStackTrace();
                logger.info("添加欢迎消息失败");
            }
        }
    }

    @AfterReturning(pointcut = "frontAddUser()", returning="result")
    public void frontAddUser(JoinPoint joinPoint, Object result){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if(result == Result.SUCCEED){
            try {
                String account = (String) request.getAttribute("account");
                this.notificationService.addSystemNotification("Welcome", "欢迎您加入知识农场", new HashMap(), account);
            }catch (Exception e){
                e.printStackTrace();
                logger.info("添加欢迎消息失败");
            }
            return;
        }
        logger.info("添加用户失败");
    }

}
