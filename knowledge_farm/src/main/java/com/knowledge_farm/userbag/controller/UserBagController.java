package com.knowledge_farm.userbag.controller;

import com.knowledge_farm.entity.BagItem;
import com.knowledge_farm.userbag.service.UserBagServiceImpl;
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

    /**
     * @Author 张帅华
     * @Description 查询用户背包中所有作物
     * @Date 13:57 2020/4/10 0010
     * @Param [userId]
     * @return java.util.List<com.atguigu.farm.entity.BagCropItem>
     **/
    @RequestMapping("/initUserBag")
    public List<BagItem> initUserBag(@RequestParam("userId") Integer userId){
        return this.userBagService.initUserBag(userId);
    }

}
