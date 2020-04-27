package com.knowledge_farm.user_pet_house.dao;

import com.knowledge_farm.entity.UserPetHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface UserPetHouseDao extends JpaRepository<UserPetHouse,Integer>, JpaSpecificationExecutor<UserPetHouse>, Serializable {

}
