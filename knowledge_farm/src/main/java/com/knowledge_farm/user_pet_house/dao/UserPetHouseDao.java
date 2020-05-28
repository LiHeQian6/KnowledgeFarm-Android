package com.knowledge_farm.user_pet_house.dao;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserPetHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface UserPetHouseDao extends JpaRepository<UserPetHouse,Integer>, JpaSpecificationExecutor<UserPetHouse>, Serializable {
    UserPetHouse findUserPetHouseByUserAndPet(User user, Pet pet);
    UserPetHouse findUserPetHouseById(Integer id);

    @Query("select uph from UserPetHouse uph where uph.user.id = ?1 and uph.id = ?2")
    UserPetHouse findUserPetHouseByUserAndId(Integer userId, Integer id);

    Page<UserPetHouse> findAll(Specification<UserPetHouse> specification, Pageable pageable);
}
