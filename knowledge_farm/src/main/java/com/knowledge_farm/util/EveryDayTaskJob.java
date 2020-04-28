package com.knowledge_farm.util;

import com.knowledge_farm.jpush.service.JpushService;
import com.knowledge_farm.task.service.TaskService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @program: knowledge_farm
 * @description: 每日签到
 * @author: 景光赞
 * @create: 2020-04-26 18:19
 **/
@Transactional(readOnly = false)
public class EveryDayTaskJob extends QuartzJobBean{
    @Resource
    private TaskService taskService;
    @Resource
    private JpushService jpushService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            this.taskService.updateTaskEveryDay();
            this.jpushService.sendCustomPush("task", "", new HashMap<>(), "");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
