package com.li.knowledgefarm.entity;

import java.util.Random;

import androidx.annotation.Nullable;

public class UserPetHouse implements Comparable<UserPetHouse> {
    private Integer id;
    private User user;
    private Pet pet;
    private Integer ifUsing;            //是否正在使用
    private Integer growPeriod;
    private Integer life;       //生命值
    private Integer intelligence;       //智力值
    private Integer physical;         //体力值

    public UserPetHouse() {
    }

    public UserPetHouse(User user, Pet pet) {
        this.user = user;
        this.pet = pet;
        life = new Random().nextInt(pet.getLife())+30;
        intelligence = new Random().nextInt(pet.getIntelligence())+30;
        physical = new Random().nextInt(pet.getPhysical())+30;
        this.ifUsing = 0;
        this.growPeriod = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Integer getIfUsing() {
        return ifUsing;
    }

    public void setIfUsing(Integer ifUsing) {
        this.ifUsing = ifUsing;
    }

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


    @Override
    public int compareTo(UserPetHouse userPetHouse) {
        if (ifUsing==1)
            return -1;
        else if(userPetHouse.getIfUsing()==1)
            return 1;
        if (id>userPetHouse.getId())
            return 1;
        else if (id<userPetHouse.getId())
            return -1;
        return 0;
    }
}
