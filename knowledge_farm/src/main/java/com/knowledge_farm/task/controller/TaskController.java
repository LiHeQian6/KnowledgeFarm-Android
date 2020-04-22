package com.knowledge_farm.task.controller;

import com.knowledge_farm.entity.Task;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.task.service.TaskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public Task getTasks(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        return taskService.findTask(user);
    }
    /**
     * @description:  领取任务奖励
     * @author :景光赞
     * @date :2020/4/22 12:28
     * @param :[taskName, request]
     * @return :int
     */
    @RequestMapping("/getReward")
    public int getReward(@RequestParam("taskName") String taskName,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        return taskService.updateTask(user,taskName);
    }

}
