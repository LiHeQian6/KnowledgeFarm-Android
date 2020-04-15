package com.knowledge_farm.front.crop.service;

import com.knowledge_farm.crop.dao.CropDao;
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
public class CropService {
    @Resource
    private CropDao cropDao;

}
