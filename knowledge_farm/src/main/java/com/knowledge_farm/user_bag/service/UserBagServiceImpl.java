package com.knowledge_farm.user_bag.service;

import com.knowledge_farm.entity.*;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.user_bag.dao.UserBagDao;
import com.knowledge_farm.user_bag.dao.UserPetFoodBagDao;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName UserBagServiceImpl
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 13:52
 */
@Service
@Transactional(readOnly = true)
public class UserBagServiceImpl {
    @Resource
    private UserBagDao userBagDao;
    @Resource
    private UserPetFoodBagDao userPetFoodBagDao;
    @Resource
    @Lazy
    private UserServiceImpl userService;

    public List<BagCropItem> initUserCropBag(Integer userId){
        User user = this.userService.findUserById(userId);
        if(user != null){
            Set<UserCropBag> userCropBags = user.getUserCropBags();
            List<BagCropItem> bagCropItems = new ArrayList<>();
            for(UserCropBag userCropBag : userCropBags){
                BagCropItem bagCropItem = new BagCropItem();
                bagCropItem.setCrop(userCropBag.getCrop());
                bagCropItem.setNumber(userCropBag.getNumber());
                bagCropItems.add(bagCropItem);
            }
            return bagCropItems;
        }
        return new ArrayList<>();
    }

    public List<BagPetFoodItem> initUserPetFoodBag(Integer userId){
        User user = this.userService.findUserById(userId);
        if(user != null){
            Set<UserPetFoodBag> userPetFoodBags = user.getUserPetFoodBags();
            List<BagPetFoodItem> bagPetFoodItems = new ArrayList<>();
            for(UserPetFoodBag userPetFoodBag : userPetFoodBags){
                BagPetFoodItem bagPetFoodItem = new BagPetFoodItem(userPetFoodBag.getNumber(), userPetFoodBag.getPetFood());
                bagPetFoodItems.add(bagPetFoodItem);
            }
            return bagPetFoodItems;
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = false)
    public void deleteUserCropBag(UserCropBag userCropBag){
        this.userBagDao.delete(userCropBag);
    }

    @Transactional(readOnly = false)
    public void deleteUserPetFoodBag(UserPetFoodBag userPetFoodBag){
        this.userPetFoodBagDao.delete(userPetFoodBag);
    }

}
