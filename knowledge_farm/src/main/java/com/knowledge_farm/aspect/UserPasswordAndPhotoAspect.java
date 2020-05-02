package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserPetHouse;
import com.knowledge_farm.entity.UserVO;
import com.knowledge_farm.util.PageUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName UserPasswordAndPhotoAspect
 * @Description
 * @Author 张帅华
 * @Date 2020-04-24 09:20
 */
@Component
@Aspect
public class UserPasswordAndPhotoAspect {
    @Value("${file.photoUrl}")
    private String photoUrl;

    @Pointcut(value = "execution(* com.knowledge_farm.user.controller.UserController.*(..))")
    private void user() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.notification.controller.NotificationController.*(..))")
    private void notification() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_friend.controller.UserFriendController.*(..))")
    private void userFriend() {
    }

    @AfterReturning(pointcut = "user()", returning="result")
    public void user(JoinPoint joinPoint, Object result) {
        if(result instanceof User){
            User user = (User) result;
            changeUser(user);
        }
    }

    @AfterReturning(pointcut = "notification()", returning="result")
    public void notification(JoinPoint joinPoint, Object result) {
        if(result instanceof PageUtil){
            for(Notification notification : ((PageUtil<Notification>) result).getList()){
                User from = notification.getFrom();
                User to = notification.getTo();
                if(from != null){
                    changeUser(from);
                }
                if(to != null){
                    changeUser(from);
                }
            }
        }
    }

    @AfterReturning(pointcut = "userFriend()", returning="result")
    public void userFriend(JoinPoint joinPoint, Object result) {
        if(result instanceof PageUtil){
            for(User user : ((PageUtil<User>) result).getList()){
                changeUser(user);
            }
        }
    }

    public User changeUser(User user){
        if(!(user.getPhoto().substring(0,4)).equals("http")){
            user.setPhoto(this.photoUrl + user.getPhoto());
        }
        Iterator<UserPetHouse> iterator = user.getPetHouses().iterator();
        while(iterator.hasNext()){
            if(iterator.next().getIfUsing() != 1){
                iterator.remove();
            }
        }
        return user;
    }

    public UserVO varyUserToUserVO(User user){
        if(!(user.getPhoto().substring(0,4)).equals("http")){
            user.setPhoto(this.photoUrl + user.getPhoto());
        }
        Set<UserPetHouse> userPetHouseSet = user.getPetHouses();
        for(UserPetHouse userPetHouse : userPetHouseSet){
            if(userPetHouse.getIfUsing() != 1){
                userPetHouseSet.remove(userPetHouse);
            }
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setAccount(user.getAccount());
        userVO.setNickName(user.getNickName());
        userVO.setPhoto(user.getPhoto());
        userVO.setEmail(user.getEmail());
        userVO.setLevel(user.getLevel());
        userVO.setExperience(user.getExperience());
        userVO.setGrade(user.getGrade());
        userVO.setMoney(user.getMoney());
        userVO.setMathRewardCount(user.getMathRewardCount());
        userVO.setEnglishRewardCount(user.getEnglishRewardCount());
        userVO.setChineseRewardCount(user.getChineseRewardCount());
        userVO.setWater(user.getWater());
        userVO.setFertilizer(user.getFertilizer());
        userVO.setOnline(user.getOnline());
        userVO.setExist(user.getExist());
        userVO.setUserAuthority(user.getUserAuthority());
        userVO.setUserPetHouseSet(user.getPetHouses());
        return userVO;
    }
}
