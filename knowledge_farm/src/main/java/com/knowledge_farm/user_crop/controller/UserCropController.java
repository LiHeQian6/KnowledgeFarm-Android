package com.knowledge_farm.user_crop.controller;

import com.knowledge_farm.entity.UserCrop;
import com.knowledge_farm.user_crop.service.UserCropServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
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
 * @ClassName UserCropController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 14:05
 */
@Api(description = "前台查看种植作物信息接口")
@RestController
@RequestMapping("/usercrop")
public class UserCropController {
    @Resource
    private UserCropServiceImpl userCropService;

    /**
     * @Author 张帅华
     * @Description 查询用户种植作物
     * @Date 14:09 2020/4/10 0010
     * @Param [userId]
     * @return java.util.List<com.atguigu.farm.entity.UserCrop>
     **/
    @ApiOperation(value = "查询用户种植作物", notes = "返回值：List（UserCrop）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/initUserCrop")
    public List<UserCrop> initUserCrop(@RequestParam("userId") Integer userId){
        try {
            List<UserCrop> userCrops = this.userCropService.initUserCrop(userId);
            return userCrops;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * @Author 张帅华
     * @Description 查看作物进度
     * @Date 15:00 2020/4/10 0010
     * @Param [userId]
     * @return int
     **/
    @ApiOperation(value = "查看作物进度", notes = "返回值：(String)作物进度 || -1：用户不存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "landNumber", value = "土地号", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/getCropProgress")
    public String getCropProgress(@RequestParam("landNumber") String landNumber, HttpSession session, HttpServletResponse response){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId != null) {
                return "" + this.userCropService.getCropProgress(userId, landNumber);
            }
            response.sendError(401);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "-1";
    }

}
