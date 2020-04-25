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
}
