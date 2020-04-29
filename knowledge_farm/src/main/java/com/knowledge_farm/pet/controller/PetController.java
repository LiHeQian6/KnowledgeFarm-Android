package com.knowledge_farm.pet.controller;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.pet.service.PetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: knowledge_farm
 * @description: 商店里的宠物
 * @author: 景光赞
 * @create: 2020-04-27 11:34
 **/
@Api(description = "前台宠物接口")
@RestController
@RequestMapping("/pet")
public class PetController {
    @Resource
    private PetService petService;

    /**
     * @description: 商店展示所有宠物
     * @author :景光赞
     * @date :2020/4/27 11:56
     * @param :[pageNum]
     * @return :org.springframework.data.domain.Page<com.knowledge_farm.entity.Pet>
     */
    @GetMapping("/showInStore")
    public List<Pet> showInStore(){
        return petService.showAllPetInStore();
    }

    @RequestMapping("/test")
    public List<Pet> showInStore2(){
        return petService.showAllPetInStore();
    }
}
