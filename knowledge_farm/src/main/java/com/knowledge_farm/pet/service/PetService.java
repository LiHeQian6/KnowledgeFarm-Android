package com.knowledge_farm.pet.service;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.entity.PetVO;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserPetHouse;
import com.knowledge_farm.pet.dao.PetDao;
import com.knowledge_farm.user.service.UserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
//    @Resource
//    private PetFoodDao petFoodDao;

    @Transactional(readOnly = false)
    public List<PetVO> showAllPetInStore(Integer userId){
        User user = userService.findUserById(userId);
        List<Pet> list0 =  petDao.findAllByExist(1);
        List<PetVO> petVOS = new ArrayList<>();
        for(int i = 0;i<list0.size();i++){
            petVOS.add(i,new PetVO(list0.get(i)));
        }
        Set<UserPetHouse> petHouses = user.getPetHouses();
        System.out.println(petHouses.toString());
        for(int j = 0;j<petVOS.size();j++){
            for(UserPetHouse petHouse : petHouses){
                if(petVOS.get(j).getId().equals(petHouse.getPet().getId())){
                    System.out.println(petVOS.get(j));
                    System.out.println(new PetVO(petHouse.getPet()));
                    petVOS.get(j).setOwn(1);
                }else {
                    petVOS.get(j).setOwn(0);
                }
            }
        }
        return petVOS;
    }

    public Pet findPetById(int petId){
        return petDao.findPetById(petId);
    }

    @Transactional(readOnly = false)
    public void changePet(Integer userId, Integer willUsingPetId){
        User user = this.userService.findUserById(userId);
        Set<UserPetHouse> userPetHouses = user.getPetHouses();
        int isChanging1 = 0;
        int isChanging2 = 0;
        for(UserPetHouse userPetHouse : userPetHouses){
            if(userPetHouse.getIfUsing() == 1){
                userPetHouse.setIfUsing(0);
                isChanging1 = 1;
            }
            if(userPetHouse.getPet().getId() == willUsingPetId){
                userPetHouse.setIfUsing(1);
                isChanging2 = 1;
            }
            if(isChanging1 == 1 && isChanging2 == 1){
                break;
            }
        }
    }

//    @Transactional(readOnly = false)
//    public int savePetFood(PetFood petFood){
//        return petFoodDao.saveAndFlush(petFood).getId();
//    }

}
