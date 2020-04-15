package com.atguigu.farm.userauthority.service;

import com.atguigu.farm.entity.UserAuthority;
import com.atguigu.farm.userauthority.dao.UserAuthorityDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName UserAuthorityServiceImpl
 * @Description
 * @Author 张帅华
 * @Date 2020-04-07 18:17
 */
@Service
@Transactional(readOnly = true)
public class UserAuthorityServiceImpl {
    @Resource
    private UserAuthorityDao userAuthorityDao;

    /**
     * @Author 张帅华
     * @Description 根据openId和账号状态查询
     * @Date 18:25 2020/4/7 0007
     * @Param [openId, exist]
     * @return com.atguigu.farm.entity.UserAuthority
     **/
    public UserAuthority findUserAuthoritiesByOpenIdAndExist(String openId, Integer exist){
        if(exist != null){
            return this.userAuthorityDao.findUserAuthoritiesByOpenIdAndExist(openId, exist);
        }
        return this.userAuthorityDao.findUserAuthoritiesByOpenId(openId);
    }

}
