package com.knowledge_farm.front.crop.controller;

import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.front.crop.service.FrontCropService;
import com.knowledge_farm.util.PageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName CropController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-15 23:20
 */
@Controller
@RequestMapping("/admin/crop")
@PropertySource(value = {"classpath:photo.properties"})
public class FrontCropController {
    @Resource
    private FrontCropService frontCropService;
    @Resource
    private EntityManager entityManager;
    @Value("${file.cropPhotoFolderName}")
    private String cropPhotoFolderName;
    @Value("${file.cropPhotoFileLocation}")
    private String cropPhotoFileLocation;

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "crop-add";
    }

    @RequestMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, Model model){
        Crop crop = this.frontCropService.findCropById(id);
        model.addAttribute("crop", crop);
        return "crop-edit";
    }

    @RequestMapping("/findCropPage")
    public String findCropPage(@RequestParam(value = "name", required = false) String name,
                               @RequestParam("exist") Integer exist,
                               @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                               HttpSession session, Model model){
        session.removeAttribute("crop");
        Page<Crop> page = this.frontCropService.findAllCrop(name, exist, pageNumber, pageSize);
        PageUtil<Crop> pageUtil = new PageUtil<>(pageNumber, pageSize);
        pageUtil.setTotalCount((int) page.getTotalElements());
        pageUtil.setList(page.getContent());
        model.addAttribute("cropPage", pageUtil);
        if(exist == 1){
            return "crop-list";
        }
        return "crop-del";
    }

    @RequestMapping("/deleteOneCrop")
    @ResponseBody
    public String deleteOneCrop(@RequestParam("id") Integer id){
        return this.frontCropService.updateExist(id, 0);
    }

    @RequestMapping("/deleteMultiCrop")
    @ResponseBody
    public String deleteMultiCrop(@RequestParam("deleteStr") String deleteStr){
        String deleteId[] = deleteStr.split(",");
        String result = "";
        for(String id : deleteId){
            result = this.frontCropService.updateExist(Integer.parseInt(id), 0);
            if(!result.equals("succeed")){
                break;
            }
        }
        return result;
    }

    @RequestMapping("/recoveryOneCrop")
    @ResponseBody
    public String recoveryOneCrop(@RequestParam("id") Integer id){
        return this.frontCropService.updateExist(id, 1);
    }

    @RequestMapping("/recoveryMultiCrop")
    @ResponseBody
    public String recoveryMultiCrop(@RequestParam("recoveryStr") String recoveryStr) {
        String recoveryId[] = recoveryStr.split(",");
        String result = "";
        for (String id : recoveryId) {
            result = this.frontCropService.updateExist(Integer.parseInt(id), 1);
            if (!result.equals("succeed")) {
                break;
            }
        }
        return result;
    }

    @RequestMapping("/deleteThoroughCrop")
    @ResponseBody
    public String deleteThoroughCrop(@RequestParam("id") Integer id){
        return this.frontCropService.deleteCropById(id);
    }

    @RequestMapping("/addCrop")
    @ResponseBody
    public String addCrop(@RequestParam("name") String name,
                          @RequestParam("price") Integer price,
                          @RequestParam("matureTime") Integer matureTime,
                          @RequestParam("value") Integer value,
                          @RequestParam("experience") Integer experience,
                          @RequestParam("upload") MultipartFile files[]){
        for(MultipartFile multipartFile : files) {
            if (multipartFile.getOriginalFilename().equals("")) {
                return "null";
            }
        }

        try {
            Crop crop = new Crop();
            crop.setName(name);
            crop.setPrice(price);
            crop.setMatureTime(matureTime);
            crop.setValue(value);
            crop.setExperience(experience);
            Crop saveCrop = this.frontCropService.save(crop);
            if(saveCrop != null){
                Integer id = saveCrop.getId();
                entityManager.clear();
                String img[] = new String[4];
                int count = 1;
                for(MultipartFile multipartFile : files){
                    String fileName = id + "_" + count + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + multipartFile.getOriginalFilename();
                    img[count-1] = this.cropPhotoFolderName + "/" + fileName;
                    FileCopyUtils.copy(multipartFile.getBytes(), new File(this.cropPhotoFileLocation, fileName));
                    count++;
                }

                Crop editCrop = this.frontCropService.findCropById(id);
                editCrop.setImg1(img[0]);
                editCrop.setImg2(img[1]);
                editCrop.setImg3(img[2]);
                editCrop.setImg4(img[3]);
                if(this.frontCropService.save(editCrop) != null){
                    return "succeed";
                }
            }
            return "fail";
        }catch (NullPointerException e){
            return null;
        }catch (Exception e){
            return "fail";
        }
    }

    @RequestMapping("/updateCrop")
    @ResponseBody
    public String updateCrop(Crop crop, @RequestParam("upload") MultipartFile files[]) {
        Crop findCropById = this.frontCropService.findCropById(crop.getId());
        crop.setExist(findCropById.getExist());
        Integer id = crop.getId();
        int count = 1;
        try {
            for (MultipartFile multipartFile : files) {
                if (!multipartFile.getOriginalFilename().equals("")) {
                    switch (count) {
                        case 1:
                            File file = new File(this.cropPhotoFileLocation + "/" + crop.getImg1());
                            if (file.exists()) {
                                file.delete();
                            }
                            String fileName = id + "_" + count + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + multipartFile.getOriginalFilename();
                            crop.setImg1(this.cropPhotoFolderName + "/" + fileName);
                            FileCopyUtils.copy(multipartFile.getBytes(), new File(this.cropPhotoFileLocation, fileName));
                            break;
                        case 2:
                            File file2 = new File(this.cropPhotoFileLocation + "/" + crop.getImg2());
                            if (file2.exists()) {
                                file2.delete();
                            }
                            String fileName2 = id + "_" + count + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + multipartFile.getOriginalFilename();
                            crop.setImg2(this.cropPhotoFolderName + "/" + fileName2);
                            FileCopyUtils.copy(multipartFile.getBytes(), new File(this.cropPhotoFileLocation, fileName2));
                            break;
                        case 3:
                            File file3 = new File(this.cropPhotoFileLocation + "/" + crop.getImg3());
                            if (file3.exists()) {
                                file3.delete();
                            }
                            String fileName3 = id + "_" + count + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + multipartFile.getOriginalFilename();
                            crop.setImg3(this.cropPhotoFolderName + "/" + fileName3);
                            FileCopyUtils.copy(multipartFile.getBytes(), new File(this.cropPhotoFileLocation, fileName3));
                            break;
                        case 4:
                            File file4 = new File(this.cropPhotoFileLocation + "/" + crop.getImg4());
                            if (file4.exists()) {
                                file4.delete();
                            }
                            String fileName4 = id + "_" + count + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "_" + multipartFile.getOriginalFilename();
                            crop.setImg4(this.cropPhotoFolderName + "/" + fileName4);
                            FileCopyUtils.copy(multipartFile.getBytes(), new File(this.cropPhotoFileLocation, fileName4));
                            break;
                    }
                }
                count++;
            }
            this.frontCropService.save(crop);
            return "succeed";
        }catch (NullPointerException | IOException e){
            return null;
        }catch (Exception e){
            return "fail";
        }
    }

}
