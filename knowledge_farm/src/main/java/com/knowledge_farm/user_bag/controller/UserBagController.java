package com.knowledge_farm.user_bag.controller;

import com.knowledge_farm.entity.BagCropItem;
import com.knowledge_farm.entity.BagPetUtilItem;
import com.knowledge_farm.user_bag.service.UserBagServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserBagController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 13:48
 */
@Api(description = "前台背包接口")
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
    @ApiOperation(value = "查询用户背包中所有作物", notes = "返回值：List（BagCropItem）")
    @GetMapping("/initUserBag")
    public List<BagCropItem> initUserCropBag(HttpSession session, HttpServletResponse response){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId != null) {
                List<BagCropItem> bagCropItems = this.userBagService.initUserCropBag(userId);
                return bagCropItems;
            }
            response.sendError(401);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
