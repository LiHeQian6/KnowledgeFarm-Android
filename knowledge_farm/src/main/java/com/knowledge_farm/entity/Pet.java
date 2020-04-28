package com.knowledge_farm.entity;

import javax.persistence.*;

/**
 * @program: knowledge_farm
 * @description: 宠物
 * @author: 景光赞
 * @create: 2020-04-25 10:26
 **/
@Entity
@Table(name = "pet")
public class Pet {
    private Integer id;
    private String name;
    private String description;
    private Integer growPeriod;
    private Integer life;
    private Integer intelligence;
    private Integer physical;
    private Integer price;
    private String img1;
    private String img2;
    private String img3;
    private Integer exist;

    public Pet() {
    }

//    public Pet(String name, String description, Integer life, Integer intelligence, Integer physical) {
//        this.name = name;
//        this.description = description;
//        this.life = life;
//        this.intelligence = intelligence;
//        this.physical = physical;
//        this.growPeriod = 0;
//        this.exist = 1;
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "grow_period")
    public Integer getGrowPeriod() {
        return growPeriod;
    }

    public void setGrowPeriod(Integer growPeriod) {
        this.growPeriod = growPeriod;
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getPhysical() {
        return physical;
    }

    public void setPhysical(Integer physical) {
        this.physical = physical;
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

    @Column(insertable = false, columnDefinition = "1")
    public Integer getExist() {
        return exist;
    }

    public void setExist(Integer exist) {
        this.exist = exist;
    }

}
