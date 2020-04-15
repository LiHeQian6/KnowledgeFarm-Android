package com.atguigu.farm.userbag.service;

import com.atguigu.farm.entity.BagItem;
import com.atguigu.farm.entity.User;
import com.atguigu.farm.entity.UserBag;
import com.atguigu.farm.user.service.UserServiceImpl;
import com.atguigu.farm.userbag.dao.UserBagDao;
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
    private UserServiceImpl userService;

    public List<BagItem> initUserBag(Integer userId){
        User user = this.userService.findUserById(userId);
        Set<UserBag> userBags = user.getUserBags();
        List<BagItem> bagItems = new ArrayList<>();
        for(UserBag userBag : userBags){
            BagItem bagItem = new BagItem();
            bagItem.setCrop(userBag.getCrop());
            bagItem.setNumber(userBag.getNumber());
            bagItems.add(bagItem);
        }
        return bagItems;
    }

    @Transactional(readOnly = false)
    public void deleteById(Integer id){
        this.userBagDao.deleteById(id);
    }

}
