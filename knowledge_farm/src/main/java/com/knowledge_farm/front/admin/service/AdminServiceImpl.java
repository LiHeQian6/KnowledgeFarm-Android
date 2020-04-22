package com.knowledge_farm.front.admin.service;

import com.knowledge_farm.entity.Admin;
import com.knowledge_farm.entity.Result;
import com.knowledge_farm.front.admin.dao.AdminDao;
import com.knowledge_farm.util.Md5Encode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ClassName AdminServiceImpl
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 15:45
 */
@Service
@Transactional(readOnly = true)
public class AdminServiceImpl {

    @Resource
    private AdminDao adminDao;

    /**
     * @Author 张帅华
     * @Description 登录
     * @Date 21:37 2020/4/8 0008
     * @Param [account, password, session]
     * @return java.lang.String
     **/
    @Transactional(readOnly = true)
    public Object login(String account, String password){
        password = Md5Encode.getMD5(password.getBytes());
        if(this.adminDao.findAdminByAccountAndExist(account, 0) == null){
            Admin admin = this.adminDao.findAdminByAccountAndPassword(account, password);
            if(admin != null){
                return admin;
            }
            return Result.FAIL;
        }
        return Result.NOT_EFFECT;
    }

    /**
     * @Author 张帅华
     * @Description 添加管理员
     * @Date 17:00 2020/4/6 0006
     * @Param [admin]
     * @return com.atguigu.farm.entity.Admin
     **/
    @Transactional(readOnly = false)
    public String add(Admin admin){
        try {
            this.adminDao.save(admin);
            return Result.SUCCEED;
        }catch (Exception e){
            return Result.FAIL;
        }
    }

    /**
     * @Author 张帅华
     * @Description 删除管理员
     * @Date 17:00 2020/4/6 0006
     * @Param [id]
     * @return void
     **/
    @Transactional(readOnly = false)
    public String deleteById(Integer id){
        try {
            this.adminDao.deleteById(id);
            return Result.SUCCEED;
        }catch (Exception e){
            return Result.FAIL;
        }

    }
    
    /**
     * @Author 张帅华
     * @Description 修改管理员账号
     * @Date 17:17 2020/4/6 0006
     * @Param [id, accout]
     * @return com.atguigu.farm.entity.Admin
     **/
    @Transactional(readOnly = false)
    public String editAccountById(Integer id, String account){
        Admin admin = this.adminDao.findAdminById(id);
        try {
            admin.setAccount(account);
            return Result.SUCCEED;
        }catch (Exception e){
            return Result.FAIL;
        }
    }

    /**
     * @Author 张帅华
     * @Description 修改管理员密码
     * @Date 17:26 2020/4/6 0006
     * @Param [id, oldPassword, newPassword]
     * @return int
     **/
    @Transactional(readOnly = false)
    public String editPasswordById(Integer id, String password){
        Admin admin = this.adminDao.findAdminById(id);
        try {
            admin.setPassword(password);
            return Result.SUCCEED;
        }catch (Exception e){
            return Result.FAIL;
        }
    }

    /**
     * @Author 张帅华
     * @Description 修改管理员账号状态
     * @Date 18:34 2020/4/6 0006
     * @Param [id, exist]
     * @return com.atguigu.farm.entity.Admin
     **/
    @Transactional(readOnly = false)
    public String editStatusById(Integer id, Integer exist){
        Admin admin = this.adminDao.findAdminById(id);
        try {
            admin.setExist(exist);
            return Result.SUCCEED;
        }catch (Exception e){
            return Result.FAIL;
        }
    }

    /**
     * @Author 张帅华
     * @Description 批量修改管理员账号状态
     * @Date 21:40 2020/4/8 0008
     * @Param [idList, exist]
     * @return java.util.List<com.atguigu.farm.entity.Admin>
     **/
    @Transactional(readOnly = false)
    public String editStatusListByIdList(List<Integer> idList, Integer exist){
        List<Admin> admins = this.adminDao.findAllById(idList);
        try {
            for(Admin admin : admins){
                admin.setExist(exist);
            }
            this.adminDao.saveAll(admins);
            return Result.SUCCEED;
        }catch (Exception e){
            return Result.FAIL;
        }
    }

    /**
     * @Author 张帅华
     * @Description 获取管理员信息 根据id
     * @Date 21:43 2020/4/8 0008
     * @Param [id]
     * @return com.atguigu.farm.entity.Admin
     **/
    public Admin findById(Integer id){
        return this.adminDao.findAdminById(id);
    }

    /**
     * @Author 张帅华
     * @Description 根据账号、账号状态查询
     * @Date 17:47 2020/4/6 0006
     * @Param [account]
     * @return com.atguigu.farm.entity.Admin
     **/
    public Admin findByAccountAndExist(String account, Integer exist){
        if(exist != null){
            return this.adminDao.findAdminByAccountAndExist(account, exist);
        }
        return this.adminDao.findAdminByAccount(account);
    }

    public Admin findByAccountExcludeAccount(String account, String excludeAccount){
        return this.adminDao.findAdminByAccountAndExcludeAccount(account, excludeAccount);
    }

    /**
     * @Author 张帅华
     * @Description 分页查询管理员信息
     * @Date 17:56 2020/4/6 0006
     * @Param [account, exist, pageNumber, pageSize]
     * @return org.springframework.data.domain.Page<com.atguigu.farm.entity.Admin>
     **/
    public Page<Admin> findPageAdminByAccount(String account, Integer exist, Integer pageNumber, Integer pageSize){
        if(account != null && !account.equals("")){
            return this.adminDao.findPageAdminByAccount(account + "%", exist, PageRequest.of(pageNumber - 1, pageSize));
        }
        return this.adminDao.findPageAdmin(exist, PageRequest.of(pageNumber - 1, pageSize));
    }

}
