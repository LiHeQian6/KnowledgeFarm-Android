package com.knowledge_farm.entity;

import javax.persistence.*;

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
    private int number;

    public UserPetHouse() {
    }

    public UserPetHouse(User user, Pet pet, int number) {
        this.user = user;
        this.pet = pet;
        this.number = number;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
