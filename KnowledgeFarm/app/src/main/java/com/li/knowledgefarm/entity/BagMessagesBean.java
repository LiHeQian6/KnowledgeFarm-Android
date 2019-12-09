package com.li.knowledgefarm.entity;

/**
 * @auther 孙建旺
 * @description 背包信息实体类
 * @date 2019/12/08 下午 2:20
 */

public class BagMessagesBean {
    private int exist;
    private String img3;
    private int matureTime;
    private int price;
    private String name;
    private int id;
    private String cropPhotoName;
    private int experience;
    private int value;
    private String img2;
    private String img1;
    public void setExist(int exist) {
        this.exist = exist;
    }
    public int getExist() {
        return exist;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }
    public String getImg3() {
        return img3;
    }

    public void setMatureTime(int matureTime) {
        this.matureTime = matureTime;
    }
    public int getMatureTime() {
        return matureTime;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public int getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setCropPhotoName(String cropPhotoName) {
        this.cropPhotoName = cropPhotoName;
    }
    public String getCropPhotoName() {
        return cropPhotoName;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
    public int getExperience() {
        return experience;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }
    public String getImg2() {
        return img2;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }
    public String getImg1() {
        return img1;
    }
}
