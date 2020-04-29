package com.knowledge_farm.user_tag.dao;

import com.knowledge_farm.entity.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName UserTagDao
 * @Description
 * @Author 张帅华
 * @Date 2020-04-29 16:18
 */
public interface UserTagDao extends JpaRepository<UserTag, Integer> {
    public UserTag findUserTagById(Integer id);
}
