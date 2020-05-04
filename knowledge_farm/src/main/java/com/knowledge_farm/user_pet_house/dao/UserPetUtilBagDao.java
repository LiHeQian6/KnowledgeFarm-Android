package com.knowledge_farm.user_pet_house.dao;

import com.knowledge_farm.entity.UserPetUtilBag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName UserPetFoodDao
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 11:20
 */
public interface UserPetUtilBagDao extends JpaRepository<UserPetUtilBag, Integer> {
    public UserPetUtilBag findUserPetUtilBagById(Integer id);
}
