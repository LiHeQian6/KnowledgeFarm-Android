package com.knowledge_farm.userbag.dao;

import com.knowledge_farm.entity.UserBag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName UserBagDao
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 13:51
 */
public interface UserBagDao extends JpaRepository<UserBag, Integer> {
}