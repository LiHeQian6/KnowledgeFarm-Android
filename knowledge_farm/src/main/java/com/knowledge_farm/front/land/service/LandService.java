package com.knowledge_farm.front.land.service;

import com.knowledge_farm.entity.Land;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.front.land.dao.LandDao;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.util.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @ClassName LandService
 * @Description
 * @Author 张帅华
 * @Date 2020-04-19 08:42
 */
@Service
@Transactional(readOnly = true)
public class LandService {
    @Resource
    private LandDao landDao;
    @Resource
    private UserServiceImpl userService;

    public Object findAllLandByAccount(String account, Integer pageNumber, Integer pageSize){
        if(account != null && !account.equals("")){
            User user = this.userService.findUserByAccount(account);
            try {
                return this.landDao.findALLByUser(user.getId(), PageRequest.of(pageNumber - 1, pageSize));
            }catch (NullPointerException e){
                PageUtil<Land> pageUtil = new PageUtil(pageNumber, pageSize);
                pageUtil.setTotalCount(0);
                pageUtil.setList(new ArrayList());
                return pageUtil;
            }
        }
        return this.landDao.findAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    public Land findLandById(Integer id){
        return this.landDao.findLandById(id);
    }

}
