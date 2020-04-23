package com.knowledge_farm.aspect;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @ClassName Md5Aspect
 * @Description
 * @Author 张帅华
 * @Date 2020-04-22 21:40
 */
@Component
@Aspect
public class Md5Aspect {
//    @Before("execution(* com.knowledge_farm.front.admin.controller.AdminController.updateAdminPassword(..))")
//    public void before(Joinpoint joinpoint){
//        System.out.println("前置");
//    }

}
