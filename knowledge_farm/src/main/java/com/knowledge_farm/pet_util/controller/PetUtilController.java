package com.knowledge_farm.pet_util.controller;

import com.knowledge_farm.entity.PetUtil;
import com.knowledge_farm.pet_util.service.PetUtilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName PetUtilController
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 10:03
 */
@Api(description = "前台宠物道具接口")
@RestController
@RequestMapping("/petutil")
public class PetUtilController {
    @Resource
    private PetUtilService petUtilService;

    @ApiOperation(value = "商店展示所有饲料或工具", notes = "返回值：List（PetUtil）")
    @GetMapping("showInStore")
    public List<PetUtil> showInStore(){
        return this.petUtilService.showInStore();
    }

}
