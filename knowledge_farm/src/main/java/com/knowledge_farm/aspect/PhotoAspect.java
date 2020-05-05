package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    @AfterReturning(pointcut = "showCropInStore()", returning="result")
    public void crop(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(Crop crop : ((List<Crop>) result)){
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
            for(Pet pet : ((List<Pet>) result)){
                pet.setImg1(photoUrl + pet.getImg1());
                pet.setImg2(photoUrl + pet.getImg2());
                pet.setImg3(photoUrl + pet.getImg3());
            }
        }
    }

    @AfterReturning(pointcut = "showUserPetHouse()", returning="result")
    public void showUserPetHouse(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(UserPetHouse userPetHouse : ((List<UserPetHouse>) result)){
                Pet pet = userPetHouse.getPet();
                pet.setImg1(photoUrl + pet.getImg1());
                pet.setImg2(photoUrl + pet.getImg2());
                pet.setImg3(photoUrl + pet.getImg3());
            }
        }
    }

    @AfterReturning(pointcut = "showPetUtilInStore()", returning="result")
    public void showPetUtilInStore(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(PetUtil petfood : ((List<PetUtil>) result)){
                petfood.setImg(photoUrl + petfood.getImg());
            }
        }
    }

    @AfterReturning(pointcut = "showUserPetUtilBag()", returning="result")
    public void showUserPetUtilBag(JoinPoint joinPoint, Object result) {
        if(result instanceof List){
            for(BagPetUtilItem bagPetUtilItem : ((List<BagPetUtilItem>) result)){
                PetUtil petUtil = bagPetUtilItem.getPetUtil();
                petUtil.setImg(photoUrl + petUtil.getImg());
            }
        }
    }

}
