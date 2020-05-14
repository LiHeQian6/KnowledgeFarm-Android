package com.li.knowledgefarm.entity;

import java.io.Serializable;

/**
 * @ClassName BagPetFoodItem
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 10:17
 */
public class BagPetUtilItem implements Comparable<BagPetUtilItem>, Serializable {
    private Integer id;
    private Integer number;
    private PetUtil petUtil;


    public BagPetUtilItem(Integer id, Integer number, PetUtil petUtil) {
        this.id = id;
        this.number = number;
        this.petUtil = petUtil;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public int compareTo(BagPetUtilItem bagPetUtilItem) {
        if (this.petUtil.getId()<bagPetUtilItem.getPetUtil().getId()) {
            return -1;
        }else {
            return 1;
        }
    }
}
