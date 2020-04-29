package com.knowledge_farm.front.pet.controller;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.front.pet.service.PetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: knowledge_farm
 * @description: 管理员操作宠物类
 * @author: 景光赞
 * @create: 2020-04-29 18:00
 **/
@Controller
@RequestMapping("/admin/pet")
public class PetController {
    @Resource
    private PetService petService;
    @Resource
    private EntityManager entityManager;
    @Value("${file.petPhotoFolderName}")
    private String petPhotoFolderName;
    @Value("${file.petPhotoFileLocation}")
    private String petPhotoFileLocation;

    @RequestMapping("/addPet")
    public String addPet(@RequestParam("name")String name,
                      @RequestParam("description")String description,
                      @RequestParam("price")int price,
                      @RequestParam("life")int life,
                      @RequestParam("intelligence")int intelligence,
                      @RequestParam("physical")int physical,
                         @RequestParam("upload") MultipartFile files[]){
        for(MultipartFile multipartFile : files) {
            if (multipartFile.getOriginalFilename().equals("")) {
                return Result.NULL;
            }
        }
        try{
            int id = petService.addPet(new Pet(name,description,life,intelligence,physical,
                    price));
            entityManager.clear();
            String img[] = new String[3];
            int count = 1;
            for(MultipartFile multipartFile : files){
                String fileName = id + "_" + count + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + multipartFile.getOriginalFilename();
                img[count-1] = this.petPhotoFolderName + "/" + fileName;
                FileCopyUtils.copy(multipartFile.getBytes(), new File(this.petPhotoFileLocation, fileName));
                count++;
            }
            Pet editPet = petService.findPetById(id);
            editPet.setImg1(img[0]);
            editPet.setImg2(img[1]);
            editPet.setImg3(img[2]);
            petService.updatePet(editPet);
            return Result.SUCCEED;
        }catch (Exception e){
            return Result.FAIL;
        }
    }
}
