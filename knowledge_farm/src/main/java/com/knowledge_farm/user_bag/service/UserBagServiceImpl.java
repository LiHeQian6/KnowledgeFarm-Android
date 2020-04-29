package com.knowledge_farm.user_bag.service;

import com.knowledge_farm.entity.BagCropItem;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserBag;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.user_bag.dao.UserBagDao;
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
    @Lazy
    private UserServiceImpl userService;

    public List<BagCropItem> initUserBag(Integer userId){
        User user = this.userService.findUserById(userId);
        if(user != null){
            Set<UserBag> userBags = user.getUserBags();
            List<BagCropItem> bagCropItems = new ArrayList<>();
            for(UserBag userBag : userBags){
                BagCropItem bagCropItem = new BagCropItem();
                bagCropItem.setCrop(userBag.getCrop());
                bagCropItem.setNumber(userBag.getNumber());
                bagCropItems.add(bagCropItem);
            }
            return bagCropItems;
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = false)
    public void delete(UserBag userBag){
        this.userBagDao.delete(userBag);
    }

}
