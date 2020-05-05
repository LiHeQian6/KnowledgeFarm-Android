package com.knowledge_farm.user_bag.dao;

import com.knowledge_farm.entity.UserCropBag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName UserBagDao
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 13:51
 */
public interface UserBagDao extends JpaRepository<UserCropBag, Integer> {
}
