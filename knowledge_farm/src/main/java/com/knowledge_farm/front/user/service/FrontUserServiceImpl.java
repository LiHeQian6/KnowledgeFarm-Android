package com.knowledge_farm.front.user.service;

import com.knowledge_farm.entity.*;
import com.knowledge_farm.user.dao.UserDao;
import com.knowledge_farm.util.RandomUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * @ClassName UserService
 * @Description
 * @Author 张帅华
 * @Date 2020-04-15 20:59
 */
@Service
@Transactional(readOnly = true)
public class FrontUserServiceImpl {
    @Resource
    private UserDao userDao;
    @Resource
    private EntityManager entityManager;
    @Value("${file.userPhotoFolderName}")
    private String userPhotoFolderName;
    @Value("${file.userDefaultFileName}")
    private String userDefaultFileName;

    public Page<User> findUserPage(String account, Integer exist, Integer pageNumber, Integer pageSize) {
        if (exist != null) {
            if (account != null && !account.equals("")) {
                return this.userDao.findAllUserByAccountAndExist(account, exist, PageRequest.of(pageNumber - 1, pageSize));
            }
            return this.userDao.findAllUserAndExist(exist, PageRequest.of(pageNumber - 1, pageSize));
        }
        if (account != null && !account.equals("")) {
            return this.userDao.findAllUserByAccount(account, PageRequest.of(pageNumber - 1, pageSize));
        }
        return this.userDao.findAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @Transactional(readOnly = false)
    public String deleteOneUser(Integer userId, Integer exist) {
        User user = this.userDao.findUserById(userId);
        try {
            UserAuthority userAuthority = user.getUserAuthority();
            user.setExist(exist);
            if (userAuthority != null) {
                userAuthority.setExist(exist);
            }
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

    @Transactional(readOnly = false)
    public String editStatusListByIdList(List<Integer> idList, Integer exist) {
        List<User> users = this.userDao.findAllById(idList);
        try {
            for (User user : users) {
                UserAuthority userAuthority = user.getUserAuthority();
                user.setExist(exist);
                if (userAuthority != null) {
                    userAuthority.setExist(exist);
                }
            }
            this.userDao.saveAll(users);
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

    @Transactional(readOnly = false)
    public String deleteThoroughUser(Integer userId) {
        User user = this.userDao.findUserById(userId);
        try {
            this.userDao.delete(user);
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

    @Transactional(readOnly = false)
    public String addUser(String nickName, String password, String email, Integer grade) {
        //生成账号
        String account = "";
        do {
            account = "";
            account = RandomUtil.generateAccount();
        } while (this.userDao.findUserByAccount(account) != null || account.charAt(0) == '0' || account.charAt(account.length() - 1) == '0');
        //构建User
        User user = new User();
        user.setAccount(account);
        user.setNickName(nickName);
        user.setPassword(password);
        user.setPhoto(this.userPhotoFolderName + "/" + this.userDefaultFileName);
        user.setEmail(email);
        user.setGrade(grade);
        //构建Land
        Land land = new Land();
        //构建UserCrop
        UserCrop userCrop1 = new UserCrop();
        UserCrop userCrop2 = new UserCrop();
        UserCrop userCrop3 = new UserCrop();
        UserCrop userCrop4 = new UserCrop();
        //关联
        land.setUser(user);
        land.setUserCrop1(userCrop1);
        land.setUserCrop2(userCrop2);
        land.setUserCrop3(userCrop3);
        land.setUserCrop4(userCrop4);
        user.setLand(land);
        try {
            this.userDao.save(user);
            entityManager.clear();
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

    public User findUserById(Integer id) {
        return this.userDao.findUserById(id);
    }

    public User findUserByAccount(String account) {
        return this.userDao.findUserByAccount(account);
    }

    public User findUserByAccountAndExcludeAccount(String account, String excludeAccount) {
        return this.userDao.findUserByAccountAndExcludeAccount(account, excludeAccount);
    }

    @Transactional(readOnly = false)
    public String save(User user) {
        try {
            this.userDao.save(user);
            return "succeed";
        } catch (Exception e) {
            return "fail";
        }
    }

    @Transactional(readOnly = false)
    public String editPasswordByAccount(String account, String password) {
        User user = this.userDao.findUserByAccount(account);
        try {
            user.setPassword(password);
            return Result.SUCCEED;
        } catch (Exception e) {
            return Result.FAIL;
        }
    }

}
