package com.knowledge_farm.pet_util_type.dao;

import com.knowledge_farm.entity.PetUtilType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName PetUtilType
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 18:01
 */
public interface PetUtilTypeDao extends JpaRepository<PetUtilType, Integer> {
    public PetUtilType findPetUtilTypeById(Integer id);
}
