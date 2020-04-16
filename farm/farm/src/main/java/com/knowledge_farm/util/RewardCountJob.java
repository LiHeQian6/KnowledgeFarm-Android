package com.knowledge_farm.util;

import com.knowledge_farm.user.service.UserServiceImpl;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 * @ClassName RewardCountJob
 * @Description
 * @Author 张帅华
 * @Date 2020-04-09 16:04
 */
public class RewardCountJob extends QuartzJobBean {
    @Resource
    private UserServiceImpl userService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        this.userService.updateUserRewardCount();
    }

    public UserServiceImpl getUserService() {
        return userService;
    }

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

}
