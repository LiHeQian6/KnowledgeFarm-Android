package com.knowledge_farm.util;

import com.knowledge_farm.entity.*;
import com.knowledge_farm.user.service.UserServiceImpl;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName PetFunctionGrowJob
 * @Description
 * @Author 张帅华
 * @Date 2020-05-21 17:59
 */
@Transactional(readOnly = false)
public class PetFunctionGrowJob extends QuartzJobBean {
    @Resource
    private UserServiceImpl userService;
    @Resource
    private Scheduler scheduler;
    Logger logger = LoggerFactory.getLogger(getClass());
    private Integer userId;
    private Integer growHour; //数据库内宠物功能
    private Integer functionHour; //当前任务调度的时间策略

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        Date date = new Date();
        int hour = date.getHours();
        String name = "job" + userId + "_petFunctionGrow";
        String group = "group" + userId + "_petFunctionGrow";
        userId = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get("userId");
        functionHour = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get("functionHour");
        User user = this.userService.findUserById(userId);
        List<UserCrop> userCropList = findUserCropListByLand(user.getLand());
        //生长
        for(UserCrop userCrop : userCropList){
            if(userCrop.getProgress() < userCrop.getCrop().getMatureTime() && userCrop.getStatus() != 0){
                userCrop.setProgress(userCrop.getProgress() + 1);
            }
        }
        //获得宠物功能值
        for(UserPetHouse userPetHouse : user.getPetHouses()){
            if(userPetHouse.getIfUsing() == 1){
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
                break;
            }
        }
        //正常执行
        if(functionHour == growHour && (hour + growHour < 24)){
            return;
        }
        //跨越0点
        if(functionHour == growHour && (hour + growHour >= 24)){
            deleteJob(name, group);
        }
        try {
            startJob(scheduler, name, group, userId, growHour);
        } catch (SchedulerException e) {
            e.printStackTrace();
            logger.info("宠物促进作物生长功能开启新任务调度失败");
        }
    }

    public void startJob(Scheduler scheduler, String name, String group, Integer userId, Integer functionHour) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(PetFunctionGrowJob.class).withIdentity(name, group).build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("userId", userId);
        jobDataMap.put("functionHour", functionHour);
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

    public void deleteJob(String name, String group){
        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.deleteJob(jobKey);
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }

    public List<UserCrop> findUserCropListByLand(Land land){
        List<UserCrop> userCrops = new ArrayList<>();
        UserCrop userCrop1 = land.getUserCrop1();
        UserCrop userCrop2 = land.getUserCrop2();
        UserCrop userCrop3 = land.getUserCrop3();
        UserCrop userCrop4 = land.getUserCrop4();
        UserCrop userCrop5 = land.getUserCrop5();
        UserCrop userCrop6 = land.getUserCrop6();
        UserCrop userCrop7 = land.getUserCrop7();
        UserCrop userCrop8 = land.getUserCrop8();
        UserCrop userCrop9 = land.getUserCrop9();
        UserCrop userCrop10 = land.getUserCrop10();
        UserCrop userCrop11 = land.getUserCrop11();
        UserCrop userCrop12 = land.getUserCrop12();
        UserCrop userCrop13 = land.getUserCrop13();
        UserCrop userCrop14 = land.getUserCrop14();
        UserCrop userCrop15 = land.getUserCrop15();
        UserCrop userCrop16 = land.getUserCrop16();
        UserCrop userCrop17 = land.getUserCrop17();
        UserCrop userCrop18 = land.getUserCrop18();
        if(userCrop1 != null && userCrop1.getCrop() != null){
            userCrops.add(userCrop1);
        }
        if(userCrop2 != null && userCrop2.getCrop() != null){
            userCrops.add(userCrop2);
        }
        if(userCrop3 != null && userCrop3.getCrop() != null){
            userCrops.add(userCrop3);
        }
        if(userCrop4 != null && userCrop4.getCrop() != null){
            userCrops.add(userCrop4);
        }
        if(userCrop5 != null && userCrop5.getCrop() != null){
            userCrops.add(userCrop5);
        }
        if(userCrop6 != null && userCrop6.getCrop() != null){
            userCrops.add(userCrop6);
        }
        if(userCrop7 != null && userCrop7.getCrop() != null){
            userCrops.add(userCrop7);
        }
        if(userCrop8 != null && userCrop8.getCrop() != null){
            userCrops.add(userCrop8);
        }
        if(userCrop9 != null && userCrop9.getCrop() != null){
            userCrops.add(userCrop9);
        }
        if(userCrop10 != null && userCrop10.getCrop() != null){
            userCrops.add(userCrop10);
        }
        if(userCrop11 != null && userCrop11.getCrop() != null){
            userCrops.add(userCrop11);
        }
        if(userCrop12 != null && userCrop12.getCrop() != null){
            userCrops.add(userCrop12);
        }
        if(userCrop13 != null && userCrop13.getCrop() != null){
            userCrops.add(userCrop13);
        }
        if(userCrop14 != null && userCrop14.getCrop() != null){
            userCrops.add(userCrop14);
        }
        if(userCrop15 != null && userCrop15.getCrop() != null){
            userCrops.add(userCrop15);
        }
        if(userCrop16 != null && userCrop16.getCrop() != null){
            userCrops.add(userCrop16);
        }
        if(userCrop17 != null && userCrop17.getCrop() != null){
            userCrops.add(userCrop17);
        }
        if(userCrop18 != null && userCrop18.getCrop() != null){
            userCrops.add(userCrop18);
        }
        return userCrops;
    }


}
