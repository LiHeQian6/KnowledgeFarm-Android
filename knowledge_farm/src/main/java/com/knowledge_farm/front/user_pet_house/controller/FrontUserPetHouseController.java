package com.knowledge_farm.front.user_pet_house.controller;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserPetHouse;
import com.knowledge_farm.front.user_pet_house.service.FrontUserPetHouseService;
import com.knowledge_farm.util.PageUtil;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FrontUserPetHouseController
 * @Description
 * @Author 张帅华
 * @Date 2020-05-27 18:09
 */
@Api(description = "后台用户宠物接口")
@Controller
@RequestMapping("/admin/user_pet_house")
public class FrontUserPetHouseController {
    @Resource
    private FrontUserPetHouseService frontUserPetHouseService;

    @GetMapping("/findUserPetHousePage")
    public String findUserPetHousePage(@RequestParam(value = "account", required = false) String account,
                                       @RequestParam(value = "petId", required = false) Integer petId,
                                       @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                       Model model){
        Page page = this.frontUserPetHouseService.findUserPetHousePage(account, petId, pageNumber, pageSize);
        List<Pet> petList = this.frontUserPetHouseService.findAllPet();
        PageUtil pageUtil = new PageUtil(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        model.addAttribute("userPetHousePage", pageUtil);
        model.addAttribute("petList", petList);
        return "user-pet-house-list";
    }

    @GetMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, Model model){
        UserPetHouse userPetHouse = this.frontUserPetHouseService.findUserPetHouseById(id);
        if(userPetHouse != null){
            model.addAttribute("userPetHouse", userPetHouse);
        }
        return "user-pet-house-edit";
    }

    @PostMapping("/editUserPetHouse")
    @ResponseBody
    public String editUserPetHouse(@RequestParam("id") Integer id,
                                   @RequestParam("life") Integer life,
                                   @RequestParam("intelligence") Integer intelligence,
                                   @RequestParam("physical") Integer physical,
                                   @RequestParam("ifUsing") Integer ifUsing){
        try {
            return this.frontUserPetHouseService.editUserPetHouse(id, life, intelligence, physical, ifUsing);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.FAIL;
    }

}
