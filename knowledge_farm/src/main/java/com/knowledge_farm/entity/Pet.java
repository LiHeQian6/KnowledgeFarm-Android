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
    private int id;
    private String name;
    private int growPeriod;
    private int life;
    private int intelligence;
    private int physical;
    private int price;
    private String img1;
    private String img2;
    private String img3;

    public Pet() {
    }

    public Pet(String name, int life, int intelligence, int physical) {
        this.name = name;
        this.life = life;
        this.intelligence = intelligence;
        this.physical = physical;
        this.growPeriod = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrowPeriod() {
        return growPeriod;
    }

    public void setGrowPeriod(int growPeriod) {
        this.growPeriod = growPeriod;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getPhysical() {
        return physical;
    }

    public void setPhysical(int physical) {
        this.physical = physical;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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
}
