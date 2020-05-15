package com.knowledge_farm.user_pet_house.controller;

import com.knowledge_farm.entity.BagPetUtilItem;
import com.knowledge_farm.entity.UserPetHouse;
import com.knowledge_farm.user_pet_house.service.UserPetHouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserPetHouseController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-28 14:42
 */
@Api(description = "前台宠物仓库接口")
@RestController
@RequestMapping("/userpethouse")
public class UserPetHouseController {
    @Resource
    private UserPetHouseService userPetHouseService;

    @ApiOperation(value = "查看宠物仓库", notes = "返回值：List（UserPetHouse）")
    @GetMapping("/showUserPetHouse")
    public List<UserPetHouse> showUserPetHouse(HttpSession session, HttpServletResponse response){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId != null) {
                return this.userPetHouseService.showUserPet(userId);
            }
            response.sendError(401);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @ApiOperation(value = "查询仓库中所有道具", notes = "返回值：List（BagPetUtilItem）")
    @GetMapping("/initUserPetUtilBag")
    public List<BagPetUtilItem> initUserPetUtilBag(HttpSession session, HttpServletResponse response){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId != null) {
                List<BagPetUtilItem> bagPetUtilItems = this.userPetHouseService.initUserPetUtilBag(userId);
                return bagPetUtilItems;
            }
            response.sendError(401);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
