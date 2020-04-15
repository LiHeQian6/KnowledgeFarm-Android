package com.atguigu.farm.crop.dao;

import com.atguigu.farm.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
