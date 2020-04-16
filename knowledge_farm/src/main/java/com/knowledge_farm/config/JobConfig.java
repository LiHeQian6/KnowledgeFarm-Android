package com.knowledge_farm.config;

import com.knowledge_farm.util.RewardCountJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobConfig
 * @Description
 * @Author 张帅华
 * @Date 2020-04-09 16:16
 */
@Configuration
public class JobConfig {
    @Resource
    private MyJobFactory myJobFactory;

    @Bean(name = "job0")
    public JobDetailFactoryBean jobDetailFactoryBean(){
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(RewardCountJob.class);
        jobDetailFactoryBean.setDurability(true);
        return jobDetailFactoryBean;
    }

    /**
     * 触发器  cronTriggerFactoryBean
     * @param jobDetailFactoryBean
     * @return
     */
    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean(@Qualifier("job0")JobDetailFactoryBean jobDetailFactoryBean){
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
        cronTriggerFactoryBean.setCronExpression("0 0 0 * * ? ");
        return cronTriggerFactoryBean;
    }

    /**
     * 将上面的jobDetails和triggers都配置到一个类中，这样便于后续定时任务的添加
     * @return
     */
    @Bean
    public JobConfigBean jobConfigBean(){
        JobConfigBean jobConfigBean = new JobConfigBean();
        List<JobDetail> jobDetails = new ArrayList<>();
        jobDetails.add(jobDetailFactoryBean().getObject());
        JobDetail[] jobDetailarr = createJobDetail(jobDetails);

        List<Trigger> triggers = new ArrayList<>();
        triggers.add(cronTriggerFactoryBean(jobDetailFactoryBean()).getObject());
        Trigger[] triggerarr = createTriggers(triggers);

        jobConfigBean.setJobDetails(jobDetailarr);
        jobConfigBean.setTriggers(triggerarr);

        return jobConfigBean;
    }

    @Bean(name = "scheduleFactory")
    public SchedulerFactoryBean schedulerFactoryBean(JobConfigBean jobConfigBean){
        System.out.println(myJobFactory);
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(myJobFactory);
        schedulerFactoryBean.setJobDetails(jobConfigBean.getJobDetails());
        schedulerFactoryBean.setTriggers(jobConfigBean.getTriggers());
        schedulerFactoryBean.setAutoStartup(true);
        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler(@Qualifier("scheduleFactory") SchedulerFactoryBean schedulerFactoryBean){
        return schedulerFactoryBean.getScheduler();
    }

    public static JobDetail[] createJobDetail(List<JobDetail> jobDetails){
        JobDetail[] jobDetailArr = new JobDetail[jobDetails.size()];
        for (int i = 0; i < jobDetails.size(); i++) {
            jobDetailArr[i] = jobDetails.get(i);
        }
        return jobDetailArr;
    }

    public static Trigger[] createTriggers(List<Trigger> triggers){
        Trigger[] triggerarr = new Trigger[triggers.size()];
        for (int i = 0; i <triggers.size() ; i++) {
            triggerarr[i] = triggers.get(i);
        }
        return triggerarr;
    }

}
