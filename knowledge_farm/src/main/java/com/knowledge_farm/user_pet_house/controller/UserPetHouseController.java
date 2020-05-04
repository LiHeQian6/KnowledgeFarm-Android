package com.knowledge_farm.user_pet_house.controller;

import com.knowledge_farm.entity.BagPetUtilItem;
import com.knowledge_farm.entity.UserPetHouse;
import com.knowledge_farm.user_pet_house.service.UserPetHouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(description = "前台宠物仓库接口")
@RestController
@RequestMapping("/userpethouse")
public class UserPetHouseController {
    @Resource
    private UserPetHouseService userPetHouseService;

    @ApiOperation(value = "查看宠物仓库", notes = "返回值：List（UserPetHouse）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/showUserPetHouse")
    public List<UserPetHouse> showUserPetHouse(@RequestParam("userId") Integer userId){
        return this.userPetHouseService.showUserPet(userId);
    }

    @ApiOperation(value = "查询仓库中所有饲料或工具", notes = "返回值：List（BagPetUtilItem）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "petUtilTypeId", value = "宠物工具类型", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/initUserPetUtilBag")
    public List<BagPetUtilItem> initUserPetUtilBag(@RequestParam("userId") Integer userId, @RequestParam("petUtilTypeId") Integer petUtilTypeId){
        List<BagPetUtilItem> bagPetUtilItems = this.userPetHouseService.initUserPetUtilBag(userId);
        return bagPetUtilItems;
    }

}
