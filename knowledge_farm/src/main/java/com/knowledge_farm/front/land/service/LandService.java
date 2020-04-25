package com.knowledge_farm.front.land.service;

import com.knowledge_farm.crop.service.CropServiceImpl;
import com.knowledge_farm.entity.*;
import com.knowledge_farm.front.land.dao.LandDao;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.user_crop.service.UserCropServiceImpl;
import com.knowledge_farm.util.PageUtil;
import com.knowledge_farm.util.UserCropGrowJob;
import org.quartz.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
            if(user != null){
                return this.landDao.findALLByUser(user.getId(), PageRequest.of(pageNumber - 1, pageSize));
            }
            PageUtil<Land> pageUtil = new PageUtil(pageNumber, pageSize);
            pageUtil.setTotalCount(0);
            pageUtil.setList(new ArrayList());
            return pageUtil;
        }
        return this.landDao.findAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    public Land findLandById(Integer id){
        return this.landDao.findLandById(id);
    }

    public List<Crop> findAllCrop(){
        return this.cropService.findAllCrop();
    }

    @Transactional(readOnly = false)
    public int editLand(Integer userId, String landNumber, Integer waterLimit, Integer fertilizerLimit, Integer progress, Integer status, Integer flag) throws SchedulerException {
        User user = this.userService.findUserById(userId);
        int isOpen = 0;
        Land land = user.getLand();
        UserCrop userCrop = this.userCropService.findUserCropByLand(land, landNumber);
        if(userCrop == null){
            switch (flag){
                case -1:
                    break;
                case 0:
                    this.userCropService.addUserCrop(land, new UserCrop(), landNumber);
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
                    this.userCropService.addUserCrop(land, userCrop1, landNumber);
                    this.userCropService.save(userCrop1);
                    if(isOpen == 0){
                        return userCrop1.getId();
                    }
                    break;
            }
        }else {
            Crop crop = userCrop.getCrop();
            if(crop != null){
                switch (flag){
                    case -1:
                        deleteJob("job" + userId + "_" + Integer.parseInt(landNumber.substring(4)), "group" + userId + "_" + Integer.parseInt(landNumber.substring(4)));
                        this.userCropService.addUserCrop(land, null, landNumber);
                        this.userCropService.deleteUserCropById(userCrop.getId());
                        break;
                    case 0:
                        deleteJob("job" + userId + "_" + Integer.parseInt(landNumber.substring(4)), "group" + userId + "_" + Integer.parseInt(landNumber.substring(4)));
                        userCrop.setCrop(null);
                        userCrop.setProgress(0);
                        userCrop.setWaterLimit(15);
                        userCrop.setFertilizerLimit(15);
                        userCrop.setStatus(1);
                        break;
                    default:
                        Crop crop1 = this.cropService.findCropById(flag);
                        if(crop != crop1){
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
                        this.userService.saveUser(user);
                        if(isOpen == 0){
                            deleteJob("job" + userId + "_" + Integer.parseInt(landNumber.substring(4)), "group" + userId + "_" + Integer.parseInt(landNumber.substring(4)));
                            return userCrop.getId();
                        }
                        break;
                }
            }else{
                switch (flag){
                    case -1:
                        this.userCropService.addUserCrop(land, null, landNumber);
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
                            return userCrop.getId();
                        }
                        break;
                }
            }
        }
        return 0;
    }

    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

}
