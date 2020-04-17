package com.knowledge_farm.usercrop.dao;

import com.knowledge_farm.entity.UserCrop;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName UserCrop
 * @Description
 * @Author 张帅华
 * @Date 2020-04-08 22:20
 */
public interface UserCropDao extends JpaRepository<UserCrop, Integer> {
    public UserCrop findUserCropById(Integer id);
}
