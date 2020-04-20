package com.knowledge_farm.user.service;

import com.knowledge_farm.crop.service.CropServiceImpl;
import com.knowledge_farm.entity.*;
import com.knowledge_farm.user.dao.UserDao;
import com.knowledge_farm.userauthority.service.UserAuthorityServiceImpl;
import com.knowledge_farm.userbag.service.UserBagServiceImpl;
import com.knowledge_farm.usercrop.service.UserCropServiceImpl;
import com.knowledge_farm.util.Email;
import com.knowledge_farm.util.RandomUtil;
import com.knowledge_farm.util.UserCropGrowJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Set;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author 张帅华
 * @Date 2020-04-07 17:36
 */
@Service
@Transactional(readOnly = true)
@PropertySource(value = {"classpath:photo.properties"})
public class UserServiceImpl {
    @Resource
    private UserDao userDao;
    @Resource
    private UserAuthorityServiceImpl userAuthorityService;
    @Resource
    private UserCropServiceImpl userCropServiceImpl;
    @Resource
    private CropServiceImpl cropService;
    @Resource
    private UserBagServiceImpl userBagService;
    @Resource
    private EntityManager entityManager;
    @Resource
    private Scheduler scheduler;
    @Value("${file.userPhotoFolderName}")
    private String userPhotoFolderName;
    @Value("${file.userDefaultFileName}")
    private String userDefaultFileName;
    @Value("${level.experienceList}")
    private Integer experienceList[];

    /**
     * @Author 张帅华
     * @Description QQ登录 根据openId 
     * @Date 16:57 2020/4/8 0008
     * @Param [openId]
     * @return java.lang.String
     **/
    public Object loginByOpenId(String openId){
        if(this.userAuthorityService.findUserAuthoritiesByOpenIdAndExist(openId, null) != null){
            UserAuthority userAuthority = this.userAuthorityService.findUserAuthoritiesByOpenIdAndExist(openId, 1);
            if(userAuthority != null){
                User user = userAuthority.getUser();
                if(user != null){
                    return user;
                }
                return "{}";
            }
            return "notEffect";
        }
        return "notExist";
    }

    /**
     * @Author 张帅华
     * @Description 第一次登录QQ，添加用户
     * @Date 17:05 2020/4/8 0008
     * @Param [openId, nickName, grade, email, password]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public Object addQQUser(String openId, String nickName, Integer grade, String email, String password){
        //生成账号
        String account = "";
        do{
            account = "";
            account = RandomUtil.generateAccount();
        }while (this.userDao.findUserByAccount(account) != null || account.charAt(0) == '0' || account.charAt(account.length()-1) == '0');
        //构建User
        User user = new User();
        user.setAccount(account);
        user.setNickName(nickName);
        user.setPassword(password);
        user.setPhoto(this.userPhotoFolderName + "/" + this.userDefaultFileName);
        user.setEmail(email);
        user.setGrade(grade);
        //构建UserAuthority
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setOpenId(openId);
        userAuthority.setType("QQ");
        //构建Land
        Land land = new Land();
        //构建UserCrop
        UserCrop userCrop1 = new UserCrop();
        UserCrop userCrop2 = new UserCrop();
        UserCrop userCrop3 = new UserCrop();
        UserCrop userCrop4 = new UserCrop();
        //关联
        user.setUserAuthority(userAuthority);
        userAuthority.setUser(user);
        land.setUser(user);
        land.setUserCrop1(userCrop1);
        land.setUserCrop2(userCrop2);
        land.setUserCrop3(userCrop3);
        land.setUserCrop4(userCrop4);
        user.setLand(land);
        //添加并返回新插入的User
        try {
            this.userDao.save(user);
            entityManager.clear();
            User resultUser = this.userDao.findUserById(user.getId());
            if(resultUser != null){
                return resultUser;
            }
            return "{}";
        }catch (Exception e){
            return "fail";
        }
    }

    /**
     * @Author 张帅华
     * @Description 账号登录
     * @Date 17:09 2020/4/8 0008
     * @Param [account, password]
     * @return java.lang.String
     **/
    public Object loginByAccount(String account, String password){
        if(this.userDao.findUserByAccount(account) != null){
            if(this.userDao.findUserByAccountAndExist(account ,1) != null){
                User user = this.userDao.findUserByAccountAndPassword(account, password);
                if(user != null){
                    return user;
                }
                return "PasswordError";
            }
            return "notEffect";
        }
        return "notExist";
    }

    /**
     * @Author 张帅华
     * @Description 注册账号
     * @Date 17:11 2020/4/8 0008
     * @Param [nickName, grade, email, password]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public Object registAccount(String nickName, Integer grade, String email, String password){
        //生成账号
        String account = "";
        do{
            account = "";
            account = RandomUtil.generateAccount();
        }while (this.userDao.findUserByAccount(account) != null || account.charAt(0) == '0' || account.charAt(account.length()-1) == '0');
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
            User resultUser = this.userDao.findUserById(user.getId());
            if(resultUser != null){
                return resultUser;
            }
            return "{}";
        }catch (Exception e){
            return "fail";
        }
    }

    /**
     * @Author 张帅华
     * @Description 发送验证码用于找回密码，并返回验证码
     * @Date 17:15 2020/4/8 0008
     * @Param [account, email]
     * @return java.lang.String
     **/
    public String sendTestCodePassword(String account, String email){
        User user = this.userDao.findUserByAccount(account);
        if(user != null){
            String userEmail = user.getEmail();
            if(!userEmail.equals("")){
                if(email.equals(userEmail)){
                    if(Email.findPasswordByMail(email)){
                        return Email.getCode();
                    }
                    return "fail";
                }
                return "EmailError";
            }
            return "notBindingEmail";
        }
        return "notExistAccount";
    }

    /**
     * @Author 张帅华
     * @Description 验证是否已经绑定QQ
     * @Date 17:54 2020/4/8 0008
     * @Param [account]
     * @return java.lang.String
     **/
    public String isBindingQQ(String account){
        User user = this.userDao.findUserByAccount(account);
        if(user != null){
            if(user.getUserAuthority() != null){
                return "true";
            }
            return "false";
        }
        return "notExist";
    }

    /**
     * @Author 张帅华
     * @Description 账号绑定QQ
     * @Date 17:22 2020/4/8 0008
     * @Param [account, openId]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public String bindingQQ(String account, String openId){
        UserAuthority userAuthority = this.userAuthorityService.findUserAuthoritiesByOpenIdAndExist(openId, null);
        if(userAuthority != null){
            if(userAuthority.getUser() != null){
                return "already";
            }
        }else {
            userAuthority = new UserAuthority();
            userAuthority.setOpenId(openId);
            userAuthority.setType("QQ");
        }
        User user = this.userDao.findUserByAccount(account);
        if(user != null){
            user.setUserAuthority(userAuthority);
            userAuthority.setUser(user);
            try {
                return "true";
            }catch (Exception e){
                return "false";
            }
        }
        return "notExist";
    }

    /**
     * @Author 张帅华
     * @Description 账号解绑QQ
     * @Date 17:24 2020/4/8 0008
     * @Param [account]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public String removeUserAuthority(String account){
        User user = this.userDao.findUserByAccount(account);
        if(user != null){
            UserAuthority userAuthority = user.getUserAuthority();
            user.setUserAuthority(null);
            if(userAuthority != null){
                userAuthority.setUser(null);
                try {
                    return "true";
                }catch (Exception e){
                    return "false";
                }
            }
            return "notBinding";
        }
        return "notExist";
    }

    /**
     * @Author 张帅华
     * @Description 添加浇水，施肥次数，减少奖励次数
     * @Date 20:22 2020/4/8 0008
     * @Param [userId, water, fertilizer, subject]
     * @return int
     **/
    @Transactional(readOnly = false)
    public int lessRewardCount(Integer userId, Integer water, Integer fertilizer, String subject){
        User user = this.userDao.findUserById(userId);
        if(user != null){
            int rewardCount = 0;
            int flag = 0;
            switch (subject){
                case "chinese":
                    rewardCount = user.getChineseRewardCount();
                    if(rewardCount >= 1){
                        rewardCount = rewardCount - 1;
                        user.setChineseRewardCount(rewardCount);
                        flag = 1;
                    }
                    break;
                case "english":
                    rewardCount = user.getEnglishRewardCount();
                    if(rewardCount >= 1){
                        rewardCount = rewardCount - 1;
                        user.setEnglishRewardCount(rewardCount);
                        flag = 1;
                    }
                    break;
                case "math":
                    rewardCount = user.getMathRewardCount();
                    if(rewardCount >= 1){
                        rewardCount = rewardCount - 1;
                        user.setMathRewardCount(rewardCount);
                        flag = 1;
                    }
                    break;
                default:
                    return -2;
            }
            if(flag == 1){
                user.setWater(user.getWater() + water);
                user.setFertilizer(user.getFertilizer() + fertilizer);
                try {
                    return rewardCount;
                }catch (Exception e){
                    return -1;
                }
            }
            return 0;
        }
        return -3;
    }

    /**
     * @Author 张帅华
     * @Description 查询用户信息 根据id
     * @Date 21:44 2020/4/8 0008
     * @Param [id]
     * @return com.atguigu.farm.entity.User
     **/
    public User findUserById(Integer id){
        return this.userDao.findUserById(id);
    }

    /**
     * @Author 张帅华
     * @Description 设置密码
     * @Date 21:44 2020/4/8 0008
     * @Param [account, password]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public User editPasswordByAccount(String account, String password){
        User user = this.userDao.findUserByAccount(account);
        if(user != null){
            user.setPassword(password);
            return user;
        }
        return null;
    }

    /**
     * @Author 张帅华
     * @Description 修改用户昵称
     * @Date 21:45 2020/4/8 0008
     * @Param [account, nickName]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public User editNickNameByAccount(String account, String nickName){
        User user = this.userDao.findUserByAccount(account);
        if(user != null){
            user.setNickName(nickName);
            return user;
        }
        return null;
    }

    /**
     * @Author 张帅华
     * @Description 修改用户年级
     * @Date 21:45 2020/4/8 0008
     * @Param [account, grade]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public User editGradeByAccount(String account, Integer grade){
        User user = this.userDao.findUserByAccount(account);
        if(user != null){
            user.setGrade(grade);
            return user;
        }
        return null;
    }

    /**
     * @Author 张帅华
     * @Description 修改用户头像
     * @Date 21:45 2020/4/8 0008
     * @Param [id, photo]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public User editPhotoById(Integer id, String photo){
        User user = this.userDao.findUserById(id);
        if(user != null){
            user.setPhoto(photo);
            return user;
        }
        return null;
    }

    /**
     * @Author 张帅华
     * @Description 修改用户邮箱
     * @Date 21:47 2020/4/8 0008
     * @Param [account, email]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public User editEmail(String account, String email){
        User user = this.userDao.findUserByAccount(account);
        if(user != null){
            user.setEmail(email);
        }
        return null;
    }

    /**
     * @Author 张帅华
     * @Description 浇水
     * @Date 22:34 2020/4/8 0008
     * @Param [userId, landNumber]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public String waterCrop(Integer userId, String landNumber){
        User user = this.userDao.findUserById(userId);
        int flag = 0;
        if(user != null){ //用户存在
            Land land = user.getLand();
            UserCrop userCrop = findUserCropByLand(land, landNumber);
            if(userCrop != null){ //土地已开垦
                Crop crop = userCrop.getCrop();
                if(crop != null){ //土地已种植作物
                    //修改剩余水的次数
                    if(user.getWater() > 0){
                        //修改作物进度
                        int progress = userCrop.getProgress();
                        int matureTime = crop.getMatureTime();
                        if(progress < matureTime){
                            if(progress+5 >= matureTime){
                                userCrop.setProgress(crop.getMatureTime());
                            }else{
                                userCrop.setProgress(progress + 5);
                            }
                        }else{
                            return "false";
                        }
                        user.setWater(user.getWater() - 1);
                    }else{
                        return "false";
                    }
                    //修改作物干枯湿润状态
                    if(userCrop.getStatus() == 0){
                        userCrop.setStatus(1);
                        flag = 1;
                    }
                    //保存修改
                    try {
                        if(flag == 1){
                            this.startJob(scheduler, user.getId(), userCrop.getId());
                        }
                        return "true";
                    }catch (Exception e){
                        return "false";
                    }
                }
            }
        }
        return "notExist";
    }

    /**
     * @Author 张帅华
     * @Description 施肥
     * @Date 19:09 2020/4/9 0009
     * @Param [userId, landNumber]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public String fertilizerCrop(Integer userId, String landNumber){
        User user = this.userDao.findUserById(userId);
        if(user != null){ //用户存在
            Land land = user.getLand();
            UserCrop userCrop = findUserCropByLand(land, landNumber);
            if(userCrop != null){ //土地已开垦
                Crop crop = userCrop.getCrop();
                if(crop != null){ //土地已种植作物
                    if(userCrop.getStatus() != 0){
                        //修改剩余化肥的次数
                        if(user.getFertilizer() > 0){
                            //修改作物进度
                            int progress = userCrop.getProgress();
                            int matureTime = crop.getMatureTime();
                            if(progress < matureTime) {
                                if (progress + 10 >= matureTime) {
                                    userCrop.setProgress(crop.getMatureTime());
                                } else {
                                    userCrop.setProgress(progress + 10);
                                }
                            }else{
                                return "false";
                            }
                            user.setFertilizer(user.getFertilizer() - 1);
                        }else{
                            return "false";
                        }
                        //保存修改
                        try {
                            return "true";
                        }catch (Exception e){
                            return "false";
                        }
                    }
                    return "false";
                }
            }
        }
        return "notExist";
    }

    /**
     * @Author 张帅华
     * @Description 购买种子后添加到背包
     * @Date 9:42 2020/4/10 0010
     * @Param [userId, cropId, number]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public String buyCrop(Integer userId, Integer cropId, Integer number){
        User user = this.userDao.findUserById(userId);
        Set<UserBag> userBags = user.getUserBags();
        Crop crop = this.cropService.findCropById(cropId);
        Integer userMoney = user.getMoney();
        Integer needMoney = crop.getPrice() * number;
        int flag = 0;
        if(userMoney >= needMoney){
            for(UserBag userBag : userBags){
                if(userBag.getCrop() == crop){
                    userBag.setNumber(userBag.getNumber() + number);
                    flag = 1;
                    break;
                }
            }
            if(flag != 1){
                UserBag userBag = new UserBag();
                userBag.setCrop(crop);
                userBag.setNumber(number);
                userBags.add(userBag);
            }
            try {
                user.setMoney(userMoney - needMoney);
                return "true";
            }catch (Exception e){
                return "false";
            }
        }
        return "notEnoughMoney";
    }

    /**
     * @Author 张帅华
     * @Description 种植作物
     * @Date 10:11 2020/4/10 0010
     * @Param [userId, cropId, landNumber]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public String raiseCrop(Integer userId, Integer cropId, String landNumber){
        User user = this.userDao.findUserById(userId);
        Crop crop = this.cropService.findCropById(cropId);
        Set<UserBag> userBags = user.getUserBags();
        int flag = 0;
        int number = 0;
        try {
            for(UserBag userBag : userBags){
                if(userBag.getCrop() == crop){
                    flag = 1;
                    number = userBag.getNumber();
                    userBag.setNumber(number - 1);
                    if((number - 1) <= 0){
                        userBags.remove(userBag);
                        this.userBagService.deleteById(userBag.getId());
                    }
                    break;
                }
            }
            if(flag == 1){
                Land land = user.getLand();
                UserCrop userCrop = findUserCropByLand(land, landNumber);
                userCrop.setCrop(crop);
                startJob(scheduler, userId, userCrop.getId());
                return "true";
            }
        }catch (Exception e){
            return "false";
        }
        return "false";
    }

    /**
     * @Author 张帅华
     * @Description 收获
     * @Date 11:03 2020/4/10 0010
     * @Param [userId, landNumber]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public String harvest(Integer userId, String landNumber){
        User user = this.userDao.findUserById(userId);
        Land land = user.getLand();
        UserCrop userCrop = findUserCropByLand(land, landNumber);
        Crop crop = userCrop.getCrop();
        userCrop.setCrop(null);
        userCrop.setProgress(0);
        userCrop.setWaterLimit(0);
        userCrop.setFertilizerLimit(0);
        userCrop.setStatus(1);
        int value = crop.getValue();
        int experience = crop.getExperience();
        int userMoney = user.getMoney();
        int userLevel = user.getLevel();
        int userExperience = user.getExperience();
        int isLevel = 0;
        if(userLevel < experienceList.length-1){
            if(userExperience + experience >= experienceList[userLevel]){
                user.setLevel(userLevel + 1);
                isLevel = 1;
            }
            user.setExperience(userExperience + experience);
        }else{
            if(userExperience + experience <= experienceList[userLevel]){
                user.setExperience(userExperience + experience);
            }else{
                user.setExperience(experienceList[userLevel]);
            }
        }
        user.setMoney(userMoney + value);
        try {
            if(isLevel == 1){
                return "up";
            }
            return "true";
        }catch (Exception e){
            return "false";
        }
    }

    /**
     * @Author 张帅华
     * @Description 扩建土地
     * @Date 12:01 2020/4/10 0010
     * @Param [userId, landNumber, needMoney]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public String extensionLand(Integer userId, String landNumber, Integer needMoney){
        User user = this.userDao.findUserById(userId);
        Land land = user.getLand();
        int userMoney = user.getMoney();
        if(userMoney >= needMoney){
            addUserCrop(land, new UserCrop(), landNumber);
            user.setMoney(userMoney - needMoney);
            try {
                return "true";
            }catch (Exception e){
                return "false";
            }
        }
        return "notEnoughMoney";
    }

    public Page<User> findAllUserByAccount(String account, Integer pageNumber, Integer pageSize){
        if(account != null && !account.equals("")){
            return this.userDao.findAllUserByAccount(account, PageRequest.of(pageNumber - 1, pageSize));
        }
        return this.userDao.findAllUser(PageRequest.of(pageNumber - 1, pageSize));
    }

    /**
     * @Author 张帅华
     * @Description 重置奖励次数
     * @Date 16:09 2020/4/9 0009
     * @Param []
     * @return int
     **/
    @Transactional(readOnly = false)
    public int updateUserRewardCount(){
        return this.userDao.updateUserRewardCount();
    }

    public User findUserByEmail(String email){
        return this.userDao.findUserByEmail(email);
    }

    public User findUserByAccount(String account){
        return this.userDao.findUserByAccount(account);
    }

    public void saveUser(User user){
        this.userDao.save(user);
    }

    /**
     * @Author 张帅华
     * @Description 根据landNumber得到对应土地的种植信息对象
     * @Date 23:30 2020/4/10 0010
     * @Param [land, landNumber]
     * @return com.atguigu.farm.entity.UserCrop
     **/
    public UserCrop findUserCropByLand(Land land, String landNumber){
        Integer realLand = Integer.parseInt(landNumber.substring(4));
        switch (realLand){
            case 1:
                return land.getUserCrop1();
            case 2:
                return land.getUserCrop2();
            case 3:
                return land.getUserCrop3();
            case 4:
                return land.getUserCrop4();
            case 5:
                return land.getUserCrop5();
            case 6:
                return land.getUserCrop6();
            case 7:
                return land.getUserCrop7();
            case 8:
                return land.getUserCrop8();
            case 9:
                return land.getUserCrop9();
            case 10:
                return land.getUserCrop10();
            case 11:
                return land.getUserCrop11();
            case 12:
                return land.getUserCrop12();
            case 13:
                return land.getUserCrop13();
            case 14:
                return land.getUserCrop14();
            case 15:
                return land.getUserCrop15();
            case 16:
                return land.getUserCrop16();
            case 17:
                return land.getUserCrop17();
            case 18:
                return land.getUserCrop18();
            default:
                return null;
        }
    }

    /**
     * @Author 张帅华
     * @Description 给指定landNumber的土地添加作物
     * @Date 23:30 2020/4/10 0010
     * @Param [land, userCrop, landNumber]
     * @return void
     **/
    public void addUserCrop(Land land, UserCrop userCrop, String landNumber){
        Integer realLand = Integer.parseInt(landNumber.substring(4));
        switch (realLand){
            case 1:
                land.setUserCrop1(userCrop);
            case 2:
                land.setUserCrop2(userCrop);
            case 3:
                land.setUserCrop3(userCrop);
            case 4:
                land.setUserCrop4(userCrop);
            case 5:
                land.setUserCrop5(userCrop);
            case 6:
                land.setUserCrop6(userCrop);
            case 7:
                land.setUserCrop7(userCrop);
            case 8:
                land.setUserCrop8(userCrop);
            case 9:
                land.setUserCrop9(userCrop);
            case 10:
                land.setUserCrop10(userCrop);
            case 11:
                land.setUserCrop11(userCrop);
            case 12:
                land.setUserCrop12(userCrop);
            case 13:
                land.setUserCrop13(userCrop);
            case 14:
                land.setUserCrop14(userCrop);
            case 15:
                land.setUserCrop15(userCrop);
            case 16:
                land.setUserCrop16(userCrop);
            case 17:
                land.setUserCrop17(userCrop);
            case 18:
                land.setUserCrop18(userCrop);
        }
    }

    /**
     * @Author 张帅华
     * @Description 开启自动生长、干旱湿润状态变换Job
     * @Date 23:31 2020/4/10 0010
     * @Param [scheduler, id]
     * @return void
     **/
    public void startJob(Scheduler scheduler, Integer userId, Integer userCropId) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        String name = "job" + userId;
        String group = "group" + userId;
        JobDetail jobDetail = JobBuilder.newJob(UserCropGrowJob.class).withIdentity(name, group).build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("userId", userId);
        jobDataMap.put("userCropId", userCropId);
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0/1 * * ? ");
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

}
