package com.knowledge_farm.front.pet_util.service;

import com.knowledge_farm.entity.PetUtil;
import com.knowledge_farm.pet_util.dao.PetUtilDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FrontPetFoodService
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 14:37
 */
@Service
@Transactional(readOnly = true)
public class FrontPetUtilService {
    @Resource
    private PetUtilDao petUtilDao;

    public PetUtil findPetUtilById(Integer id){
        return this.petUtilDao.findPetUtilById(id);
    }

    public Page<PetUtil> findAllPetUtil(String name, Integer petUtilTypeId, Integer exist, Integer pageNumber, Integer pageSize){
        if(name != null && !name.equals("")){
            if(petUtilTypeId != null && petUtilTypeId != 0){
                return this.petUtilDao.findPetUtilByNameAndPetUtilTypeAndExist(name, petUtilTypeId, exist, PageRequest.of(pageNumber - 1, pageSize));
            }
            return this.petUtilDao.findPetUtilByNameAndExist(name, exist, PageRequest.of(pageNumber - 1, pageSize));
        }
        if (petUtilTypeId != null && petUtilTypeId != 0) {
            return this.petUtilDao.findPetUtilByPetUtilTypeAndExist(petUtilTypeId, exist, PageRequest.of(pageNumber - 1, pageSize));
        }
        return this.petUtilDao.findPetUtilByExist(exist, PageRequest.of(pageNumber - 1, pageSize));
    }

    @Transactional(readOnly = false)
    public void updateExist(Integer id, Integer exist){
        PetUtil petUtil = this.petUtilDao.findPetUtilById(id);
        petUtil.setExist(exist);
    }

    @Transactional(readOnly = false)
    public void editStatusListByIdList(List<Integer> idList, Integer exist){
        List<PetUtil> petUtils = this.petUtilDao.findAllById(idList);
        for(PetUtil petUtil : petUtils){
            petUtil.setExist(exist);
        }
        this.petUtilDao.saveAll(petUtils);
    }

    @Transactional(readOnly = false)
    public void deletePetUtilById(Integer id){
        PetUtil petUtil = this.petUtilDao.findPetUtilById(id);
        this.petUtilDao.delete(petUtil);
    }

    @Transactional(readOnly = false)
    public int save(PetUtil petUtil){
        return this.petUtilDao.save(petUtil).getId();
    }

}
