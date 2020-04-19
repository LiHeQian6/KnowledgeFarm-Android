package com.knowledge_farm.task.controller;

import com.knowledge_farm.entity.Task;
import com.knowledge_farm.task.service.TaskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: knowledge_farm
 * @description: 每日任务
 * @author: 景光赞
 * @create: 2020-04-19 14:48
 **/
@RestController
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskService taskService;

    /**
     * @description: 查询所有任务
     * @author :景光赞
     * @date :2020/4/19 16:23
     * @param :[userId]
     * @return :com.knowledge_farm.entity.Task
     */
    @RequestMapping("/getTask")
    public Task getTasks(@RequestParam("userId") int userId) {
        return taskService.findTask(userId);
    }

    /**
     * @description: 完成任务，改变任务的值=1
     * @author :景光赞
     * @date :2020/4/19 16:23
     * @param :[userId, taskName]
     * @return :int
     */
    @RequestMapping("/finish")
    public int finishTask(@RequestParam("userId") int userId, @RequestParam("taskName") String taskName) {
        return taskService.finishTask(userId, taskName);
    }

    /**
     * @description: 领取任务奖励，改变任务值=2
     * @author :景光赞
     * @date :2020/4/19 16:24
     * @param :[userId, taskName]
     * @return :int
     */
    @RequestMapping("/getReward")
    public int getReward(@RequestParam("userId") int userId, @RequestParam("taskName") String taskName) {
        return taskService.updateTask(userId, taskName);
    }

    @RequestMapping("/test")
    public int getReward2() {
        return taskService.finishTask(109, "sign_in");
    }
}
