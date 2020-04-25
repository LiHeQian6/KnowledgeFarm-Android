package com.knowledge_farm.user_friend.service;

import com.knowledge_farm.entity.*;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.user_crop.service.UserCropServiceImpl;
import com.knowledge_farm.user_friend.dao.UserFriendDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName UserFriendServiceImpl
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 22:44
 */
@Service
@Transactional(readOnly = true)
public class UserFriendServiceImpl {
    @Resource
    private UserFriendDao userFriendDao;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private UserCropServiceImpl userCropService;

    public Page<User> findUserFriendPageByAccount(Integer userId, String account, Integer pageNumber, Integer pageSize){
        if(account != null && !account.equals("")){
            return this.userFriendDao.findUserFriendPageByAccount(userId, account, PageRequest.of(pageNumber - 1, pageSize));
        }
        return this.userFriendDao.findUserFriendPage(userId, PageRequest.of(pageNumber - 1, pageSize));
    }

    public Page<User> findAllUserByAccount(String account, Integer pageNumber, Integer pageSize){
        return this.userService.findAllUserByAccount(account, pageNumber, pageSize);
    }

    @Transactional(readOnly = false)
    public void addUserFriend(Integer userId, String account){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserByAccount(account);
        UserFriend userFriend = new UserFriend();
        userFriend.setUser(user);
        userFriend.setFriendUser(friendUser);
        this.userFriendDao.save(userFriend);
    }

    @Transactional(readOnly = false)
    public void deleteUserFriend(Integer userId, String account){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserByAccount(account);
        UserFriend userFriend = this.userFriendDao.findUserFriendByUserAndFriendUser(user.getId(), friendUser.getId());
        this.userFriendDao.delete(userFriend);
    }

    @Transactional(readOnly = false)
    public int waterForFriend(Integer userId, Integer friendId, String landNumber){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserById(friendId);
        Land land = friendUser.getLand();
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
            }else{
                return -1;
            }
        }else{
            return -1;
        }
        user.setMoney(user.getMoney() + 10);
        //修改作物干枯湿润状态
        if(userCrop.getStatus() == 0){
            userCrop.setStatus(1);
            return userCrop.getId();
        }
        return 0;
    }

    @Transactional(readOnly = false)
    public String fertilizerForFriend(Integer userId, Integer friendId, String landNumber){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserById(friendId);
        Land land = friendUser.getLand();
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
                    user.setMoney(user.getMoney() + 20);
                    return Result.TRUE;
                }else{
                    return Result.FALSE;
                }
            }else{
                return Result.FALSE;
            }
        }
        return Result.FALSE;
    }

}
