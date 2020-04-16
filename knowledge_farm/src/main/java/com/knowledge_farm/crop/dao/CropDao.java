package com.knowledge_farm.crop.dao;

import com.knowledge_farm.entity.Crop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName CropDao
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 09:25
 */
public interface CropDao extends JpaRepository<Crop,Integer> {
    public Crop findCropById(Integer id);

    public List<Crop> findCropsByExist(Integer exist);

    @Query("select c from Crop c where c.exist = ?1")
    public Page<Crop> findAllCropByExist(Integer exist, Pageable pageable);

    @Query("select c from Crop c where c.name = ?1 and c.exist = ?2")
    public Page<Crop> findAllCropByNameAndExist(String name, Integer exist, Pageable pageable);
}
