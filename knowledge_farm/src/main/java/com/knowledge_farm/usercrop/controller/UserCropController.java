package com.knowledge_farm.usercrop.controller;

import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.entity.UserCrop;
import com.knowledge_farm.usercrop.service.UserCropServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserCropController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 14:05
 */
@RestController
@RequestMapping("/usercrop")
@PropertySource(value = {"classpath:photo.properties"})
public class UserCropController {
    @Resource
    private UserCropServiceImpl userCropService;
    @Value("${file.photoUrl}")
    private String photoUrl;

    /**
     * @Author 张帅华
     * @Description 查询用户种植作物
     * @Date 14:09 2020/4/10 0010
     * @Param [userId]
     * @return java.util.List<com.atguigu.farm.entity.UserCrop>
     **/
    @RequestMapping("/initUserCrop")
    public List<UserCrop> initUserCrop(@RequestParam("userId") Integer userId){
        List<UserCrop> userCrops = this.userCropService.initUserCrop(userId);
        for(UserCrop userCrop : userCrops){
            if(userCrop != null){
                Crop crop = userCrop.getCrop();
                if(crop != null){
                    if(crop.getImg1().contains("http://")){
                        continue;
                    }
                    crop.setImg1(photoUrl + crop.getImg1());
                    crop.setImg2(photoUrl + crop.getImg2());
                    crop.setImg3(photoUrl + crop.getImg3());
                    crop.setImg4(photoUrl + crop.getImg4());
                }
            }
        }
        return userCrops;
    }

    /**
     * @Author 张帅华
     * @Description 查看作物进度
     * @Date 15:00 2020/4/10 0010
     * @Param [userId]
     * @return int
     **/
    @RequestMapping("/getCropProgress")
    public int getCropProgress(@RequestParam("userId") Integer userId, @RequestParam("landNumber") String landNumber ){
        return this.userCropService.getCropProgress(userId, landNumber);
    }

}
