package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.Task;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.jpush.service.JpushService;
import com.knowledge_farm.task.service.TaskService;
import com.knowledge_farm.user.service.UserServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.HashMap;

@Component
@Aspect
public class TaskAspect {

    @Pointcut("@annotation(com.knowledge_farm.annotation.Task)")
    public  void taskAspect() {}
    @Resource
    private TaskService taskService;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private JpushService jpushService;

    /**
     * @Author 景光赞
     * @param joinPoint
     * @return java.lang.Object
     * @Description 每日任务完成
     * @Date 11:58 2020/4/17
     **/
    @Around("taskAspect()")
    public Object encode(ProceedingJoinPoint joinPoint) throws ParseException {
        String description = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(com.knowledge_farm.annotation.Task.class).description();
        System.out.println("*****Task Start*****");
        Object[] args = joinPoint.getArgs();
        Object result = null;
        try {
            result = joinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        User user = userService.findUserById((Integer) args[0]);
        Task task = user.getTask();
        if (description.equals("water")&&task.getWater()==0){
            taskService.finishTask(user,"water");
            jpushService.sendCustomPush("task", "", new HashMap<>(), user.getAccount());
        }else if(description.equals("fertilize")&&task.getFertilize()==0){
            taskService.finishTask(user,"fertilize");
            jpushService.sendCustomPush("task", "", new HashMap<>(), user.getAccount());
        }else if(description.equals("crop")&&task.getCrop()==0){
            taskService.finishTask(user,"crop");
            jpushService.sendCustomPush("task", "", new HashMap<>(), user.getAccount());
        }else if(description.equals("harvest")&&task.getHarvest()==0){
            taskService.finishTask(user,"harvest");
            jpushService.sendCustomPush("task", "", new HashMap<>(), user.getAccount());
        }else if(description.equals("help_water")&&task.getHelpWater()==0){
            taskService.finishTask(user,"help_water");
            jpushService.sendCustomPush("task", "", new HashMap<>(), user.getAccount());
        }else if(description.equals("help_fertilize")&&task.getHelpFertilize()==0){
            taskService.finishTask(user,"help_fertilize");
            jpushService.sendCustomPush("task", "", new HashMap<>(), user.getAccount());
        }
        System.out.println("*****Task End*****");
        return result;
    }

}
