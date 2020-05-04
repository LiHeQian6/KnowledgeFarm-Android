package com.knowledge_farm.pet_food.dao;

import com.knowledge_farm.entity.PetFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassName PetFoodDao
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 10:01
 */
public interface PetFoodDao extends JpaRepository<PetFood, Integer> {
    public List<PetFood> findAllByExist(Integer exist);
    public PetFood findPetFoodById(Integer id);
}
