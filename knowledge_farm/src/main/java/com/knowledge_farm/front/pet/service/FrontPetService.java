package com.knowledge_farm.front.pet.service;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.pet.dao.PetDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<Pet> findAllPet(String name, Integer exist, Integer pageNumber, Integer pageSize){
        if(name != null && !name.equals("")){
            return this.petDao.findAllPetByNameAndExist(name, exist, PageRequest.of(pageNumber - 1, pageSize));
        }
        return this.petDao.findAllPetByExist(exist, PageRequest.of(pageNumber - 1, pageSize));
    }

    public Pet findPetById(int id){
        return petDao.findPetById(id);
    }

    @Transactional(readOnly = false)
    public void updateExist(Integer id, Integer exist){
        Pet pet = this.petDao.findPetById(id);
        pet.setExist(exist);
    }

    @Transactional(readOnly = false)
    public void editStatusListByIdList(List<Integer> idList, Integer exist){
        List<Pet> pets = this.petDao.findAllById(idList);
        for(Pet pet : pets){
            pet.setExist(exist);
        }
        this.petDao.saveAll(pets);
    }

    @Transactional(readOnly = false)
    public void deletePetById(Integer id){
        Pet pet = this.petDao.findPetById(id);
        this.petDao.delete(pet);
    }

    @Transactional(readOnly = false)
    public int addPet(Pet pet){
        return petDao.save(pet).getId();
    }

    @Transactional(readOnly = false)
    public int updatePet(Pet pet){
        return petDao.saveAndFlush(pet).getId();
    }

}
