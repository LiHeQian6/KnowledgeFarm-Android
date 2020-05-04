package com.knowledge_farm.pet_food.controller;

import com.knowledge_farm.entity.PetFood;
import com.knowledge_farm.pet_food.service.PetFoodService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName PetFoodController
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 10:03
 */
@RestController
@RequestMapping("/petfood")
public class PetFoodController {
    @Resource
    private PetFoodService petFoodService;

    @ApiOperation(value = "商店展示所有饲料", notes = "返回值：List（PetFood）")
    @GetMapping("showInStore")
    public List<PetFood> showInStore(){
        return this.petFoodService.showInStore();
    }

}
