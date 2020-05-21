package com.knowledge_farm.front.pet.controller;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.entity.PetFunction;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.front.pet.service.FrontPetService;
import com.knowledge_farm.util.PageUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: knowledge_farm
 * @description: 管理员操作宠物类
 * @author: 景光赞
 * @create: 2020-04-29 18:00
 **/
@Api(description = "后台宠物接口")
@Controller
@RequestMapping("/admin/pet")
public class FrontPetController {
    @Resource
    private FrontPetService frontPetService;
    @Resource
    private EntityManager entityManager;
    @Value("${file.petPhotoFolderName}")
    private String petPhotoFolderName;
    @Value("${file.petPhotoFileLocation}")
    private String petPhotoFileLocation;

    @GetMapping("/toAdd")
    public String toAdd(){
        return "pet-add";
    }

    @GetMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, Model model){
        Pet pet = this.frontPetService.findPetById(id);
        if(pet != null){
            model.addAttribute("pet", pet);
        }
        return "pet-edit";
    }

    @GetMapping("/findPetPage")
    public String findPetPage(@RequestParam(value = "name", required = false) String name,
                               @RequestParam("exist") Integer exist,
                               @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                               Model model){
        Page<Pet> page = this.frontPetService.findAllPet(name, exist, pageNumber, pageSize);
        PageUtil<Pet> pageUtil = new PageUtil<>(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        model.addAttribute("petPage", pageUtil);
        if(exist == 1){
            return "pet-list";
        }
        return "pet-del";
    }

    @PostMapping("/deleteOnePet")
    @ResponseBody
    public String deleteOnePet(@RequestParam("id") Integer id){
        try {
            this.frontPetService.updateExist(id, 0);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/deleteMultiPet")
    @ResponseBody
    public String deleteMultiPet(@RequestParam("deleteStr") String deleteStr){
        String deleteIds[] = deleteStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : deleteIds){
            idList.add(Integer.parseInt(id));
        }
        try {
            this.frontPetService.editStatusListByIdList(idList, 0);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/recoveryOnePet")
    @ResponseBody
    public String recoveryOnePet(@RequestParam("id") Integer id){
        try {
            this.frontPetService.updateExist(id, 1);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/recoveryMultiPet")
    @ResponseBody
    public String recoveryMultiPet(@RequestParam("recoveryStr") String recoveryStr) {
        String recoveryId[] = recoveryStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : recoveryId){
            idList.add(Integer.parseInt(id));
        }
        try {
            this.frontPetService.editStatusListByIdList(idList, 1);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/deleteThoroughPet")
    @ResponseBody
    public String deleteThoroughPet(@RequestParam("id") Integer id){
        try {
            this.frontPetService.deletePetById(id);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/addPet")
    @ResponseBody
    public String addPet(@RequestParam("name")String name,
                         @RequestParam("description")String description,
                         @RequestParam("price")Integer price,
                         @RequestParam("life")Integer life,
                         @RequestParam("intelligence")Integer intelligence,
                         @RequestParam("physical")Integer physical,
                         @RequestParam("upload") MultipartFile files[],
                         @RequestParam("harvestHour1")Integer harvestHour1,
                         @RequestParam("harvestHour2")Integer harvestHour2,
                         @RequestParam("harvestHour3")Integer harvestHour3,
                         @RequestParam("growHour1")Integer growHour1,
                         @RequestParam("growHour2")Integer growHour2,
                         @RequestParam("growHour3")Integer growHour3){
        String result = validate(harvestHour1, harvestHour2, harvestHour3, growHour1, growHour2, growHour3);
        if(result != Result.SUCCEED){
            return result;
        }
        for(MultipartFile multipartFile : files) {
            if (multipartFile.getOriginalFilename().equals("")) {
                return Result.NULL;
            }
        }
        try{
            int id = frontPetService.addPet(new Pet(name,description,life,intelligence,physical,price));
            entityManager.clear();
            String img[] = new String[3];
            int count = 1;
            for(MultipartFile multipartFile : files){
                String fileName = id + "_" + count + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + multipartFile.getOriginalFilename();
                img[count-1] = this.petPhotoFolderName + "/" + fileName;
                FileCopyUtils.copy(multipartFile.getBytes(), new File(this.petPhotoFileLocation, fileName));
                count++;
            }
            Pet editPet = frontPetService.findPetById(id);
            editPet.setImg1(img[0]);
            editPet.setImg2(img[1]);
            editPet.setImg3(img[2]);
            PetFunction petFunction = new PetFunction(editPet, harvestHour1, harvestHour2, harvestHour3, growHour1, growHour2, growHour3);
            editPet.setPetFunction(petFunction);
            frontPetService.updatePet(editPet);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/updatePet")
    @ResponseBody
    public String updatePet(Pet pet, PetFunction petFunction, @RequestParam("upload") MultipartFile files[]) {
        Integer harvestHour1 = petFunction.getHarvestHour1();
        Integer harvestHour2 = petFunction.getHarvestHour2();
        Integer harvestHour3 = petFunction.getHarvestHour3();
        Integer growHour1 = petFunction.getGrowHour1();
        Integer growHour2 = petFunction.getGrowHour2();
        Integer growHour3 = petFunction.getGrowHour3();
        String result = validate(harvestHour1, harvestHour2, harvestHour3, growHour1, growHour2, growHour3);
        if(result != Result.SUCCEED){
            return result;
        }
        Integer id = pet.getId();
        int count = 1;
        try {
            for (MultipartFile multipartFile : files) {
                if (!multipartFile.getOriginalFilename().equals("")) {
                    switch (count) {
                        case 1:
                            File file = new File(this.petPhotoFileLocation + "/" + pet.getImg1());
                            if (file.exists()) {
                                file.delete();
                            }
                            String fileName = id + "_" + count + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + multipartFile.getOriginalFilename();
                            pet.setImg1(this.petPhotoFolderName + "/" + fileName);
                            FileCopyUtils.copy(multipartFile.getBytes(), new File(this.petPhotoFileLocation, fileName));
                            break;
                        case 2:
                            File file2 = new File(this.petPhotoFileLocation + "/" + pet.getImg2());
                            if (file2.exists()) {
                                file2.delete();
                            }
                            String fileName2 = id + "_" + count + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + multipartFile.getOriginalFilename();
                            pet.setImg2(this.petPhotoFolderName + "/" + fileName2);
                            FileCopyUtils.copy(multipartFile.getBytes(), new File(this.petPhotoFileLocation, fileName2));
                            break;
                        case 3:
                            File file3 = new File(this.petPhotoFileLocation + "/" + pet.getImg3());
                            if (file3.exists()) {
                                file3.delete();
                            }
                            String fileName3 = id + "_" + count + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + multipartFile.getOriginalFilename();
                            pet.setImg3(this.petPhotoFolderName + "/" + fileName3);
                            FileCopyUtils.copy(multipartFile.getBytes(), new File(this.petPhotoFileLocation, fileName3));
                            break;
                    }
                }
                count++;
            }
            petFunction.setPet(pet);
            pet.setPetFunction(petFunction);
            this.frontPetService.updatePet(pet);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    public String validate(Integer harvestHour1, Integer harvestHour2, Integer harvestHour3, Integer growHour1, Integer growHour2, Integer growHour3){
        if(harvestHour1 != 0 && harvestHour2 != 0 && harvestHour3 != 0){
            if(harvestHour1 <= harvestHour2){
                return "宠物第一阶段n小时收获必须比第二阶段大";
            }
            if(harvestHour2 <= harvestHour3){
                return "宠物第二阶段n小时收获必须比第三阶段大";
            }
        }else{
            harvestHour1 = 0;
            harvestHour2 = 0;
            harvestHour3 = 0;
        }
        if(growHour1 != 0 && growHour2 != 0 && growHour3 != 0){
            if(growHour1 <= growHour2){
                return "宠物第一阶段n小时生长必须比第二阶段大";
            }
            if(growHour2 <= growHour3){
                return "宠物第二阶段n小时生长必须比第三阶段大";
            }
        }else{
            growHour1 = 0;
            growHour2 = 0;
            growHour3 = 0;
        }
        return Result.SUCCEED;
    }

}
