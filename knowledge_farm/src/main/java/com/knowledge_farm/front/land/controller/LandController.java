package com.knowledge_farm.front.land.controller;

import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.entity.Land;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.front.land.service.LandService;
import com.knowledge_farm.util.PageUtil;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName LandController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-19 08:43
 */
@Api(description = "后台土地接口")
@Controller
@RequestMapping("/admin/land")
public class LandController {
    @Resource
    private LandService landService;

    @GetMapping("/findPageLand")
    public String findPageLand(@RequestParam(value = "account", required = false) String account,
                               @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                               HttpServletRequest request){
        Object obj = this.landService.findAllLandByAccount(account, pageNumber, pageSize);
        if(obj instanceof Page){
            Page<Land> page = (Page) obj;
            PageUtil<Land> pageUtil = new PageUtil(pageNumber, pageSize);
            pageUtil.setTotalCount((int) ((Page) obj).getTotalElements());
//            for(Land land : page.getContent()){
//                User user = land.getUser();
//                user.setPassword("");
//            }
            pageUtil.setList(page.getContent());
            request.setAttribute("landPage", pageUtil);
            return "member-land-list";
        }
        request.setAttribute("landPage", obj);
        return "member-land-list";
    }

    @GetMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, HttpServletRequest request){
        Land land = this.landService.findLandById(id);
        List<Crop> crops = this.landService.findAllCrop();
        if(land != null){
//            User user = land.getUser();
//            user.setPassword("");
            request.setAttribute("land", land);
            request.setAttribute("crops", crops);
        }
        return "member-land-edit";
    }

    @PostMapping("/editLand")
    @ResponseBody
    public String editLand(@RequestParam("userId") Integer userId,
                           @RequestParam("landNumber") String landNumber,
                           @RequestParam("waterLimit") Integer waterLimit,
                           @RequestParam("fertilizerLimit") Integer fertilizerLimit,
                           @RequestParam("progress") Integer progress,
                           @RequestParam("status") Integer status,
                           @RequestParam("flag") Integer flag,
                           HttpServletRequest request){
        try {
            int result = this.landService.editLand(userId, landNumber, waterLimit, fertilizerLimit, progress, status, flag);
            if(result != 0){
                request.setAttribute("StartUserCropGrowJob", new Integer[]{userId, result, Integer.parseInt(landNumber.substring(4))});
            }
            return Result.SUCCEED;
        }catch (Exception e){
            e.printStackTrace();
            return Result.FAIL;
        }
    }

}
