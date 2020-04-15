package com.atguigu.farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName Crop
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 14:06
 */
@Entity
@Table(name = "crop")
public class Crop {
    private Integer id;
    private String name;
    private Integer price;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String cropPhotoName;
    private Integer matureTime;
    private Integer value;
    private Integer experience;
    private Integer exist;

    @Id
    @GeneratedValue(generator="identity")
    @GenericGenerator(name="identity", strategy="identity")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    @Column(name = "crop_photo_name")
    public String getCropPhotoName() {
        return cropPhotoName;
    }

    public void setCropPhotoName(String cropPhotoName) {
        this.cropPhotoName = cropPhotoName;
    }

    @Column(name = "mature_time")
    public Integer getMatureTime() {
        return matureTime;
    }

    public void setMatureTime(Integer matureTime) {
        this.matureTime = matureTime;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    @Column(insertable = false)
    public Integer getExist() {
        return exist;
    }

    public void setExist(Integer exist) {
        this.exist = exist;
    }

    @Override
    public String toString() {
        return "Crop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                ", img3='" + img3 + '\'' +
                ", img4='" + img4 + '\'' +
                ", cropPhotoName='" + cropPhotoName + '\'' +
                ", matureTime=" + matureTime +
                ", value=" + value +
                ", experience=" + experience +
                ", exist=" + exist +
                '}';
    }

}
