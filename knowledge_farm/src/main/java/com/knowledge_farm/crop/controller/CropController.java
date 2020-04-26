package com.knowledge_farm.crop.controller;

import com.knowledge_farm.crop.service.CropServiceImpl;
import com.knowledge_farm.entity.Crop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@Api(description = "前台作物接口")
@RestController
@RequestMapping("/crop")
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
    @ApiOperation(value = "加载商店所有作物", notes = "返回值：List（Crop）")
    @ApiImplicitParam
    @GetMapping("/initCrop")
    public List<Crop> initCrop(){
        List<Crop> crops = this.cropService.findAllCropByExist(1);
//        for(Crop crop : crops){
//            crop.setImg1(photoUrl + crop.getImg1());
//            crop.setImg2(photoUrl + crop.getImg2());
//            crop.setImg3(photoUrl + crop.getImg3());
//            crop.setImg4(photoUrl + crop.getImg4());
//        }
        return crops;
    }

}
