package com.knowledge_farm.front.crop.service;

import com.knowledge_farm.crop.dao.CropDao;
import com.knowledge_farm.entity.Crop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
    public String updateExist(Integer id, Integer exist){
        Crop crop = this.cropDao.findCropById(id);
        try {
            crop.setExist(exist);
            return "succeed";
        }catch (NullPointerException e){
            return null;
        }catch (Exception e){
            return "fail";
        }
    }

    @Transactional(readOnly = false)
    public String deleteCropById(Integer id){
        Crop crop = this.cropDao.findCropById(id);
        try {
            this.cropDao.delete(crop);
            return "succeed";
        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            return "fail";
        }
    }

    @Transactional(readOnly = false)
    public Crop save(Crop crop){
        try {
            return this.cropDao.save(crop);
        }catch (Exception e){
            return null;
        }
    }

    public Crop findCropById(Integer id){
        return this.cropDao.findCropById(id);
    }

}
