package com.knowledge_farm.util;

import com.knowledge_farm.entity.*;
import com.knowledge_farm.user.service.UserServiceImpl;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName PetFunctionHarvestJob
 * @Description
 * @Author 张帅华
 * @Date 2020-05-21 09:47
 */
@Transactional(readOnly = false)
public class PetFunctionHarvestJob extends QuartzJobBean {
    @Resource
    private UserServiceImpl userService;
    @Resource
    private Scheduler scheduler;
    @Value("${level.experienceList}")
    private Integer experienceList[];
    Logger logger = LoggerFactory.getLogger(getClass());
    private Integer userId;
    private Integer harvestHour;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        Date date = new Date();
        int hour = date.getHours();
        int minute = date.getMinutes();
        int second = date.getSeconds();
        String trigger = "";
        userId = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get("userId");
        User user = this.userService.findUserById(userId);
        List<UserCrop> userCropList = findUserCropListByLand(user.getLand());
        for(UserCrop userCrop : userCropList){
            Crop crop = userCrop.getCrop();
            if(userCrop.getProgress() >= crop.getMatureTime()){
                userCrop.setCrop(null);
                userCrop.setProgress(0);
                userCrop.setWaterLimit(15);
                userCrop.setFertilizerLimit(15);
                userCrop.setStatus(1);
                int value = crop.getValue();
                int experience = crop.getExperience();
                int userMoney = user.getMoney();
                int userLevel = user.getLevel();
                int userExperience = user.getExperience();
                if(userLevel < experienceList.length-1){
                    if(userExperience + experience >= experienceList[userLevel]){
                        user.setLevel(userLevel + 1);
                    }
                    user.setExperience(userExperience + experience);
                }else{
                    if(userExperience + experience <= experienceList[userLevel]){
                        user.setExperience(userExperience + experience);
                    }else{
                        user.setExperience(experienceList[userLevel]);
                    }
                }
                user.setMoney(userMoney + value);
            }
        }
        for(UserPetHouse userPetHouse : user.getPetHouses()){
            if(userPetHouse.getIfUsing() == 1){
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
                break;
            }
        }
        if(hour + harvestHour >= 24){
            String name = "job" + userId + "_petFunctionHarvest";
            String group = "group" + userId + "_petFunctionHarvest";
            deleteJob(name, group);
            trigger = second + " " + minute + " " + (hour + harvestHour - 24) + "/" + harvestHour + " * * ?";
            try {
                startJob(scheduler, name, group, userId, trigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
                logger.info("宠物收获作物功能开启新任务调度失败");
            }
        }
    }

    public void startJob(Scheduler scheduler, String name, String group, Integer userId, String trigger) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(PetFunctionHarvestJob.class).withIdentity(name, group).build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("userId", userId);
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
