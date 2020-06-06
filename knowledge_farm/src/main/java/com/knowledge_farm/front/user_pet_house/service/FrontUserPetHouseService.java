package com.knowledge_farm.front.user_pet_house.service;

import com.knowledge_farm.entity.*;
import com.knowledge_farm.pet.service.PetService;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.user_pet_house.dao.UserPetHouseDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FrontUserPetHouseService
 * @Description
 * @Author 张帅华
 * @Date 2020-05-27 18:08
 */
@Service
@Transactional(readOnly = true)
public class FrontUserPetHouseService {
    @Resource
    private UserPetHouseDao userPetHouseDao;
    @Resource
    private PetService petService;
    @Resource
    private UserServiceImpl userService;

    public Page<UserPetHouse> findUserPetHousePage(String account, Integer petId, Integer pageNumber, Integer pageSize){
        Specification<UserPetHouse> spec = new Specification<UserPetHouse>() {
            @Override
            public Predicate toPredicate(Root<UserPetHouse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if(account != null && !account.equals("")){
                    Join<User, UserPetHouse> join1 = root.join("user", JoinType.INNER);
                    list.add(cb.equal(join1.get("account"), account));
                }
                if(petId != null && petId != 0) {
                    Join<Pet, UserPetHouse> join1 = root.join("pet", JoinType.INNER);
                    list.add(cb.equal(join1.get("id"), petId));
                }
                //此时条件之间是没有任何关系的。
                Predicate[] arr = new Predicate[list.size()];
                return cb.and(list.toArray(arr));
            }
        };
        return this.userPetHouseDao.findAll(spec, PageRequest.of(pageNumber - 1, pageSize));
    }

    public UserPetHouse findUserPetHouseById(Integer id){
        return this.userPetHouseDao.findUserPetHouseById(id);
    }

    public List<Pet> findAllPet(){
        return this.petService.findAllPet();
    }

    @Transactional(readOnly = false)
    public String editUserPetHouse(Integer id, Integer life, Integer intelligence, Integer physical, Integer ifUsing){
        UserPetHouse userPetHouse = this.userPetHouseDao.findUserPetHouseById(id);
        Pet pet = userPetHouse.getPet();
        if(userPetHouse.getIfUsing() == 1 && ifUsing == 0){
            return "不可修改为未使用状态";
        }
        if(physical > pet.getPhysical()){
            return "该宠物体力上限为" + pet.getPhysical();
        }
        userPetHouse.setLife(life);
        userPetHouse.setPhysical(physical);
        userPetHouse.setIntelligence(intelligence);
        if(intelligence < 3 * pet.getIntelligence()){
            userPetHouse.setGrowPeriod(0);
        }else if(intelligence >= 3*pet.getIntelligence() && intelligence < 5*pet.getIntelligence()){
            userPetHouse.setGrowPeriod(1);
        }else if(intelligence >= 5*pet.getIntelligence()){
            userPetHouse.setGrowPeriod(2);
        }
        if(userPetHouse.getIfUsing() != ifUsing){
            for(UserPetHouse userPetHouse1 : userPetHouse.getUser().getPetHouses()){
                if(userPetHouse1.getIfUsing() == 1){
                    userPetHouse1.setIfUsing(0);
                }
            }
            userPetHouse.setIfUsing(1);
        }
        return Result.SUCCEED;
    }

    public User findUserById(Integer userId){
        return this.userService.findUserById(userId);
    }

    @Transactional(readOnly = false)
    public String deleteOne(Integer id){
        UserPetHouse userPetHouse = this.userPetHouseDao.findUserPetHouseById(id);
        if(userPetHouse.getIfUsing() == 1){
            return "该宠物正在使用，不可删除";
        }
        this.userPetHouseDao.delete(userPetHouse);
        return Result.SUCCEED;
    }

    @Transactional(readOnly = false)
    public String deleteMulti(List<Integer> idList){
        List<UserPetHouse> userPetHouseList = this.userPetHouseDao.findAllById(idList);
        for(UserPetHouse userPetHouse : userPetHouseList){
            if(userPetHouse.getIfUsing() == 1){
                return "宠物正在使用，不可删除";
            }
        }
        this.userPetHouseDao.deleteAll(userPetHouseList);
        return Result.SUCCEED;
    }

}
