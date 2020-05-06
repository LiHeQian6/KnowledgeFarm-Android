package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.Admin;
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
import javax.servlet.http.HttpSession;

/**
 * @ClassName FrontAdminPasswordAspect
 * @Description
 * @Author 张帅华
 * @Date 2020-04-24 09:06
 */
@Component
@Aspect
public class FrontPasswordAspect {
    @Pointcut(value = "execution(* com.knowledge_farm.front.admin.controller.AdminController.*(..))")
    private void admin() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.front.user.controller.FrontUserController.*(..))")
    private void user() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.front.land.controller.LandController.*(..))")
    private void land() {
    }

    @AfterReturning(pointcut = "admin()", returning="result")
    public void admin(JoinPoint joinPoint, Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();

        Admin adminInfo = (Admin) request.getAttribute("adminInfo");
        if(adminInfo != null){
            adminInfo.setPassword("");
            request.setAttribute("adminInfo", adminInfo);
        }

        Admin admin = (Admin) session.getAttribute("admin");
        if(admin != null){
            admin.setPassword("");
            session.setAttribute("admin", admin);
        }

        PageUtil<Admin> pageUtil = (PageUtil<Admin>) request.getAttribute("adminPage");
        if(pageUtil != null){
            for(Admin admin1 : pageUtil.getList()){
                admin1.setPassword("");
            }
            request.setAttribute("adminPage", pageUtil);
        }
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
