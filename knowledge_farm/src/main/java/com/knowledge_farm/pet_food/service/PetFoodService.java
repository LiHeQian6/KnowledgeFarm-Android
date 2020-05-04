package com.knowledge_farm.pet_food.service;

import com.knowledge_farm.entity.PetFood;
import com.knowledge_farm.pet_food.dao.PetFoodDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName PetFoodService
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 10:02
 */
@Service
@Transactional(readOnly = true)
public class PetFoodService {
    @Resource
    private PetFoodDao petFoodDao;

    public List<PetFood> showInStore(){
        return this.petFoodDao.findAllByExist(1);
    }

    public PetFood findPetFoodById(Integer id){
        return this.petFoodDao.findPetFoodById(id);
    }

}
