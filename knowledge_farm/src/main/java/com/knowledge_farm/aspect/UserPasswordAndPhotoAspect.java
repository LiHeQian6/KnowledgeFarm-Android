package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.util.PageUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
            user.setPassword("");
            if(!(user.getPhoto().substring(0,4)).equals("http")){
                user.setPhoto(this.photoUrl + user.getPhoto());
            }
        }
    }

    @AfterReturning(pointcut = "notification()", returning="result")
    public void notification(JoinPoint joinPoint, Object result) {
        if(result instanceof PageUtil){
            for(Notification notification : ((PageUtil<Notification>) result).getList()){
                User from = notification.getFrom();
                User to = notification.getTo();
                if(from != null){
                    from.setPassword("");
                    if(!(from.getPhoto().substring(0,4)).equals("http")){
                        from.setPhoto(this.photoUrl + from.getPhoto());
                    }
                }
                if(to != null){
                    to.setPassword("");
                    if(!(to.getPhoto().substring(0,4)).equals("http")){
                        to.setPhoto(this.photoUrl + to.getPhoto());
                    }
                }
            }
        }
    }

    @AfterReturning(pointcut = "userFriend()", returning="result")
    public void userFriend(JoinPoint joinPoint, Object result) {
        if(result instanceof PageUtil){
            for(User user :  ((PageUtil<User>) result).getList()){
                user.setPassword("");
                if(!(user.getPhoto().substring(0,4)).equals("http")){
                    user.setPhoto(this.photoUrl + user.getPhoto());
                }
            }
        }
    }
}
