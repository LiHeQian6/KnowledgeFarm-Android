package com.knowledge_farm.userauthority.dao;

import com.knowledge_farm.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName UserAuthorityDao
 * @Description
 * @Author 张帅华
 * @Date 2020-04-07 18:17
 */
public interface UserAuthorityDao extends JpaRepository<UserAuthority, Integer> {
    public UserAuthority findUserAuthoritiesByOpenId(String openId);

    public UserAuthority findUserAuthoritiesByOpenIdAndExist(String openId, Integer exist);
}
