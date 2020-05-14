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
import java.util.List;

/**
 * @ClassName CropPhotoAspect
 * @Description
 * @Author 张帅华
 * @Date 2020-04-24 09:59
 */
@Component
@Aspect
public class PhotoAspect {
    @Value("${file.photoUrl}")
    private String photoUrl;

    @Pointcut(value = "execution(* com.knowledge_farm.crop.controller.CropController.initCrop(..))")
    private void showCropInStore() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_bag.controller.UserBagController.initUserCropBag(..))")
    private void userCropBag() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_crop.controller.UserCropController.initUserCrop(..))")
    private void userCrop() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.pet.controller.PetController.showInStore(..))")
    private void showPetInStore() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_pet_house.controller.UserPetHouseController.showUserPetHouse(..))")
    private void showUserPetHouse() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.pet_util.controller.PetUtilController.showInStore(..))")
    private void showPetUtilInStore() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_pet_house.controller.UserPetHouseController.initUserPetUtilBag(..))")
    private void showUserPetUtilBag() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user.controller.UserController.*(..))")
    private void user() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.notification.controller.NotificationController.*(..))")
    private void notification() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_friend.controller.UserFriendController.*(..))")
    private void userFriend() {
    }

    @AfterReturning(pointcut = "showCropInStore()", returning="result")
    public void crop(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(Crop crop : ((List<Crop>) result)){
                if((crop.getImg1().substring(0,4)).equals("http")){
                    continue;
                }
                crop.setImg1(photoUrl + crop.getImg1());
                crop.setImg2(photoUrl + crop.getImg2());
                crop.setImg3(photoUrl + crop.getImg3());
                crop.setImg4(photoUrl + crop.getImg4());
            }
        }
    }

    @AfterReturning(pointcut = "userCropBag()", returning="result")
    public void userBag(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(BagCropItem item : ((List<BagCropItem>) result)){
                Crop crop = item.getCrop();
                if((crop.getImg1().substring(0,4)).equals("http")){
                    continue;
                }
                crop.setImg1(photoUrl + crop.getImg1());
                crop.setImg2(photoUrl + crop.getImg2());
                crop.setImg3(photoUrl + crop.getImg3());
                crop.setImg4(photoUrl + crop.getImg4());
            }
        }
    }

    @AfterReturning(pointcut = "userCrop()", returning="result")
    public void userCrop(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(UserCrop userCrop : ((List<UserCrop>) result)){
                if(userCrop != null){
                    Crop crop = userCrop.getCrop();
                    if(crop != null){
                        if((crop.getImg1().substring(0,4)).equals("http")){
                            continue;
                        }
                        crop.setImg1(photoUrl + crop.getImg1());
                        crop.setImg2(photoUrl + crop.getImg2());
                        crop.setImg3(photoUrl + crop.getImg3());
                        crop.setImg4(photoUrl + crop.getImg4());
                    }
                }
            }
        }
    }

    @AfterReturning(pointcut = "showPetInStore()", returning="result")
    public void showInStore(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(PetVO petVO : ((List<PetVO>) result)){
                if((petVO.getImg1().substring(0,4)).equals("http")){
                    continue;
                }
                petVO.setImg1(photoUrl + petVO.getImg1());
                petVO.setImg2(photoUrl + petVO.getImg2());
                petVO.setImg3(photoUrl + petVO.getImg3());
            }
        }
    }

    @AfterReturning(pointcut = "showUserPetHouse()", returning="result")
    public void showUserPetHouse(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(UserPetHouse userPetHouse : ((List<UserPetHouse>) result)){
                Pet pet = userPetHouse.getPet();
                if((pet.getImg1().substring(0,4)).equals("http")){
                    continue;
                }
                pet.setImg1(photoUrl + pet.getImg1());
                pet.setImg2(photoUrl + pet.getImg2());
                pet.setImg3(photoUrl + pet.getImg3());
            }
        }
    }

    @AfterReturning(pointcut = "showPetUtilInStore()", returning="result")
    public void showPetUtilInStore(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(PetUtil petUtil : ((List<PetUtil>) result)){
                if((petUtil.getImg().substring(0,4)).equals("http")){
                    continue;
                }
                petUtil.setImg(photoUrl + petUtil.getImg());
            }
        }
    }

    @AfterReturning(pointcut = "showUserPetUtilBag()", returning="result")
    public void showUserPetUtilBag(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(BagPetUtilItem bagPetUtilItem : ((List<BagPetUtilItem>) result)){
                PetUtil petUtil = bagPetUtilItem.getPetUtil();
                if((petUtil.getImg().substring(0,4)).equals("http")){
                    continue;
                }
                petUtil.setImg(photoUrl + petUtil.getImg());
            }
        }
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
                    changeUser(to);
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
            if((pet.getImg1().substring(0,4)).equals("http")){
                continue;
            }
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
