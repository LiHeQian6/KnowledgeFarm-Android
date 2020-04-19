package com.knowledge_farm.front.land.service;

import com.knowledge_farm.crop.service.CropServiceImpl;
import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.entity.Land;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserCrop;
import com.knowledge_farm.front.land.dao.LandDao;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.usercrop.service.UserCropServiceImpl;
import com.knowledge_farm.util.PageUtil;
import com.knowledge_farm.util.UserCropGrowJob;
import org.quartz.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @ClassName LandService
 * @Description
 * @Author 张帅华
 * @Date 2020-04-19 08:42
 */
@Service
@Transactional(readOnly = true)
public class LandService {
    @Resource
    private LandDao landDao;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private CropServiceImpl cropService;
    @Resource
    private UserCropServiceImpl userCropService;
    @Resource
    private Scheduler scheduler;

    public Object findAllLandByAccount(String account, Integer pageNumber, Integer pageSize){
        if(account != null && !account.equals("")){
            User user = this.userService.findUserByAccount(account);
            try {
                return this.landDao.findALLByUser(user.getId(), PageRequest.of(pageNumber - 1, pageSize));
            }catch (NullPointerException e){
                PageUtil<Land> pageUtil = new PageUtil(pageNumber, pageSize);
                pageUtil.setTotalCount(0);
                pageUtil.setList(new ArrayList());
                return pageUtil;
            }
        }
        return this.landDao.findAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    public Land findLandById(Integer id){
        return this.landDao.findLandById(id);
    }

    @Transactional(readOnly = false)
    public String editLand(Integer userId, String landNumber, Integer waterLimit, Integer fertilizerLimit, Integer progress, Integer status, Integer flag){
        User user = this.userService.findUserById(userId);
        int isOpen = 0;
        try {
            Land land = user.getLand();
            UserCrop userCrop = findUserCropByLand(land, landNumber);
            if(userCrop == null){
                switch (flag){
                    case -1:
                        break;
                    case 0:
                        addUserCrop(land, new UserCrop(), landNumber);
                        this.userService.saveUser(user);
                        break;
                    default:
                        UserCrop userCrop1 = new UserCrop();
                        Crop crop = this.cropService.findCropById(flag);
                        userCrop1.setCrop(crop);
                        if(progress >= crop.getMatureTime()){
                            progress = crop.getMatureTime();
                            isOpen = 1;
                        }
                        if(status == 0){
                            isOpen = 1;
                        }
                        userCrop1.setWaterLimit(waterLimit);
                        userCrop1.setFertilizerLimit(fertilizerLimit);
                        userCrop1.setProgress(progress);
                        userCrop1.setStatus(status);
                        addUserCrop(land, userCrop1, landNumber);
                        this.userService.saveUser(user);
                        if(isOpen == 0){
                            startJob(scheduler, userId, userCrop1.getId());
                        }
                        break;
                }
            }else {
                Crop crop = userCrop.getCrop();
                if(crop != null){
                    switch (flag){
                        case -1:
                            deleteJob("job" + userId, "group" + userId);
                            addUserCrop(land, null, landNumber);
                            this.userService.saveUser(user);
                            this.userCropService.deleteUserCropById(userCrop.getId());
                            break;
                        case 0:
                            userCrop.setCrop(null);
                            userCrop.setProgress(0);
                            userCrop.setWaterLimit(0);
                            userCrop.setFertilizerLimit(0);
                            userCrop.setStatus(1);
                            deleteJob("job" + userId, "group" + userId);
                            break;
                        default:
                            Crop crop1 = this.cropService.findCropById(flag);
                            if(userCrop.getCrop() != crop1){
                                userCrop.setCrop(crop1);
                            }else{
                                isOpen = 1;
                            }
                            if(progress >= crop1.getMatureTime()){
                                progress = crop1.getMatureTime();
                                isOpen = 1;
                            }
                            if(status == 0){
                                isOpen = 1;
                            }
                            userCrop.setWaterLimit(waterLimit);
                            userCrop.setFertilizerLimit(fertilizerLimit);
                            userCrop.setProgress(progress);
                            userCrop.setStatus(status);
                            if(isOpen == 0){
                                deleteJob("job" + userId, "group" + userId);
                                startJob(scheduler, userId, userCrop.getId());
                            }
                            break;
                    }
                }else{
                    switch (flag){
                        case -1:
                            addUserCrop(land, null, landNumber);
                            this.userService.saveUser(user);
                            this.userCropService.deleteUserCropById(userCrop.getId());
                            break;
                        case 0:
                            break;
                        default:
                            Crop crop1 = this.cropService.findCropById(flag);
                            userCrop.setCrop(crop1);
                            if(progress >= crop1.getMatureTime()){
                                progress = crop1.getMatureTime();
                                isOpen = 1;
                            }
                            if(status == 0){
                                isOpen = 1;
                            }
                            userCrop.setWaterLimit(waterLimit);
                            userCrop.setFertilizerLimit(fertilizerLimit);
                            userCrop.setProgress(progress);
                            userCrop.setStatus(status);
                            if(isOpen == 0){
                                startJob(scheduler, userId, userCrop.getId());
                            }
                            break;
                    }
                }
            }
            return "succeed";
        }catch (NullPointerException e){
            return null;
        }catch (Exception e){
            return "fail";
        }
    }

    /**
     * @Author 张帅华
     * @Description 根据landNumber得到对应土地的种植信息对象
     * @Date 23:30 2020/4/10 0010
     * @Param [land, landNumber]
     * @return com.atguigu.farm.entity.UserCrop
     **/
    public UserCrop findUserCropByLand(Land land, String landNumber){
        Integer realLand = Integer.parseInt(landNumber.substring(4));
        switch (realLand){
            case 1:
                return land.getUserCrop1();
            case 2:
                return land.getUserCrop2();
            case 3:
                return land.getUserCrop3();
            case 4:
                return land.getUserCrop4();
            case 5:
                return land.getUserCrop5();
            case 6:
                return land.getUserCrop6();
            case 7:
                return land.getUserCrop7();
            case 8:
                return land.getUserCrop8();
            case 9:
                return land.getUserCrop9();
            case 10:
                return land.getUserCrop10();
            case 11:
                return land.getUserCrop11();
            case 12:
                return land.getUserCrop12();
            case 13:
                return land.getUserCrop13();
            case 14:
                return land.getUserCrop14();
            case 15:
                return land.getUserCrop15();
            case 16:
                return land.getUserCrop16();
            case 17:
                return land.getUserCrop17();
            case 18:
                return land.getUserCrop18();
            default:
                return null;
        }
    }

    /**
     * @Author 张帅华
     * @Description 给指定landNumber的土地添加作物
     * @Date 23:30 2020/4/10 0010
     * @Param [land, userCrop, landNumber]
     * @return void
     **/
    public void addUserCrop(Land land, UserCrop userCrop, String landNumber){
        Integer realLand = Integer.parseInt(landNumber.substring(4));
        switch (realLand){
            case 1:
                land.setUserCrop1(userCrop);
                break;
            case 2:
                land.setUserCrop2(userCrop);
                break;
            case 3:
                land.setUserCrop3(userCrop);
                break;
            case 4:
                land.setUserCrop4(userCrop);
                break;
            case 5:
                land.setUserCrop5(userCrop);
                break;
            case 6:
                land.setUserCrop6(userCrop);
                break;
            case 7:
                land.setUserCrop7(userCrop);
                break;
            case 8:
                land.setUserCrop8(userCrop);
                break;
            case 9:
                land.setUserCrop9(userCrop);
                break;
            case 10:
                land.setUserCrop10(userCrop);
                break;
            case 11:
                land.setUserCrop11(userCrop);
                break;
            case 12:
                land.setUserCrop12(userCrop);
                break;
            case 13:
                land.setUserCrop13(userCrop);
                break;
            case 14:
                land.setUserCrop14(userCrop);
                break;
            case 15:
                land.setUserCrop15(userCrop);
                break;
            case 16:
                land.setUserCrop16(userCrop);
                break;
            case 17:
                land.setUserCrop17(userCrop);
                break;
            case 18:
                land.setUserCrop18(userCrop);
                break;
        }
    }

    /**
     * @Author 张帅华
     * @Description 开启自动生长、干旱湿润状态变换Job
     * @Date 23:31 2020/4/10 0010
     * @Param [scheduler, id]
     * @return void
     **/
    public void startJob(Scheduler scheduler, Integer userId, Integer userCropId) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        String name = "job" + userId;
        String group = "group" + userId;
        JobDetail jobDetail = JobBuilder.newJob(UserCropGrowJob.class).withIdentity(name, group).build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("userId", userId);
        jobDataMap.put("userCropId", userCropId);
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0/1 * * ? ");
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

}
