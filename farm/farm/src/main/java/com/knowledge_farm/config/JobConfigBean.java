package com.knowledge_farm.config;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.stereotype.Component;

/**
 * @ClassName JobConfigBean
 * @Description
 * @Author 张帅华
 * @Date 2020-04-09 14:07
 */
public class JobConfigBean {
    private JobDetail[] jobDetails;

    private Trigger[] triggers;

    public JobDetail[] getJobDetails() {
        return jobDetails;
    }

    public void setJobDetails(JobDetail[] jobDetails) {
        this.jobDetails = jobDetails;
    }

    public Trigger[] getTriggers() {
        return triggers;
    }

    public void setTriggers(Trigger[] triggers) {
        this.triggers = triggers;
    }

}
