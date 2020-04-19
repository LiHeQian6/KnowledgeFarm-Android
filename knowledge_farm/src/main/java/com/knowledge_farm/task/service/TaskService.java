package com.knowledge_farm.task.service;

import com.knowledge_farm.entity.Task;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.task.dao.TaskDao;
import com.knowledge_farm.user.service.UserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @program: knowledge_farm
 * @description: 每日任务
 * @author: 景光赞
 * @create: 2020-04-19 12:32
 **/
@Service
@Transactional(readOnly = true)
public class TaskService {
    @Resource
    private TaskDao taskDao;
    @Resource
    private UserServiceImpl userService;

    public Task findTask(int userId){
        return taskDao.findTaskByUser(userService.findUserById(userId));
    }

    @Transactional(readOnly = false)
    public int finishTask(int userId,String taskName){
        Task task = findTask(userId);
        if(taskName.equals("sign_in")&&task.getSignIn()==0){
            task.setSignIn(1);
            return taskDao.saveAndFlush(task).getSignIn();
        }else if(taskName.equals("water")&&task.getWater()==0){
            task.setWater(1);
            return taskDao.saveAndFlush(task).getWater();
        }else if(taskName.equals("fertilize")&&task.getFertilize()==0){
            task.setFertilize(1);
            return taskDao.saveAndFlush(task).getFertilize();
        }else if(taskName.equals("crop")&&task.getCrop()==0){
            task.setCrop(1);
            return taskDao.saveAndFlush(task).getCrop();
        }else if(taskName.equals("harvest")&&task.getHarvest()==0){
            task.setHarvest(1);
            return taskDao.saveAndFlush(task).getHarvest();
        }else if(taskName.equals("help_water")&&task.getHelpWater()==0){
            task.setHelpWater(1);
            return taskDao.saveAndFlush(task).getHelpWater();
        }else if(taskName.equals("help_fertilize")&&task.getHelpFertilize()==0){
            task.setHelpFertilize(1);
            return taskDao.saveAndFlush(task).getHelpFertilize();
        }else {
            return -1;
        }
    }

    @Transactional(readOnly = false)
    public int updateTask(int userId,String taskName){
        Task task = findTask(userId);
        if(taskName.equals("sign_in")&&task.getSignIn()==1){
            task.setSignIn(2);
            return taskDao.saveAndFlush(task).getSignIn();
        }else if(taskName.equals("water")&&task.getWater()==1){
            task.setWater(2);
            return taskDao.saveAndFlush(task).getWater();
        }else if(taskName.equals("fertilize")&&task.getFertilize()==1){
            task.setFertilize(2);
            return taskDao.saveAndFlush(task).getFertilize();
        }else if(taskName.equals("crop")&&task.getCrop()==1){
            task.setCrop(2);
            return taskDao.saveAndFlush(task).getCrop();
        }else if(taskName.equals("harvest")&&task.getHarvest()==1){
            task.setHarvest(2);
            return taskDao.saveAndFlush(task).getHarvest();
        }else if(taskName.equals("help_water")&&task.getHelpWater()==1){
            task.setHelpWater(2);
            return taskDao.saveAndFlush(task).getHelpWater();
        }else if(taskName.equals("help_fertilize")&&task.getHelpFertilize()==1){
            task.setHelpFertilize(2);
            return taskDao.saveAndFlush(task).getHelpFertilize();
        }else {
            return -1;
        }
    }

}
