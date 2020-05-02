package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.Pet;
import com.knowledge_farm.entity.UserPetHouse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName PetPhotoAspect
 * @Description
 * @Author 张帅华
 * @Date 2020-05-02 16:48
 */
@Component
@Aspect
public class PetPhotoAspect {
    @Value("${file.photoUrl}")
    private String photoUrl;

    @Pointcut(value = "execution(* com.knowledge_farm.pet.controller.PetController.showInStore(..))")
    private void showInStore() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_pet_house.controller.UserPetHouseController.showUserPetHouse(..))")
    private void showUserPetHouse() {
    }

    @AfterReturning(pointcut = "showInStore()", returning="result")
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

}
