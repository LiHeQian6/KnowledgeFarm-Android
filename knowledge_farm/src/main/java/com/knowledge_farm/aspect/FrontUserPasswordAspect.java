package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.Land;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.util.PageUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName UserPasswordAndPhotoAspect
 * @Description
 * @Author 张帅华
 * @Date 2020-04-24 08:45
 */
@Component
@Aspect
public class FrontUserPasswordAspect {

    @Pointcut(value = "execution(* com.knowledge_farm.front.user.controller.FrontUserController.*(..))")
    private void user() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.front.land.controller.LandController.*(..))")
    private void land() {
    }

    @AfterReturning(pointcut = "user()", returning="result")
    public void frontUserPassword(JoinPoint joinPoint, Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        User user = (User) request.getAttribute("user");
        if(user != null){
            user.setPassword("");
            request.setAttribute("user", user);
        }

        PageUtil<User> pageUtil = (PageUtil<User>) request.getAttribute("userPage");
        if(pageUtil != null){
            for(User user1 : pageUtil.getList()){
                user1.setPassword("");
            }
            request.setAttribute("userPage", pageUtil);
        }
    }

    @AfterReturning(pointcut = "land()", returning="result")
    public void frontLandOfUserPassword(JoinPoint joinPoint, Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        PageUtil<Land> pageUtil = (PageUtil<Land>) request.getAttribute("landPage");
        if(pageUtil != null){
            for(Land land : pageUtil.getList()){
                User user = land.getUser();
                if(user != null){
                    user.setPassword("");
                }
            }
            request.setAttribute("landPage", pageUtil);
        }

        Land land = (Land) request.getAttribute("land");
        if(land != null){
            User user = land.getUser();
            if(user != null){
                user.setPassword("");
            }
            request.setAttribute("land", land);
        }
    }

}
