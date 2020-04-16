package com.knowledge_farm.crop.controller;

import com.knowledge_farm.crop.service.CropServiceImpl;
import com.knowledge_farm.entity.Crop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CropController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 13:22
 */
@RestController
@RequestMapping("/crop")
@PropertySource(value = {"classpath:photo.properties"})
public class CropController {
    @Resource
    private CropServiceImpl cropService;
    @Value("${file.photoUrl}")
    private String photoUrl;

    /**
     * @Author 张帅华
     * @Description 查询商店所有作物
     * @Date 13:56 2020/4/10 0010
     * @Param []
     * @return java.util.List<com.atguigu.farm.entity.Crop>
     **/
    @RequestMapping("/initCrop")
    public List<Crop> initCrop(){
        List<Crop> crops = this.cropService.findAllCropByExist(1);
        int count = 0;
        for(Crop crop : crops){
            crop.setImg1(photoUrl + crop.getImg1());
            crop.setImg2(photoUrl + crop.getImg2());
            crop.setImg3(photoUrl + crop.getImg3());
            crop.setImg4(photoUrl + crop.getImg4());
            crops.set(count, crop);
        }
        return crops;
    }

}
