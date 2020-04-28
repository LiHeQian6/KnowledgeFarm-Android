package com.knowledge_farm.user_pet_house.controller;

import com.knowledge_farm.user_pet_house.service.UserPetHoueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    private UserPetHoueService userPetHoueService;

}
