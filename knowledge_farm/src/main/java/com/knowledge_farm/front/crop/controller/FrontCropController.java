package com.knowledge_farm.front.crop.controller;

import com.knowledge_farm.front.crop.service.FrontCropService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName CropController
 * @Description
 * @Author 张帅华
 * @Date 2020-04-15 23:20
 */
@RestController
@RequestMapping("/admin/crop")
public class FrontCropController {
    @Resource
    private FrontCropService frontCropService;

}
