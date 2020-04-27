package com.knowledge_farm.user_pet_house.service;

import com.knowledge_farm.user_pet_house.dao.UserPetHouseDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @program: knowledge_farm
 * @description: 宠物仓库
 * @author: 景光赞
 * @create: 2020-04-27 13:08
 **/
@Service
@Transactional(readOnly = true)
public class UserPetHoueService {
    @Resource
    private UserPetHouseDao userPetHouseDao;

    @Transactional(readOnly = false)
    public int buyPet(){
        return 0;
    }
}
