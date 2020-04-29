package com.knowledge_farm.entity;

import javax.persistence.*;
import java.util.Random;

/**
 * @program: knowledge_farm
 * @description: 宠物仓库
 * @author: 景光赞
 * @create: 2020-04-27 10:37
 **/
@Entity
@Table(name = "user_pet")
public class UserPetHouse {
    private int id;
    private User user;
    private Pet pet;
    private int ifUsing;            //是否正在使用
    private Integer life;       //生命值
    private Integer intelligence;       //智力值
    private Integer physical;         //体力值

    public UserPetHouse() {
    }

    public UserPetHouse(User user, Pet pet) {
        this.user = user;
        this.pet = pet;
        ifUsing = 0;
        life = new Random().nextInt(pet.getLife())+30;
        intelligence = new Random().nextInt(pet.getIntelligence())+30;
        physical = new Random().nextInt(pet.getPhysical())+30;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "pet_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public int getIfUsing() {
        return ifUsing;
    }

    public void setIfUsing(int ifUsing) {
        this.ifUsing = ifUsing;
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
}
