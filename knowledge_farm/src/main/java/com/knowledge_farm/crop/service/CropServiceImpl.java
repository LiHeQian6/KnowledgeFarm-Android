package com.knowledge_farm.crop.service;

import com.knowledge_farm.crop.dao.CropDao;
import com.knowledge_farm.entity.Crop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CropService
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 09:22
 */
@Service
@Transactional(readOnly = true)
public class CropServiceImpl {
    @Resource
    private CropDao cropDao;

    public Crop findCropById(Integer id){
        return this.cropDao.findCropById(id);
    }

    public List<Crop> findAllCropByExist(Integer exist){
        return this.cropDao.findCropsByExist(exist);
    }

}
