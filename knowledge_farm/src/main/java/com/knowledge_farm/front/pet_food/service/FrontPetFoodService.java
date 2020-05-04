package com.knowledge_farm.front.pet_food.service;

import com.knowledge_farm.pet_util.dao.PetUtilDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName FrontPetFoodService
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 14:37
 */
@Service
@Transactional(readOnly = true)
public class FrontPetFoodService {
    @Resource
    private PetUtilDao petUtilDao;

}
