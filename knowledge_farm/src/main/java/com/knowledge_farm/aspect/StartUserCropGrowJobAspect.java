package com.knowledge_farm.aspect;

import com.knowledge_farm.util.UserCropGrowJob;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName StartUserCropGrowAspect
 * @Description
 * @Author 张帅华
 * @Date 2020-04-25 13:17
 */
@Component
@Aspect
public class StartUserCropGrowJobAspect {
    @Resource
    private Scheduler scheduler;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut(value = "execution(* com.knowledge_farm.user.controller.UserController.raiseCrop(..))")
    private void raiseCrop() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user.controller.UserController.waterCrop(..))")
    private void waterCrop() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.front.land.controller.LandController.editLand(..))")
    private void editLand() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_friend.controller.UserFriendController.waterForFriend(..))")
    private void waterForFriend() {
    }

    @AfterReturning(pointcut = "raiseCrop()", returning="result")
    public void raiseCrop(JoinPoint joinPoint, Object result) throws SchedulerException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Integer start[] = (Integer[]) request.getAttribute("StartUserCropGrowJob");
        if(start != null){
            startJob(scheduler, start[0], start[1], start[2]);
            return;
        }
        logger.info("种植作物失败");
    }

    @AfterReturning(pointcut = "waterCrop()", returning="result")
    public void water(JoinPoint joinPoint, Object result) throws SchedulerException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Integer start[] = (Integer[]) request.getAttribute("StartUserCropGrowJob");
        if(start != null){
            startJob(scheduler, start[0], start[1], start[2]);
            return;
        }
        logger.info("浇水失败");
    }

    @AfterReturning(pointcut = "editLand()", returning="result")
    public void editLand(JoinPoint joinPoint, Object result) throws SchedulerException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Integer start[] = (Integer[]) request.getAttribute("StartUserCropGrowJob");
        if(start != null){
            startJob(scheduler, start[0], start[1], start[2]);
            return;
        }
        logger.info("种植作物失败");
    }

    @AfterReturning(pointcut = "waterForFriend()", returning="result")
    public void waterForFriend(JoinPoint joinPoint, Object result) throws SchedulerException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Integer start[] = (Integer[]) request.getAttribute("StartUserCropGrowJob");
        if(start != null){
            startJob(scheduler, start[0], start[1], start[2]);
            return;
        }
        logger.info("给好友浇水失败");
    }

    /**
     * @Author 张帅华
     * @Description 开启自动生长、干旱湿润状态变换Job
     * @Date 23:31 2020/4/10 0010
     * @Param [scheduler, id]
     * @return void
     **/
    public void startJob(Scheduler scheduler, Integer userId, Integer userCropId, Integer land) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        String name = "job" + userId + "_" + land;
        String group = "group" + userId + "_" + land;
        JobDetail jobDetail = JobBuilder.newJob(UserCropGrowJob.class).withIdentity(name, group).build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("userId", userId);
        jobDataMap.put("userCropId", userCropId);
        jobDataMap.put("land", land);
        // 基于表达式构建触发器
        Date date = new Date();
        int hour = date.getHours();
        int minute = date.getMinutes();
        int second = date.getSeconds();
        hour = ((hour + 1) == 24) ? 0 : (hour + 1);
        String trigger = second + " " + minute + " " + hour + "/1 * * ?";
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(trigger);
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

}
