package com.knowledge_farm.util;

import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.entity.UserCrop;
import com.knowledge_farm.usercrop.service.UserCropServiceImpl;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName UserCropGrowJob
 * @Description
 * @Author 张帅华
 * @Date 2020-04-09 16:41
 */
public class UserCropGrowJob extends QuartzJobBean {
    @Resource
    private Scheduler scheduler;
    @Resource
    private UserCropServiceImpl userCropService;
    private Integer userId;
    private Integer userCropId;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        CronTriggerImpl cronTrigger = (CronTriggerImpl) jobExecutionContext.getTrigger();
        long count = (new Date().getTime() - cronTrigger.getStartTime().getTime())/1000/60/60;
        userId = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get("userId");
        userCropId = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get("userCropId");
        UserCrop userCrop = this.userCropService.findUserCropById(userCropId);
        Crop crop = userCrop.getCrop();
        try{
            if(crop != null) {
                int progress = userCrop.getProgress();
                int matureTime = crop.getMatureTime();
                if (progress < matureTime) {
                    if (count % 3 == 0 && count != 0) {
                        int rand = (int) (Math.random() * 100);
                        System.out.println("随机数：" + rand);
                        if (rand <= 25) {
                            userCrop.setStatus(0);
                            this.userCropService.save(userCrop);
                            deleteJob("job" + userId, "group" + userId);
                            return;
                        }
                    }
                    userCrop.setProgress(progress + 1);
                    this.userCropService.save(userCrop);
                    System.out.println(count);
                    return;
                }
            }
            deleteJob("job" + userId, "group" + userId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

}
