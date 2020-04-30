package com.knowledge_farm.pet.service;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserPetHouse;
import com.knowledge_farm.pet.dao.PetDao;
import com.knowledge_farm.user.service.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

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
    @Resource
    private UserServiceImpl userService;

    public List<Pet> showAllPetInStore(){
        return petDao.findAllByExist(1);
    }

    public Pet findPetById(int petId){
        return petDao.findPetById(petId);
    }

    @Transactional(readOnly = false)
    public void changePet(Integer userId, Integer usingPetId, Integer willUsingPetId){
        User user = this.userService.findUserById(userId);
        Set<UserPetHouse> userPetHouses = user.getPetHouses();
        int isChanging1 = 0;
        int isChanging2 = 0;
        for(UserPetHouse userPetHouse : userPetHouses){
            Integer petId = userPetHouse.getPet().getId();
            if(petId == usingPetId){
                userPetHouse.setIfUsing(0);
                isChanging1 = 1;
            }
            if(petId == willUsingPetId){
                userPetHouse.setIfUsing(1);
                isChanging2 = 1;
            }
            if(isChanging1 == 1 && isChanging2 == 1){
                break;
            }
        }
    }

}
