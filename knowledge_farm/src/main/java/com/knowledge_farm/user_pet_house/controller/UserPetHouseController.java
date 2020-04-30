package com.knowledge_farm.user_pet_house.controller;

import com.knowledge_farm.entity.UserPetHouse;
import com.knowledge_farm.user_pet_house.service.UserPetHouseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserPetHouseController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-28 14:42
 */
@RestController
@RequestMapping("/userpethouse")
public class UserPetHouseController {
    @Resource
    private UserPetHouseService userPetHouseService;

    @GetMapping("/showUserPetHouse")
    public List<UserPetHouse> showUserPet(@RequestParam("userId") Integer userId){
        return this.userPetHouseService.showUserPet(userId);
    }

}
