package com.knowledge_farm.front.land.controller;

import com.knowledge_farm.crop.service.CropServiceImpl;
import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.entity.Land;
import com.knowledge_farm.front.land.service.LandService;
import com.knowledge_farm.util.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName LandController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-19 08:43
 */
@Controller
@RequestMapping("/admin/land")
public class LandController {
    @Resource
    private LandService landService;

    @RequestMapping("/findPageLand")
    public String findPageLand(@RequestParam(value = "account", required = false) String account,
                                       @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                       Model model){
        Object obj = this.landService.findAllLandByAccount(account, pageNumber, pageSize);
        if(obj instanceof Page){
            Page page = (Page) obj;
            PageUtil<Land> pageUtil = new PageUtil(pageNumber, pageSize);
            pageUtil.setTotalCount((int) ((Page) obj).getTotalElements());
            pageUtil.setList(page.getContent());
            model.addAttribute("landPage", pageUtil);
            return "member-land-list";
        }
        model.addAttribute("landPage", (PageUtil) obj);
        return "member-land-list";
    }

    @RequestMapping("/toEdit")
    public String toEdit(@RequestParam("id") Integer id, Model model){
        Land land = this.landService.findLandById(id);
        List<Crop> crops = this.landService.findAllCrop();
        if(land != null){
            model.addAttribute("land", land);
            model.addAttribute("crops", crops);
        }
        return "member-land-edit";
    }

    @RequestMapping("/editLand")
    @ResponseBody
    public String editLand(@RequestParam("userId") Integer userId,
                           @RequestParam("landNumber") String landNumber,
                           @RequestParam("waterLimit") Integer waterLimit,
                           @RequestParam("fertilizerLimit") Integer fertilizerLimit,
                           @RequestParam("progress") Integer progress,
                           @RequestParam("status") Integer status,
                           @RequestParam("flag") Integer flag){
        return this.landService.editLand(userId, landNumber, waterLimit, fertilizerLimit, progress, status, flag);
    }

}
