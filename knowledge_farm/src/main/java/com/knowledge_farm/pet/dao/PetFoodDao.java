package com.knowledge_farm.pet.dao;

import com.knowledge_farm.entity.PetFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface PetFoodDao extends JpaRepository<PetFood,Integer>, JpaSpecificationExecutor<PetFood>, Serializable {

}
