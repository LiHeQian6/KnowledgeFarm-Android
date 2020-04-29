package com.knowledge_farm.front.pet.service;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.pet.dao.PetDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: knowledge_farm
 * @description: petService
 * @author: 景光赞
 * @create: 2020-04-29 18:00
 **/
@Service
@Transactional(readOnly = true)
public class FrontPetService {
    @Resource
    private PetDao petDao;

    public List<Pet> findAllPet(){
        return petDao.findAll();
    }

    public Pet findPetById(int id){
        return petDao.findPetById(id);
    }

    @Transactional(readOnly = false)
    public int addPet(Pet pet){
        return petDao.save(pet).getId();
    }

    @Transactional(readOnly = false)
    public int updatePet(Pet pet){
        return petDao.saveAndFlush(pet).getId();
    }

    @Transactional(readOnly = false)
    public void deletePet(int id){
        petDao.delete(findPetById(id));
    }
}
