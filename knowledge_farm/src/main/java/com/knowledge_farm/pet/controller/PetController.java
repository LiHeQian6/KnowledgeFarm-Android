package com.knowledge_farm.pet.controller;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.entity.PetVO;
import com.knowledge_farm.entity.Result;
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
    @ApiOperation(value = "商店展示所有宠物", notes = "返回值：List（Pet）")
    @GetMapping("/showInStore")
    public List<PetVO> showInStore(@RequestParam("userId") Integer userId){
        return petService.showAllPetInStore(userId);
    }

    @ApiOperation(value = "切换使用的宠物", notes = "返回值：(String)true || (String)false")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "willUsingPetId", value = "将要使用的宠物Id", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/changePet")
    public String changePet(@RequestParam("userId") Integer userId,
                            @RequestParam("willUsingPetId") Integer willUsingPetId){
        try {
            this.petService.changePet(userId, willUsingPetId);
            return Result.TRUE;
        }catch (Exception e){
            return Result.FALSE;
        }
    }

}
