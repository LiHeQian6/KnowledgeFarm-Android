package com.knowledge_farm;

import com.knowledge_farm.crop.service.CropServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class FarmApplicationTests {
    @Resource
    private CropServiceImpl cropService;

    @Test
    void contextLoads() {
        System.out.println(cropService.findAllCropByExist(1));
    }

}
