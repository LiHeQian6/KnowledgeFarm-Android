package com.knowledge_farm.front.pet_util.controller;

import com.knowledge_farm.entity.PetUtil;
import com.knowledge_farm.entity.PetUtilType;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.front.pet_util.service.FrontPetUtilService;
import com.knowledge_farm.pet_util_type.service.PetUtilTypeService;
import com.knowledge_farm.util.PageUtil;
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
 * @ClassName FrontPetUtilController
 * @Description
 * @Author 张帅华
 * @Date 2020-05-05 09:42
 */
@Controller
@RequestMapping("/admin/petutil")
public class FrontPetUtilController {
    @Resource
    private FrontPetUtilService frontPetUtilService;
    @Resource
    private PetUtilTypeService petUtilTypeService;
    @Resource
    private EntityManager entityManager;
    @Value("${file.petUtilPhotoFolderName}")
    private String petUtilPhotoFolderName;
    @Value("${file.petUtilPhotoFileLocation}")
    private String petUtilPhotoFileLocation;

    @GetMapping("/toAdd")
    public String toAdd(Model model){
        List<PetUtilType> petUtilTypeList = this.petUtilTypeService.findAll();
        model.addAttribute("petUtilTypes", petUtilTypeList);
        return "pet-util-add";
    }

    @GetMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, Model model){
        PetUtil petUtil = this.frontPetUtilService.findPetUtilById(id);
        if(petUtil != null){
            List<PetUtilType> petUtilTypeList = this.petUtilTypeService.findAll();
            model.addAttribute("petUtilTypes", petUtilTypeList);
            model.addAttribute("petUtil", petUtil);
        }
        return "pet-util-edit";
    }

    @GetMapping("/findPetUtilPage")
    public String findPetUtilPage(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "petUtilType", required = false) Integer petUtilType,
                               @RequestParam("exist") Integer exist,
                               @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                               Model model){
        Page<PetUtil> page = this.frontPetUtilService.findAllPetUtil(name, petUtilType, exist, pageNumber, pageSize);
        PageUtil<PetUtil> pageUtil = new PageUtil<>(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        model.addAttribute("petUtilPage", pageUtil);
        if(exist == 1){
            return "pet-util-list";
        }
        return "pet-util-del";
    }

    @PostMapping("/deleteOnePetUtil")
    @ResponseBody
    public String deleteOnePetUtil(@RequestParam("id") Integer id){
        try {
            this.frontPetUtilService.updateExist(id, 0);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/deleteMultiPetUtil")
    @ResponseBody
    public String deleteMultiPetUtil(@RequestParam("deleteStr") String deleteStr){
        String deleteIds[] = deleteStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : deleteIds){
            idList.add(Integer.parseInt(id));
        }
        try {
            this.frontPetUtilService.editStatusListByIdList(idList, 0);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/recoveryOnePetUtil")
    @ResponseBody
    public String recoveryOnePetUtil(@RequestParam("id") Integer id){
        try {
            this.frontPetUtilService.updateExist(id, 1);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/recoveryMultiPetUtil")
    @ResponseBody
    public String recoveryMultiPetUtil(@RequestParam("recoveryStr") String recoveryStr) {
        String recoveryId[] = recoveryStr.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : recoveryId){
            idList.add(Integer.parseInt(id));
        }
        try {
            this.frontPetUtilService.editStatusListByIdList(idList, 1);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/deleteThoroughPetUtil")
    @ResponseBody
    public String deleteThoroughPetUtil(@RequestParam("id") Integer id){
        try {
            this.frontPetUtilService.deletePetUtilById(id);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/addPetUtil")
    @ResponseBody
    public String addPetUtil(@RequestParam("name") String name,
                              @RequestParam("description") String description,
                              @RequestParam("price") Integer price,
                              @RequestParam("value") Integer value,
                              @RequestParam("petUtilType") Integer type,
                              @RequestParam("upload") MultipartFile file){
        if(file.getOriginalFilename().equals("")){
            return Result.NULL;
        }

        try {
            String img = "";
            PetUtilType petUtilType = this.petUtilTypeService.findPetUtilTypeById(type);
            PetUtil petUtil = new PetUtil(name, description, value, price, petUtilType);
            Integer id = this.frontPetUtilService.save(petUtil);
            entityManager.clear();
            String fileName = id + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + file.getOriginalFilename();
            img = this.petUtilPhotoFolderName + "/" + fileName;
            FileCopyUtils.copy(file.getBytes(), new File(this.petUtilPhotoFileLocation, fileName));
            PetUtil editPetUtil = this.frontPetUtilService.findPetUtilById(id);
            editPetUtil.setImg(img);
            this.frontPetUtilService.save(editPetUtil);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

    @PostMapping("/updatePetUtil")
    @ResponseBody
    public String updatePetUtil(@RequestParam("id") Integer id,
                                @RequestParam("name") String name,
                                @RequestParam("description") String description,
                                @RequestParam("price") Integer price,
                                @RequestParam("value") Integer value,
                                @RequestParam("petUtilType") Integer type,
                                @RequestParam("upload") MultipartFile file){
        try {
            PetUtil petUtil = this.frontPetUtilService.findPetUtilById(id);
            PetUtilType petUtilType = this.petUtilTypeService.findPetUtilTypeById(type);
            File file1 = new File(this.petUtilPhotoFileLocation + "/" + petUtil.getImg());
            if (file1.exists()) {
                file1.delete();
            }
            String fileName = id + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + file.getOriginalFilename();
            FileCopyUtils.copy(file.getBytes(), new File(this.petUtilPhotoFileLocation, fileName));
            petUtil.setName(name);
            petUtil.setDescription(description);
            petUtil.setImg(this.petUtilPhotoFolderName + "/" + fileName);
            petUtil.setPrice(price);
            petUtil.setValue(value);
            petUtil.setPetUtilType(petUtilType);
            this.frontPetUtilService.save(petUtil);
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

}
