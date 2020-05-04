package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName User_PetFood_Bag
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 09:18
 */
@Entity
@Table(name = "user_pet_food_bag")
public class UserPetFoodBag {
    private Integer id;
    private User user;
    private PetFood petFood;
    private Integer number;

    @Id
    @GeneratedValue(generator="identity")
    @GenericGenerator(name="identity", strategy="identity")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
    @JoinColumn(name = "pet_food_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    public PetFood getPetFood() {
        return petFood;
    }

    public void setPetFood(PetFood petFood) {
        this.petFood = petFood;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}
