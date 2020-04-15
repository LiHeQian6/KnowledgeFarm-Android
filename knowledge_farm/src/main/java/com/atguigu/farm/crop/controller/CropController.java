package com.atguigu.farm.crop.controller;

import com.atguigu.farm.crop.service.CropServiceImpl;
import com.atguigu.farm.entity.Crop;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CropController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 13:22
 */
@RestController
@RequestMapping("/crop")
public class CropController {
    @Resource
    private CropServiceImpl cropService;

    /**
     * @Author 张帅华
     * @Description 查询商店所有作物
     * @Date 13:56 2020/4/10 0010
     * @Param []
     * @return java.util.List<com.atguigu.farm.entity.Crop>
     **/
    @RequestMapping("/initCrop")
    public List<Crop> initCrop(){
        return this.cropService.findAllCropByExist(1);
    }

}
