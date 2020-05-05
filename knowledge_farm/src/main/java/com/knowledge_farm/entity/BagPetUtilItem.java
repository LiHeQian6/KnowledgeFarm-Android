package com.knowledge_farm.entity;

/**
 * @ClassName BagPetFoodItem
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 10:17
 */
public class BagPetUtilItem {
    private Integer number;
    private PetUtil petUtil;

    public BagPetUtilItem(Integer number, PetUtil petUtil) {
        this.number = number;
        this.petUtil = petUtil;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public PetUtil getPetUtil() {
        return petUtil;
    }

    public void setPetUtil(PetUtil petUtil) {
        this.petUtil = petUtil;
    }

}
