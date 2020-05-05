package com.knowledge_farm.pet_util.dao;

import com.knowledge_farm.entity.PetUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName PetFoodDao
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 10:01
 */
public interface PetUtilDao extends JpaRepository<PetUtil, Integer> {
    public List<PetUtil> findAllByExist(Integer exist);
    public PetUtil findPetUtilById(Integer id);
    @Query("select p from PetUtil p where p.name = ?1 and p.petUtilType.id = ?2 and p.exist = ?3")
    public Page<PetUtil> findPetUtilByNameAndPetUtilTypeAndExist(String name, Integer petUtilType, Integer exist, Pageable pageable);
    @Query("select p from PetUtil p where p.name = ?1 and p.exist = ?2")
    public Page<PetUtil> findPetUtilByNameAndExist(String name, Integer exist, Pageable pageable);
    @Query("select p from PetUtil p where p.petUtilType.id = ?1 and p.exist = ?2")
    public Page<PetUtil> findPetUtilByPetUtilTypeAndExist(Integer petUtilType, Integer exist, Pageable pageable);
    @Query("select p from PetUtil p where p.exist = ?1")
    public Page<PetUtil> findPetUtilByExist(Integer exist, Pageable pageable);
}
