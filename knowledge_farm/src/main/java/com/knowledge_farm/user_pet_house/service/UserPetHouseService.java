package com.knowledge_farm.user_pet_house.service;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserPetHouse;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.user_pet_house.dao.UserPetHouseDao;
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
    private UserServiceImpl userService;

    public UserPetHouse findByUser(User user, Pet pet){
        return userPetHouseDao.findUserPetHouseByUserAndPet(user,pet);
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

    @Transactional(readOnly = false)
    public int buyPet(){
        return 0;
    }
}
