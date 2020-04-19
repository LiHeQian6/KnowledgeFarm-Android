package com.knowledge_farm.usercrop.service;

import com.knowledge_farm.entity.Land;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserCrop;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.usercrop.dao.UserCropDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserCropService
 * @Description
 * @Author 张帅华
 * @Date 2020-04-08 22:23
 */
@Service
@Transactional(readOnly = true)
public class UserCropServiceImpl {
    @Resource
    private UserCropDao userCropDao;
    @Resource
    private UserServiceImpl userService;

    @Transactional(readOnly = false)
    public UserCrop save(UserCrop userCrop){
        return this.userCropDao.save(userCrop);
    }

    public UserCrop findUserCropById(Integer id){
        return this.userCropDao.findUserCropById(id);
    }

    public List<UserCrop> initUserCrop(Integer userId){
        User user = this.userService.findUserById(userId);
        Land land = user.getLand();
        return findUserCropListByLand(land);
    }

    public int getCropProgress(Integer userId, String landNumber){
        User user = this.userService.findUserById(userId);
        Land land = user.getLand();
        UserCrop userCrop = findUserCropByLand(land, landNumber);
        return userCrop.getProgress();
    }

    public List<UserCrop> findUserCropListByLand(Land land){
        List<UserCrop> userCrops = new ArrayList<>();
        userCrops.add(land.getUserCrop1());
        userCrops.add(land.getUserCrop2());
        userCrops.add(land.getUserCrop3());
        userCrops.add(land.getUserCrop4());
        userCrops.add(land.getUserCrop5());
        userCrops.add(land.getUserCrop6());
        userCrops.add(land.getUserCrop7());
        userCrops.add(land.getUserCrop8());
        userCrops.add(land.getUserCrop1());
        userCrops.add(land.getUserCrop9());
        userCrops.add(land.getUserCrop10());
        userCrops.add(land.getUserCrop11());
        userCrops.add(land.getUserCrop12());
        userCrops.add(land.getUserCrop13());
        userCrops.add(land.getUserCrop14());
        userCrops.add(land.getUserCrop15());
        userCrops.add(land.getUserCrop16());
        userCrops.add(land.getUserCrop17());
        userCrops.add(land.getUserCrop18());
        return userCrops;
    }

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

}
