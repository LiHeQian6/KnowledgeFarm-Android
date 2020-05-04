package com.knowledge_farm.pet_util.dao;

import com.knowledge_farm.entity.PetUtil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassName PetFoodDao
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 10:01
 */
public interface PetUtilDao extends JpaRepository<PetUtil, Integer> {
    public List<PetUtil> findAllByExist(Integer exist);
    public PetUtil findPetFoodById(Integer id);
}
