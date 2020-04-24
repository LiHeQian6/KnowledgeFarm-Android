package com.knowledge_farm.aspect;

import com.knowledge_farm.entity.BagCropItem;
import com.knowledge_farm.entity.Crop;
import com.knowledge_farm.entity.UserCrop;
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
public class CropPhotoAspect {
    @Value("${file.photoUrl}")
    private String photoUrl;

    @Pointcut(value = "execution(* com.knowledge_farm.crop.controller.CropController.*(..))")
    private void crop() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_bag.controller.UserBagController.*(..))")
    private void userBag() {
    }

    @Pointcut(value = "execution(* com.knowledge_farm.user_crop.controller.UserCropController.*(..))")
    private void userCrop() {
    }

    @AfterReturning(pointcut = "crop()", returning="result")
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

    @AfterReturning(pointcut = "userBag()", returning="result")
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

}
