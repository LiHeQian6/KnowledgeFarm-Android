package com.knowledge_farm.pet.controller;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.pet.service.PetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: knowledge_farm
 * @description: 商店里的宠物
 * @author: 景光赞
 * @create: 2020-04-27 11:34
 **/
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
    @RequestMapping("/showInStore")
    public Page<Pet> showInStore(@RequestParam("pageNum")int pageNum){
        return petService.showAllPetInStore(PageRequest.of(pageNum,3));
    }
    @RequestMapping("/test")
    public Page<Pet> showInStore(){
        return petService.showAllPetInStore(PageRequest.of(0,3));
    }
}
