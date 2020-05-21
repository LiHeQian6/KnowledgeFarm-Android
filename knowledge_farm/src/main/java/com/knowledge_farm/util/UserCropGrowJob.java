package com.knowledge_farm.util;

import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.entity.UserCrop;
import com.knowledge_farm.user_crop.service.UserCropServiceImpl;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName UserCropGrowJob
 * @Description
 * @Author 张帅华
 * @Date 2020-04-09 16:41
 */
@Transactional(readOnly = false)
public class UserCropGrowJob extends QuartzJobBean {
    @Resource
    private Scheduler scheduler;
    @Resource
    private UserCropServiceImpl userCropService;
    Logger logger = LoggerFactory.getLogger(getClass());
    private Integer userId;
    private Integer userCropId;
    private Integer land;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        CronTriggerImpl cronTrigger = (CronTriggerImpl) jobExecutionContext.getTrigger();
        long count = (new Date().getTime() - cronTrigger.getStartTime().getTime())/1000/60/60;
        if(count != 0){
            userId = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get("userId");
            userCropId = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get("userCropId");
            land = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get("land");

            UserCrop userCrop = this.userCropService.findUserCropById(userCropId);
            Crop crop = userCrop.getCrop();
            int progress = userCrop.getProgress();
            int matureTime = crop.getMatureTime();
            if (progress < matureTime) {
                userCrop.setProgress(progress + 1);
                if (count % 3 == 0 && count != 0) {
                    int rand = (int) (Math.random() * 100);
                    logger.info(userId + "随机数：" + rand);
                    if (rand <= 25) {
                        userCrop.setStatus(0);
                        this.userCropService.save(userCrop);
                        deleteJob("job" + userId + "_" + land, "group" + userId + "_" + land);
                        return;
                    }
                }
                this.userCropService.save(userCrop);
                logger.info(userId + "第" + count + "次");
                return;
            }
            deleteJob("job" + userId + "_" + land, "group" + userId + "_" + land);
        }
    }

    public void deleteJob(String name, String group){
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = null;
        try {
            jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
