package com.knowledge_farm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
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

    @Column(insertable = false, columnDefinition = "int default 0")
    public Integer getIfUsing() {
        return ifUsing;
    }

    public void setIfUsing(Integer ifUsing) {
        this.ifUsing = ifUsing;
    }

    @Column(insertable = false, columnDefinition = "int default 0")
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

}
