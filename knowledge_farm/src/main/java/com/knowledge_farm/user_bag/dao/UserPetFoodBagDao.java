package com.knowledge_farm.user_bag.dao;

import com.knowledge_farm.entity.UserPetFoodBag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName UserPetFoodDao
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 11:20
 */
public interface UserPetFoodBagDao extends JpaRepository<UserPetFoodBag, Integer> {
}
