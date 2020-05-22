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
    public int changePet(Integer userId, Integer willUsingPetId){
        User user = this.userService.findUserById(userId);
        Set<UserPetHouse> userPetHouses = user.getPetHouses();
        int count = 0;
        int count1 = -1;
        int count2 = -1;
        int result = 0;

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
            return 0;
        }
        count = 0;
        for(UserPetHouse userPetHouse : userPetHouses){
            if(count == count1){
                userPetHouse.setIfUsing(0);
            }
            if(count == count2){
                userPetHouse.setIfUsing(1);
                result = userPetHouse.getId();
            }
        }
        return result;
    }

    @Transactional(readOnly = false)
    public String updatePhysical(Integer userId){
        User user = this.userService.findUserById(userId);
        for(UserPetHouse userPetHouse : user.getPetHouses()){
            if(userPetHouse.getIfUsing() == 1){
                Integer physical = userPetHouse.getPhysical();
                if(physical > 0){
                    userPetHouse.setPhysical(physical - 1);
                    return Result.TRUE;
                }
                return Result.NOT_ENOUGH_PHYSICAL;
            }
        }
        return Result.FALSE;
    }

    @Transactional(readOnly = false)
    public String updateData(Integer userId, Integer result) {
        User user = this.userService.findUserById(userId);
        Set<UserPetHouse> userPetHouses = user.getPetHouses();
        for(UserPetHouse userPetHouse : userPetHouses) {
            if (userPetHouse.getIfUsing() == 1 && result == 1) {
                Integer userPetIntelligence = userPetHouse.getIntelligence();
                Integer petIntelligence = userPetHouse.getIntelligence();
                userPetHouse.setIntelligence(userPetIntelligence + 5);
                switch (userPetHouse.getGrowPeriod()){
                    case 0:
                        if(userPetIntelligence + 5 >= petIntelligence * 3){
                            return Result.UP;
                        }
                    case 1:
                        if(userPetIntelligence + 5 >= petIntelligence * 5){
                            return Result.UP;
                        }
                }
                return Result.TRUE;
//                if(userPetHouse.getPhysical() == 0){
//                    return Result.NOT_ENOUGH_PHYSICAL;
//                }
//                userPetHouse.setPhysical(userPetHouse.getPhysical()-1);
//                if(result == 1){
//                    if(userPetHouse.getIntelligence() < userPetHouse.getPet().getIntelligence()*5){
//                        userPetHouse.setIntelligence(userPetHouse.getIntelligence()+5);
//                        return Result.SUCCEED;
//                    }
//                    return Result.FULL;
//                }
            }
        }
        return Result.FALSE;
    }

}
