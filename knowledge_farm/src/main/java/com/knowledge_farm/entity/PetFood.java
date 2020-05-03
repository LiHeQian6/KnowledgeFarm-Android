package com.knowledge_farm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @program: knowledge_farm
 * @description: 宠物饲料
 * @author: 景光赞
 * @create: 2020-05-03 14:21
 **/
@Entity
@Table(name = "pet_food")
public class PetFood {
    private int id;
    private User user;
    private String name;
    private int num;
    private int price;

    public PetFood() {
    }

    public PetFood(User user, String name, int num) {
        this.user = user;
        this.name = name;
        this.num = num;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne(mappedBy = "petFood")
    @org.hibernate.annotations.ForeignKey(name = "none")
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
