package com.knowledge_farm.front.crop.service;

import com.knowledge_farm.crop.dao.CropDao;
import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.entity.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CropService
 * @Description
 * @Author 张帅华
 * @Date 2020-04-15 23:19
 */
@Service
@Transactional(readOnly = true)
public class FrontCropService {
    @Resource
    private CropDao cropDao;

    public Page<Crop> findAllCrop(String name, Integer exist, Integer pageNumber, Integer pageSize){
        if(name != null && !name.equals("")){
            return this.cropDao.findAllCropByNameAndExist(name, exist, PageRequest.of(pageNumber - 1, pageSize));
        }
        return this.cropDao.findAllCropByExist(exist, PageRequest.of(pageNumber - 1, pageSize));
    }

    @Transactional(readOnly = false)
    public void updateExist(Integer id, Integer exist){
        Crop crop = this.cropDao.findCropById(id);
        crop.setExist(exist);
    }

    @Transactional(readOnly = false)
    public void editStatusListByIdList(List<Integer> idList, Integer exist){
        List<Crop> crops = this.cropDao.findAllById(idList);
        for(Crop crop : crops){
            crop.setExist(exist);
        }
        this.cropDao.saveAll(crops);
    }

    @Transactional(readOnly = false)
    public void deleteCropById(Integer id){
        Crop crop = this.cropDao.findCropById(id);
        this.cropDao.delete(crop);
    }

    @Transactional(readOnly = false)
    public Crop save(Crop crop){
        return this.cropDao.save(crop);
    }

    public Crop findCropById(Integer id){
        return this.cropDao.findCropById(id);
    }

}
