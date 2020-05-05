package com.knowledge_farm.pet_util_type.service;

import com.knowledge_farm.entity.PetUtilType;
import com.knowledge_farm.pet_util_type.dao.PetUtilTypeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName PetUtilService
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 18:03
 */
@Service
@Transactional(readOnly = true)
public class PetUtilTypeService {
    @Resource
    private PetUtilTypeDao petUtilTypeDao;

    public List<PetUtilType> findAll() {
        return this.petUtilTypeDao.findAll();
    }

    public PetUtilType findPetUtilTypeById(Integer id){
        return this.petUtilTypeDao.findPetUtilTypeById(id);
    }

}
