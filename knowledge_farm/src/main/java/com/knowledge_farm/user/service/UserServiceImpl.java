package com.knowledge_farm.user.service;

import com.knowledge_farm.annotation.Task;
import com.knowledge_farm.crop.service.CropServiceImpl;
import com.knowledge_farm.entity.*;
import com.knowledge_farm.pet.service.PetService;
import com.knowledge_farm.pet_util.service.PetUtilService;
import com.knowledge_farm.pet_util_type.service.PetUtilTypeService;
import com.knowledge_farm.user.dao.UserDao;
import com.knowledge_farm.user_authority.service.UserAuthorityServiceImpl;
import com.knowledge_farm.user_bag.service.UserBagServiceImpl;
import com.knowledge_farm.user_crop.service.UserCropServiceImpl;
import com.knowledge_farm.user_pet_house.service.UserPetHouseService;
import com.knowledge_farm.util.Email;
import com.knowledge_farm.util.RandomUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author 张帅华
 * @Date 2020-04-07 17:36
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl {
    @Resource
    private UserDao userDao;
    @Resource
    private UserAuthorityServiceImpl userAuthorityService;
    @Resource
    @Lazy
    private UserCropServiceImpl userCropService;
    @Resource
    private PetUtilService petUtilService;
    @Resource
    private CropServiceImpl cropService;
    @Resource
    @Lazy
    private UserBagServiceImpl userBagService;
    @Resource
    private UserPetHouseService userPetHouseService;
    @Resource
    @Lazy
    private PetService petService;
    @Resource
    private PetUtilTypeService petUtilTypeService;
    @Resource
    private EntityManager entityManager;
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
        if(this.userAuthorityService.findUserAuthorityByOpenIdAndExist(openId, null) != null){
            UserAuthority userAuthority = this.userAuthorityService.findUserAuthorityByOpenIdAndExist(openId, 1);
            if(userAuthority != null){
                User user = userAuthority.getUser();
                if(user != null){
                    return user;
                }
                return "{}";
            }
            return Result.NOT_EFFECT;
        }
        return Result.NOT_EXIST;
    }

    /**
     * @Author 张帅华
     * @Description 第一次登录QQ，添加用户
     * @Date 17:05 2020/4/8 0008
     * @Param [openId, nickName, grade, email, password]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public Object addQQUser(String openId, String photo, String nickName, Integer grade, String email, String password){
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
        user.setPhoto(photo);
        user.setEmail(email);
        user.setGrade(grade);
        user.setLastReadTime(new Date());
        user.setRegisterTime(new Date());
        user.setTask(new com.knowledge_farm.entity.Task(user));
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
        //宠物仓库
        UserPetHouse petHouse = new UserPetHouse(user,petService.findPetById(1));
        petHouse.setIfUsing(1);
        //关联
        user.setUserAuthority(userAuthority);
        userAuthority.setUser(user);
        land.setUser(user);
        land.setUserCrop1(userCrop1);
        land.setUserCrop2(userCrop2);
        land.setUserCrop3(userCrop3);
        land.setUserCrop4(userCrop4);
        user.setLand(land);
        user.getPetHouses().add(petHouse);
        //添加并返回新插入的User
        this.userDao.save(user);
        entityManager.clear();
        User resultUser = this.userDao.findUserById(user.getId());
        if(resultUser != null){
            return resultUser;
        }
        return "{}";
    }

    /**
     * @Author 张帅华
     * @Description 账号登录
     * @Date 17:09 2020/4/8 0008
     * @Param [account, password]
     * @return java.lang.String
     **/
    public Object loginByAccount(String account, String password){
        if(this.userDao.findUserByAccountAndExist(account, 0) == null){
            User user = this.userDao.findUserByAccountAndPassword(account, password);
            if(user != null){
                return user;
            }
            return Result.PASSWORD_ERROR;
        }
        return Result.NOT_EFFECT;
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
        User resultUser = this.userDao.findUserById(user.getId());
        if(resultUser != null){
            return resultUser;
        }
        return "{}";
    }

    /**
     * @Author 张帅华
     * @Description 发送验证码用于找回密码，并返回验证码
     * @Date 17:15 2020/4/8 0008
     * @Param [account, email]
     * @return java.lang.String
     **/
    public String sendTestCodePassword(Integer userId, String email){
        User user = this.userDao.findUserById(userId);
        if(user != null){
            String userEmail = user.getEmail();
            if(!userEmail.equals("")){
                if(email.equals(userEmail)){
                    if(Email.findPasswordByMail(email)){
                        return Email.getCode();
                    }
                    return Result.FAIL;
                }
                return Result.EMAIL_ERROR;
            }
            return Result.NOT_BINDING_EMAIL;
        }
        return Result.NOT_EXIST_ACCOUNT;
    }

    /**
     * @Author 张帅华
     * @Description 验证是否已经绑定QQ
     * @Date 17:54 2020/4/8 0008
     * @Param [account]
     * @return java.lang.String
     **/
    public String isBindingQQ(Integer userId){
        User user = this.userDao.findUserById(userId);
        if(user.getUserAuthority() != null){
            return Result.TRUE;
        }
        return Result.FALSE;
    }

    /**
     * @Author 张帅华
     * @Description 账号绑定QQ
     * @Date 17:22 2020/4/8 0008
     * @Param [account, openId]
     * @return java.lang.String
     **/
    @Transactional(readOnly = false)
    public String bindingQQ(Integer userId, String openId){
        UserAuthority userAuthority = this.userAuthorityService.findUserAuthorityByOpenIdAndExist(openId, null);
        if(userAuthority != null){
            if(userAuthority.getUser() != null){
                return Result.ALREADY;
            }
        }else {
            userAuthority = new UserAuthority();
            userAuthority.setOpenId(openId);
            userAuthority.setType("QQ");
        }
        User user = this.userDao.findUserById(userId);
        user.setUserAuthority(userAuthority);
        userAuthority.setUser(user);
        return Result.TRUE;
    }

    /**
     * @Author 张帅华
     * @Description 账号解绑QQ
     * @Date 17:24 2020/4/8 0008
     * @Param [account]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public void removeUserAuthority(Integer userId){
        User user = this.userDao.findUserById(userId);
        UserAuthority userAuthority = user.getUserAuthority();
        user.setUserAuthority(null);
        if(userAuthority != null){
            userAuthority.setUser(null);
        }
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
                return -1;
        }
        if(flag == 1){
            user.setWater(user.getWater() + water);
            user.setFertilizer(user.getFertilizer() + fertilizer);
            return rewardCount;
        }
        return -1;
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
    public void editPasswordById(Integer userId, String password){
        User user = this.userDao.findUserById(userId);
        user.setPassword(password);
    }

    /**
     * @Author 张帅华
     * @Description 修改用户昵称
     * @Date 21:45 2020/4/8 0008
     * @Param [account, nickName]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public void editNickNameById(Integer userId, String nickName){
        User user = this.userDao.findUserById(userId);
        user.setNickName(nickName);
    }

    /**
     * @Author 张帅华
     * @Description 修改用户年级
     * @Date 21:45 2020/4/8 0008
     * @Param [account, grade]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public void editGradeById(Integer userId, Integer grade){
        User user = this.userDao.findUserById(userId);
        user.setGrade(grade);
    }

    /**
     * @Author 张帅华
     * @Description 修改用户头像
     * @Date 21:45 2020/4/8 0008
     * @Param [id, photo]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public void editPhotoById(Integer id, String photo){
        User user = this.userDao.findUserById(id);
        user.setPhoto(photo);
    }

    /**
     * @Author 张帅华
     * @Description 修改用户邮箱
     * @Date 21:47 2020/4/8 0008
     * @Param [account, email]
     * @return com.atguigu.farm.entity.User
     **/
    @Transactional(readOnly = false)
    public void editEmail(Integer userId, String email){
        User user = this.userDao.findUserById(userId);
        user.setEmail(email);
    }

    /**
     * @Author 张帅华
     * @Description 浇水
     * @Date 22:34 2020/4/8 0008
     * @Param [userId, landNumber]
     * @return java.lang.String
     **/
    @Task(description = "water")
    @Transactional(readOnly = false)
    public Integer waterCrop(Integer userId, String landNumber) {
        User user = this.userDao.findUserById(userId);
        Land land = user.getLand();
        UserCrop userCrop = this.userCropService.findUserCropByLand(land, landNumber);
        Crop crop = userCrop.getCrop();
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
                user.setWater(user.getWater() - 1);
                //修改作物干枯湿润状态
                if(userCrop.getStatus() == 0){
                    userCrop.setStatus(1);
                    return userCrop.getId();
                }
                return 0;
            }
        }
        return -1;
    }

    /**
     * @Author 张帅华
     * @Description 施肥
     * @Date 19:09 2020/4/9 0009
     * @Param [userId, landNumber]
     * @return java.lang.String
     **/
    @Task(description = "fertilize")
    @Transactional(readOnly = false)
    public String fertilizerCrop(Integer userId, String landNumber){
        User user = this.userDao.findUserById(userId);
        Land land = user.getLand();
        UserCrop userCrop = this.userCropService.findUserCropByLand(land, landNumber);
        Crop crop = userCrop.getCrop();
        if(userCrop.getStatus() != 0){
            //修改剩余化肥的次数
            if(user.getFertilizer() > 0){
                //修改作物进度
                int progress = userCrop.getProgress();
                int matureTime = crop.getMatureTime();
                if(progress < matureTime) {
                    if (progress+10 >= matureTime) {
                        userCrop.setProgress(crop.getMatureTime());
                    }else {
                        userCrop.setProgress(progress + 10);
                    }
                    user.setFertilizer(user.getFertilizer() - 1);
                    return Result.TRUE;
                }
            }
        }
        return Result.FALSE;
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
        Set<UserCropBag> userCropBags = user.getUserCropBags();
        Crop crop = this.cropService.findCropById(cropId);
        Integer userMoney = user.getMoney();
        Integer needMoney = crop.getPrice() * number;
        int flag = 0;
        if(userMoney >= needMoney){
            for(UserCropBag userCropBag : userCropBags){
                if(userCropBag.getCrop() == crop){
                    userCropBag.setNumber(userCropBag.getNumber() + number);
                    flag = 1;
                    break;
                }
            }
            if(flag != 1){
                UserCropBag userCropBag = new UserCropBag();
                userCropBag.setCrop(crop);
                userCropBag.setNumber(number);
                userCropBag.setUser(user);
                userCropBags.add(userCropBag);
            }
            user.setMoney(userMoney - needMoney);
            return Result.TRUE;
        }
        return Result.NOT_ENOUGH_MONEY;
    }

    /**
     * @description: 购买宠物
     * @author :景光赞
     * @date :2020/4/27 18:18
     * @param :[userId, petId]
     * @return :java.lang.String
     */
    @Transactional(readOnly = false)
    public String buyPet(Integer userId,Integer petId){
        User user = findUserById(userId);
        Set<UserPetHouse> petHouses = user.getPetHouses();
        Pet pet = petService.findPetById(petId);
        int userMoney = user.getMoney();
        int petMoney = pet.getPrice();
        if(userMoney >= petMoney){
            petHouses.add(new UserPetHouse(user,pet));
            user.setMoney(userMoney - petMoney);
            return Result.TRUE;
        }
        return Result.NOT_ENOUGH_MONEY;
    }

    @Transactional(readOnly = false)
    public String buyPetUtil(Integer userId, Integer petUtilId, Integer number){
        User user = this.userDao.findUserById(userId);
        Set<UserPetUtilBag> userPetUtilBags = user.getUserPetUtilBags();
        PetUtil petUtil = this.petUtilService.findPetUtilById(petUtilId);
        Integer userMoney = user.getMoney();
        Integer needMoney = petUtil.getPrice() * number;
        int flag = 0;
        if(userMoney >= needMoney){
            for(UserPetUtilBag userPetUtilBag : userPetUtilBags){
                if(userPetUtilBag.getPetUtil() == petUtil){
                    userPetUtilBag.setNumber(userPetUtilBag.getNumber() + number);
                    flag = 1;
                    break;
                }
            }
            if(flag != 1){
                UserPetUtilBag userPetUtilBag = new UserPetUtilBag();
                userPetUtilBag.setPetUtil(petUtil);
                userPetUtilBag.setNumber(number);
                userPetUtilBag.setUser(user);
                userPetUtilBags.add(userPetUtilBag);
            }
            user.setMoney(userMoney - needMoney);
            return Result.TRUE;
        }
        return Result.NOT_ENOUGH_MONEY;
    }

    /**
     * @description: 宠物喂食
     * @author :景光赞
     * @date :2020/5/3 22:01
     * @param :[userId, petId]
     * @return :java.lang.String
     */
    @Transactional(readOnly = false)
    public String feedPet(Integer userId, Integer userPetHouseId, Integer petUtilBagId){
        UserPetUtilBag userPetUtilBag = this.userPetHouseService.findUserPetUtilBagById(petUtilBagId);
        PetUtilType petUtilType = userPetUtilBag.getPetUtil().getPetUtilType();

        User user = this.userDao.findUserById(userId);
        Set<UserPetUtilBag> userPetUtilBagSet = user.getUserPetUtilBags();
        UserPetHouse userPetHouse = this.userPetHouseService.findUserPetHouseById(userPetHouseId);
        int number = userPetUtilBag.getNumber();
        if(number - 1 <= 0){
            userPetUtilBagSet.remove(userPetUtilBag);
            userPetUtilBag.setUser(null);
            this.userPetHouseService.deleteUserPetUtilBag(userPetUtilBag);
        }else{
            userPetUtilBag.setNumber(number - 1);
        }

        switch (petUtilType.getId()){
            case 1:
                int userPetLife = userPetHouse.getLife();
                int petLife = 3 * userPetHouse.getPet().getLife();
                int lifeValue = userPetUtilBag.getPetUtil().getValue();
                if(userPetLife < petLife){
                    if(userPetLife + lifeValue >= petLife){
                        userPetHouse.setLife(petLife);
                    }else{
                        userPetHouse.setLife(userPetLife + lifeValue);
                    }
                    return Result.TRUE;
                }else{
                    return Result.FALSE;
                }
            case 2:
                int userPetPhysical = userPetHouse.getPhysical();
                int petPhysical = userPetHouse.getPet().getPhysical();
                int physicalValue = userPetUtilBag.getPetUtil().getValue();
                if(userPetPhysical < petPhysical){
                    if(userPetPhysical + physicalValue >= petPhysical){
                        userPetHouse.setPhysical(petPhysical);
                    }else{
                        userPetHouse.setPhysical(userPetPhysical + physicalValue);
                    }
                    return Result.TRUE;
                }else{
                    return Result.FALSE;
                }
            case 3:
                int userPetIntelligence = userPetHouse.getIntelligence();
                int intelligenceValue = userPetUtilBag.getPetUtil().getValue();
                userPetHouse.setIntelligence(userPetIntelligence + intelligenceValue);
                return Result.TRUE;
        }
        return Result.FALSE;
    }

    /**
     * @Author 张帅华
     * @Description 种植作物
     * @Date 10:11 2020/4/10 0010
     * @Param [userId, cropId, landNumber]
     * @return java.lang.String
     **/
    @Task(description = "crop")
    @Transactional(readOnly = false)
    public Integer raiseCrop(Integer userId, Integer cropId, String landNumber){
        User user = this.userDao.findUserById(userId);
        Crop crop = this.cropService.findCropById(cropId);
        Set<UserCropBag> userCropBags = user.getUserCropBags();
        int number = 0;
        Iterator<UserCropBag> iterator = userCropBags.iterator();
        while(iterator.hasNext()){
            UserCropBag userCropBag = iterator.next();
            if(userCropBag.getCrop() == crop){
                number = userCropBag.getNumber();
                if((number - 1) <= 0){
                    iterator.remove();
                    userCropBag.setUser(null);
                    this.userBagService.deleteUserCropBag(userCropBag);
                }else{
                    userCropBag.setNumber(number - 1);
                }
                Land land = user.getLand();
                UserCrop userCrop = this.userCropService.findUserCropByLand(land, landNumber);
                userCrop.setCrop(crop);
                return userCrop.getId();
            }
        }
        return -1;
    }

    /**
     * @Author 张帅华
     * @Description 收获
     * @Date 11:03 2020/4/10 0010
     * @Param [userId, landNumber]
     * @return java.lang.String
     **/
    @Task(description = "harvest")
    @Transactional(readOnly = false)
    public String harvest(Integer userId, String landNumber){
        User user = this.userDao.findUserById(userId);
        Land land = user.getLand();
        UserCrop userCrop = this.userCropService.findUserCropByLand(land, landNumber);
        Crop crop = userCrop.getCrop();
        userCrop.setCrop(null);
        userCrop.setProgress(0);
        userCrop.setWaterLimit(15);
        userCrop.setFertilizerLimit(15);
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
        if(isLevel == 1){
            return Result.UP;
        }
        return Result.TRUE;
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
            this.userCropService.addUserCrop(land, new UserCrop(), landNumber);
            user.setMoney(userMoney - needMoney);
            return Result.TRUE;
        }
        return Result.NOT_ENOUGH_MONEY;
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

    @Transactional(readOnly = false)
    public void saveUser(User user){
        this.userDao.save(user);
    }

}
