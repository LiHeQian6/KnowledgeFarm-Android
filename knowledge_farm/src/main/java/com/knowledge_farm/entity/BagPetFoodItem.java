package com.knowledge_farm.entity;

/**
 * @ClassName BagPetFoodItem
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 10:17
 */
public class BagPetFoodItem {
    private Integer number;
    private PetFood petFood;

    public BagPetFoodItem(Integer number, PetFood petFood) {
        this.number = number;
        this.petFood = petFood;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public PetFood getPetFood() {
        return petFood;
    }

    public void setPetFood(PetFood petFood) {
        this.petFood = petFood;
    }

}
