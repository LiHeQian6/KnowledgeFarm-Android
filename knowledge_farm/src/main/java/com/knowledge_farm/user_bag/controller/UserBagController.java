package com.knowledge_farm.user_bag.controller;

import com.knowledge_farm.entity.BagCropItem;
import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.user_bag.service.UserBagServiceImpl;
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
    public List<BagCropItem> initUserBag(@RequestParam("userId") Integer userId){
        List<BagCropItem> bagCropItems = this.userBagService.initUserBag(userId);
        for(BagCropItem item : bagCropItems){
            Crop crop = item.getCrop();
            crop.setImg1(photoUrl + crop.getImg1());
            crop.setImg2(photoUrl + crop.getImg2());
            crop.setImg3(photoUrl + crop.getImg3());
            crop.setImg4(photoUrl + crop.getImg4());
        }
        return bagCropItems;
    }

}
