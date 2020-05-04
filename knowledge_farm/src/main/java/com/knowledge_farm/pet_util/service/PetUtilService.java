package com.knowledge_farm.pet_util.service;

import com.knowledge_farm.entity.PetUtil;
import com.knowledge_farm.pet_util.dao.PetUtilDao;
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
public class PetUtilService {
    @Resource
    private PetUtilDao petUtilDao;

    public List<PetUtil> showInStore(){
        return this.petUtilDao.findAllByExist(1);
    }

    public PetUtil findPetFoodById(Integer id){
        return this.petUtilDao.findPetFoodById(id);
    }

}
