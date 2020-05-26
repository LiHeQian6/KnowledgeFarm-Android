package com.li.knowledgefarm.entity;

import java.io.Serializable;

public class Crop implements Serializable {

    private int exist;
    private String img3;
    private String img4;
    private int matureTime;
    private int price;
    private String name;
    private int id;
    private String cropPhotoName;
    private int experience;
    private int value;
    private String img2;
    private String img1;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExist(int exist) {
         this.exist = exist;
     }
     public int getExist() {
         return exist;
     }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
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

    @Override
    public String toString() {
        return "Crop{" +
                "exist=" + exist +
                ", img3='" + img3 + '\'' +
                ", img4='" + img4 + '\'' +
                ", matureTime=" + matureTime +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", cropPhotoName='" + cropPhotoName + '\'' +
                ", experience=" + experience +
                ", value=" + value +
                ", img2='" + img2 + '\'' +
                ", img1='" + img1 + '\'' +
                '}';
    }
}