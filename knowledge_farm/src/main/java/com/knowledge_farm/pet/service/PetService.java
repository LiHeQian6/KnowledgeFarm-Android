package com.knowledge_farm.pet.service;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.pet.dao.PetDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @program: knowledge_farm
 * @description: petService
 * @author: 景光赞
 * @create: 2020-04-27 11:28
 **/
@Service
@Transactional(readOnly = true)
public class PetService {
    @Resource
    private PetDao petDao;

    public Page<Pet> showAllPetInStore(Pageable pageable){
        return petDao.findAllByIsOwn(0,pageable);
    }

    public Pet findPetById(int petId){
        return petDao.findPetById(petId);
    }
}
