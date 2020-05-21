package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserPetHouse;
import com.knowledge_farm.pet.service.PetService;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.user_pet_house.service.UserPetHouseService;
import com.knowledge_farm.util.PetFunctionGrowJob;
import com.knowledge_farm.util.PetFunctionHarvestJob;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName PetFunctionAspect
 * @Description
 * @Author 张帅华
 * @Date 2020-05-20 20:36
 */
@Component
@Aspect
public class PetFunctionAspect {
    @Resource
    private UserServiceImpl userService;
    @Resource
    private UserPetHouseService userPetHouseService;
    @Resource
    private Scheduler scheduler;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut(value = "execution(* com.knowledge_farm.pet.controller.PetController.changePet(..))")
    private void changePet() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.pet.controller.PetController.fightResult(..))")
    private void fightResult() {
    }

    @AfterReturning(pointcut = "changePet()", returning="result")
    public void changePet(JoinPoint joinPoint, Object result) {
        if(result == Result.TRUE){
            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                Integer start[] = (Integer[]) request.getAttribute("PetFunction");
                String name1 = "job" + start[0] + "_petFunctionHarvest", name2 = "job" + start[0] + "_petFunctionGrow";
                String group1 = "group" + start[0] + "_petFunctionHarvest", group2 = "group" + start[0] + "_petFunctionGrow";
                deleteJob(name1, group1);
                deleteJob(name2, group2);
                UserPetHouse userPetHouse = this.userPetHouseService.findUserPetHouseById(start[1]);
                Pet pet = userPetHouse.getPet();
                Integer harvestHour = pet.getPetFunction().getHarvestHour1();
                Integer growHour = pet.getPetFunction().getGrowHour1();
                if(harvestHour != 0){
                    switch (userPetHouse.getGrowPeriod()){
                        case 0:
                            harvestHour = userPetHouse.getPet().getPetFunction().getHarvestHour1();
                            break;
                        case 1:
                            harvestHour = userPetHouse.getPet().getPetFunction().getHarvestHour2();
                            break;
                        case 2:
                            harvestHour = userPetHouse.getPet().getPetFunction().getHarvestHour3();
                            break;
                    }
                    startJob1(scheduler, name1, group1, start[0], harvestHour);
                }
                if(growHour != 0){
                    switch (userPetHouse.getGrowPeriod()){
                        case 0:
                            growHour = userPetHouse.getPet().getPetFunction().getGrowHour1();
                            break;
                        case 1:
                            growHour = userPetHouse.getPet().getPetFunction().getGrowHour2();
                            break;
                        case 2:
                            growHour = userPetHouse.getPet().getPetFunction().getGrowHour3();
                            break;
                    }
                    startJob2(scheduler, name2, group2, start[0], growHour);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        logger.info("切换宠物失败");
    }

    @AfterReturning(pointcut = "fightResult()", returning="result")
    public void fightResult(JoinPoint joinPoint, Object result){
        if(result == Result.UP){
            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                Integer start[] = (Integer[]) request.getAttribute("PetFunction");
                String name1 = "job" + start[0] + "_petFunctionHarvest", name2 = "job" + start[0] + "_petFunctionGrow";
                String group1 = "group" + start[0] + "_petFunctionHarvest", group2 = "group" + start[0] + "_petFunctionGrow";
                deleteJob(name1, group1);
                deleteJob(name2, group2);
                User user = this.userService.findUserById(start[0]);
                Integer harvestHour = 0;
                Integer growHour = 0;
                int flag1 = 0, flag2 = 0;
                for(UserPetHouse userPetHouse : user.getPetHouses()){
                    Pet pet = userPetHouse.getPet();
                    if(userPetHouse.getIfUsing() == 1 && pet.getPetFunction().getHarvestHour1() != 0){
                        switch (userPetHouse.getGrowPeriod()){
                            case 0:
                                 harvestHour = userPetHouse.getPet().getPetFunction().getGrowHour1();
                                break;
                            case 1:
                                harvestHour = userPetHouse.getPet().getPetFunction().getGrowHour2();
                                break;
                            case 2:
                                harvestHour = userPetHouse.getPet().getPetFunction().getGrowHour3();
                                break;
                        }
                        flag1 = 1;
                        startJob1(scheduler, name1, group1, start[0], harvestHour);
                    }
                    if(userPetHouse.getIfUsing() == 1 && pet.getPetFunction().getGrowHour1() != 0){
                        switch (userPetHouse.getGrowPeriod()){
                            case 0:
                                growHour = userPetHouse.getPet().getPetFunction().getGrowHour1();
                                break;
                            case 1:
                                growHour = userPetHouse.getPet().getPetFunction().getGrowHour2();
                                break;
                            case 2:
                                growHour = userPetHouse.getPet().getPetFunction().getGrowHour3();
                                break;
                        }
                        flag2 = 1;
                        startJob2(scheduler, name2, group2, start[0], growHour);
                    }
                    if(flag1 == 1 && flag2 == 1){
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        logger.info("宠物智力值修改失败");
    }

    public void startJob1(Scheduler scheduler, String name, String group, Integer userId, Integer functionHour) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(PetFunctionHarvestJob.class).withIdentity(name, group).build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("userId", userId);
        Date date = new Date();
        int hour = date.getHours();int minute = date.getMinutes();int second = date.getSeconds();
        String trigger = "";
        int result = hour + functionHour;
        if(result < 24){
            hour = result;
        }else if(result == 24){
            hour = 0;
        }else{
            hour = hour + functionHour - 24;
        }
        trigger = second + " " + minute + " " + hour + "/" + functionHour + " * * ?";
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(trigger);
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    public void startJob2(Scheduler scheduler, String name, String group, Integer userId, Integer functionHour) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(PetFunctionGrowJob.class).withIdentity(name, group).build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("userId", userId);
        Date date = new Date();
        int hour = date.getHours();int minute = date.getMinutes();int second = date.getSeconds();
        String trigger = "";
        int result = hour + functionHour;
        if(result < 24){
            hour = result;
        }else if(result == 24){
            hour = 0;
        }else{
            hour = hour + functionHour - 24;
        }
        trigger = second + " " + minute + " " + hour + "/" + functionHour + " * * ?";
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(trigger);
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

}
