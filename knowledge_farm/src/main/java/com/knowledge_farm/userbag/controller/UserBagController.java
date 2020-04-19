package com.knowledge_farm.userbag.controller;

import com.knowledge_farm.entity.BagItem;
import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.userbag.service.UserBagServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserBagControllrt
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 13:48
 */
@RestController
@RequestMapping("/bag")
@PropertySource(value = {"classpath:photo.properties"})
public class UserBagController {
    @Resource
    private UserBagServiceImpl userBagService;
    @Value("${file.photoUrl}")
    private String photoUrl;

    /**
     * @Author 张帅华
     * @Description 查询用户背包中所有作物
     * @Date 13:57 2020/4/10 0010
     * @Param [userId]
     * @return java.util.List<com.atguigu.farm.entity.BagCropItem>
     **/
    @RequestMapping("/initUserBag")
    public List<BagItem> initUserBag(@RequestParam("userId") Integer userId){
        List<BagItem> bagItems = this.userBagService.initUserBag(userId);
        for(BagItem item : bagItems){
            Crop crop = item.getCrop();
            crop.setImg1(photoUrl + crop.getImg1());
            crop.setImg2(photoUrl + crop.getImg2());
            crop.setImg3(photoUrl + crop.getImg3());
            crop.setImg4(photoUrl + crop.getImg4());
        }
        return bagItems;
    }

}
