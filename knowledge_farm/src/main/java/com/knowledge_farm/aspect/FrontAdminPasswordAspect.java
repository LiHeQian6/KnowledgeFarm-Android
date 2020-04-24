package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.Admin;
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
public class FrontAdminPasswordAspect {
    @Pointcut(value = "execution(* com.knowledge_farm.front.admin.controller.AdminController.*(..))")
    private void admin() {
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
                admin.setPassword("");
            }
            request.setAttribute("adminPage", pageUtil);
        }
    }

}
