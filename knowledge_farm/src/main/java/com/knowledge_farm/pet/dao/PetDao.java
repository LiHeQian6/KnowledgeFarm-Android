package com.knowledge_farm.pet.dao;

import com.knowledge_farm.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface PetDao extends JpaRepository<Pet,Integer>, JpaSpecificationExecutor<Pet>, Serializable {
    @Query("select p from Pet p where p.exist = ?1")
    public Page<Pet> findAllPetByExist(Integer exist, Pageable pageable);

    @Query("select p from Pet p where p.name = ?1 and p.exist = ?2")
    public Page<Pet> findAllPetByNameAndExist(String name, Integer exist, Pageable pageable);

    List<Pet> findAllByExist(int exist);

    Pet findPetById(int petId);
}
