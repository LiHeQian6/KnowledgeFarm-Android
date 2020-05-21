package com.knowledge_farm.pet.service;

import com.knowledge_farm.entity.*;
import com.knowledge_farm.pet.dao.PetDao;
import com.knowledge_farm.user.service.UserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
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

    public List<PetVO> showAllPetInStore(Integer userId){
        User user = this.userService.findUserById(userId);
        List<Pet> petList = this.petDao.findAllByExist(1);
        List<PetVO> petVOList = new ArrayList<>();
        Set<UserPetHouse> petHouseSet = user.getPetHouses();
        for(Pet pet : petList){
            PetVO petVO = new PetVO(pet);
            for(UserPetHouse petHouse : petHouseSet){
                if(petHouse.getPet().getId() == pet.getId()){
                    petVO.setOwn(1);
                    break;
                }
            }
            petVOList.add(petVO);
        }
        return petVOList;
    }

    public Pet findPetById(int petId){
        return petDao.findPetById(petId);
    }

    @Transactional(readOnly = false)
        public String changePet(Integer userId, Integer willUsingPetId){
        User user = this.userService.findUserById(userId);
        Set<UserPetHouse> userPetHouses = user.getPetHouses();
        int count = 0;
        int count1 = -1;
        int count2 = -1;

        for(UserPetHouse userPetHouse : userPetHouses){
            if(userPetHouse.getIfUsing() == 1){
                count1 = count;;
            }
            if(userPetHouse.getPet().getId() == willUsingPetId){
                count2 = count;
            }
            if(count1 != -1 && count2 != -1){
                break;
            }
            count++;
        }
        if(count1 == -1 || count2 == -1){
            return Result.FALSE;
        }
        count = 0;
        for(UserPetHouse userPetHouse : userPetHouses){
            if(count == count1){
                userPetHouse.setIfUsing(0);
            }
            if(count == count2){
                userPetHouse.setIfUsing(1);
            }
        }
        return Result.TRUE;
    }

//    @Transactional(readOnly = false)
//    public int savePetFood(PetFood petFood){
//        return petFoodDao.saveAndFlush(petFood).getId();
//    }
    @Transactional(readOnly = false)
    public String updateData(Integer userId, Integer result) {
        User user = this.userService.findUserById(userId);
        Set<UserPetHouse> userPetHouses = user.getPetHouses();
        for(UserPetHouse userPetHouse : userPetHouses) {
            if (userPetHouse.getIfUsing() == 1) {
                Pet pet = userPetHouse.getPet();
                if(result == 1){
                    if(userPetHouse.getIntelligence()<pet.getIntelligence()*5){
                        userPetHouse.setIntelligence(userPetHouse.getIntelligence()+5);
                        if(userPetHouse.getIntelligence()>=2*pet.getIntelligence()*2 && userPetHouse.getIntelligence()< 4*pet.getIntelligence()*2){
                            return Result.UP;
                        }
                        return Result.SUCCEED;
                    }
                    return Result.FULL;
                }
            }
        }
        return Result.FAIL;
    }
}
