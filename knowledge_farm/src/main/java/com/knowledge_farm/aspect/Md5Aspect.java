package com.knowledge_farm.aspect;

import com.knowledge_farm.util.Md5Encode;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName Md5Aspect
 * @Description
 * @Author 张帅华
 * @Date 2020-04-22 21:40
 */
@Component
@Aspect
public class Md5Aspect {

    @Pointcut(value = "execution(* com.knowledge_farm.front.admin.controller.AdminController.updateAdminPassword(..))")
    private void adminPassword() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.front.user.controller.FrontUserController.updateUserPassword(..))")
    private void userPassword() {
    }

//    @Before(value = "adminPasswordToMd5()")
//    public void before(JoinPoint joinPoint){
//        Object args[] = joinPoint.getArgs();
//        System.out.println(args[0] + " " +args[1]);
//        args[1] = Md5Encode.getMD5(args[1].toString().getBytes());
//        System.out.println(args[0] + " " +args[1]);
//    }

    @Around(value = "adminPassword()")
    public Object adminPasswordToMd5(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        args[1] = Md5Encode.getMD5(args[1].toString().getBytes());
        Object result = null;
        try {
            result = joinPoint.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    @Around(value = "userPassword()")
    public Object userPasswordToMd5(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        args[1] = Md5Encode.getMD5(args[1].toString().getBytes());
        Object result = null;
        try {
            result = joinPoint.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

}
