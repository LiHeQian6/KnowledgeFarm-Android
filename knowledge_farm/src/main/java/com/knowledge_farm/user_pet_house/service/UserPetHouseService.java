package com.knowledge_farm.user_pet_house.service;

import com.knowledge_farm.entity.*;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.user_pet_house.dao.UserPetUtilBagDao;
import com.knowledge_farm.user_pet_house.dao.UserPetHouseDao;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: knowledge_farm
 * @description: 宠物仓库
 * @author: 景光赞
 * @create: 2020-04-27 13:08
 **/
@Service
@Transactional(readOnly = true)
public class UserPetHouseService {
    @Resource
    private UserPetHouseDao userPetHouseDao;
    @Resource
    @Lazy
    private UserServiceImpl userService;
    @Resource
    private UserPetUtilBagDao userPetUtilBagDao;

    public UserPetHouse findByUser(User user, Pet pet){
        return userPetHouseDao.findUserPetHouseByUserAndPet(user,pet);
    }

    public UserPetHouse findUserPetHouseById(Integer id){
        return this.userPetHouseDao.findUserPetHouseById(id);
    }

    public UserPetHouse findUserPetHouseByUserAndId(Integer userId, Integer id){
        return this.userPetHouseDao.findUserPetHouseByUserAndId(userId, id);
    }

    public UserPetUtilBag findUserPetUtilBagById(Integer id){
        return this.userPetUtilBagDao.findUserPetUtilBagById(id);
    }

    public List<UserPetHouse> showUserPet(Integer userId){
        User user = this.userService.findUserById(userId);
        Set<UserPetHouse> userPetHouses = user.getPetHouses();
        List<UserPetHouse> userPetHouseList = new ArrayList<>();
        for(UserPetHouse userPetHouse : userPetHouses){
            userPetHouseList.add(userPetHouse);
        }
        return userPetHouseList;
    }

    public List<BagPetUtilItem> initUserPetUtilBag(Integer userId){
        User user = this.userService.findUserById(userId);
        if(user != null){
            Set<UserPetUtilBag> userPetUtilBags = user.getUserPetUtilBags();
            List<BagPetUtilItem> bagPetUtilItems = new ArrayList<>();
            for(UserPetUtilBag userPetUtilBag : userPetUtilBags){
                BagPetUtilItem bagPetUtilItem = new BagPetUtilItem(userPetUtilBag.getId(), userPetUtilBag.getNumber(), userPetUtilBag.getPetUtil());
                bagPetUtilItems.add(bagPetUtilItem);
            }
            return bagPetUtilItems;
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = false)
    public void deleteUserPetUtilBag(UserPetUtilBag userPetUtilBag){
        this.userPetUtilBagDao.delete(userPetUtilBag);
    }
}
