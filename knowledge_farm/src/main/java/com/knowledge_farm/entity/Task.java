package com.knowledge_farm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @program: knowledge_farm
 * @description: 每日任务
 * @author: 景光赞
 * @create: 2020-04-19 11:05
 **/
@Entity
@Table(name = "task")
public class Task {
    private int id;
    private int signIn;
    private int water;
    private int fertilize;
    private int crop;
    private int harvest;
    private int helpWater;
    private int helpFertilize;
    private User user;

    public Task() {
        this.signIn = 0;
        this.water = 0;
        this.fertilize = 0;
        this.crop = 0;
        this.harvest = 0;
        this.helpWater = 0;
        this.helpFertilize = 0;
    }

    public Task(User user) {
        this.signIn = 0;
        this.water = 0;
        this.fertilize = 0;
        this.crop = 0;
        this.harvest = 0;
        this.helpWater = 0;
        this.helpFertilize = 0;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "sign_in")
    public int getSignIn() {
        return signIn;
    }

    public void setSignIn(int signIn) {
        this.signIn = signIn;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getFertilize() {
        return fertilize;
    }

    public void setFertilize(int fertilize) {
        this.fertilize = fertilize;
    }

    public int getCrop() {
        return crop;
    }

    public void setCrop(int crop) {
        this.crop = crop;
    }

    public int getHarvest() {
        return harvest;
    }

    public void setHarvest(int harvest) {
        this.harvest = harvest;
    }

    @Column(name = "help_water")
    public int getHelpWater() {
        return helpWater;
    }

    public void setHelpWater(int helpWater) {
        this.helpWater = helpWater;
    }

    @Column(name = "help_fertilize")
    public int getHelpFertilize() {
        return helpFertilize;
    }

    public void setHelpFertilize(int helpFertilize) {
        this.helpFertilize = helpFertilize;
    }

    @OneToOne(mappedBy = "task")
    @org.hibernate.annotations.ForeignKey(name = "none")
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
