package com.knowledge_farm.pet.dao;

import com.knowledge_farm.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface PetDao extends JpaRepository<Pet,Integer>, JpaSpecificationExecutor<Pet>, Serializable {
    Page<Pet> findAllByExist(int exist, Pageable pageable);

    Pet findPetById(int petId);
}
