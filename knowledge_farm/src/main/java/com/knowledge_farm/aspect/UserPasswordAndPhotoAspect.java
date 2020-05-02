package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.*;
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
            UserPetHouse userPetHouse = iterator.next();
            Pet pet = userPetHouse.getPet();
            pet.setImg1(photoUrl + pet.getImg1());
            pet.setImg2(photoUrl + pet.getImg2());
            pet.setImg3(photoUrl + pet.getImg3());
            if(userPetHouse.getIfUsing() != 1){
                iterator.remove();
            }
        }
        return user;
    }

}
