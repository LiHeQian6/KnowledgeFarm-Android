package com.knowledge_farm.user_tag.service;

import com.knowledge_farm.entity.UserTag;
import com.knowledge_farm.user_tag.dao.UserTagDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName UsreTagServiceImpl
 * @Description
 * @Author 张帅华
 * @Date 2020-04-29 16:18
 */
@Service
@Transactional(readOnly = true)
public class UserTagServiceImpl {
    @Resource
    private UserTagDao userTagDao;

    public UserTag findUserTagById(Integer id){
        return this.userTagDao.findUserTagById(id);
    }

}
