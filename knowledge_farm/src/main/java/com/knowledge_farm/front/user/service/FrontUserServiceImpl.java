package com.knowledge_farm.front.user.service;

import com.knowledge_farm.entity.*;
import com.knowledge_farm.pet.service.PetService;
import com.knowledge_farm.user.dao.UserDao;
import com.knowledge_farm.util.RateRandomNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Date;
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
    private PetService petService;
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
    public void deleteOneUser(Integer userId, Integer exist) {
        User user = this.userDao.findUserById(userId);
        UserAuthority userAuthority = user.getUserAuthority();
        user.setExist(exist);
        if (userAuthority != null) {
            userAuthority.setExist(exist);
        }
    }

    @Transactional(readOnly = false)
    public void editStatusListByIdList(List<Integer> idList, Integer exist) {
        List<User> users = this.userDao.findAllById(idList);
        for (User user : users) {
            UserAuthority userAuthority = user.getUserAuthority();
            user.setExist(exist);
            if (userAuthority != null) {
                userAuthority.setExist(exist);
            }
        }
        this.userDao.saveAll(users);
    }

    @Transactional(readOnly = false)
    public void deleteThoroughUser(Integer userId) {
        User user = this.userDao.findUserById(userId);
        this.userDao.delete(user);
    }

    @Transactional(readOnly = false)
    public String addUser(String nickName, String password, String email, Integer grade) {
        //生成账号
        String account = "";
        do {
            account = "";
            account = RateRandomNumber.generateAccount();
        } while (this.userDao.findUserByAccount(account) != null || account.charAt(0) == '0' || account.charAt(account.length() - 1) == '0');
        //构建User
        User user = new User();
        user.setAccount(account);
        user.setNickName(nickName);
        user.setPassword(password);
        user.setPhoto(this.userPhotoFolderName + "/" + this.userDefaultFileName);
        user.setEmail(email);
        user.setGrade(grade);
        user.setLastReadTime(new Date());
        user.setRegisterTime(new Date());
        user.setTask(new com.knowledge_farm.entity.Task(user));
        //构建Land
        Land land = new Land();
        //构建UserCrop
        UserCrop userCrop1 = new UserCrop();
        UserCrop userCrop2 = new UserCrop();
        UserCrop userCrop3 = new UserCrop();
        UserCrop userCrop4 = new UserCrop();
        //宠物仓库
        UserPetHouse petHouse = new UserPetHouse(user,petService.findPetById(1));
        petHouse.setIfUsing(1);
        //关联
        land.setUser(user);
        land.setUserCrop1(userCrop1);
        land.setUserCrop2(userCrop2);
        land.setUserCrop3(userCrop3);
        land.setUserCrop4(userCrop4);
        user.setLand(land);
        user.getPetHouses().add(petHouse);
        this.userDao.save(user);
        entityManager.clear();
        return account;
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
    public void save(User user) {
        this.userDao.save(user);
    }

    @Transactional(readOnly = false)
    public void editPasswordByAccount(String account, String password) {
        User user = this.userDao.findUserByAccount(account);
        user.setPassword(password);
    }

}
