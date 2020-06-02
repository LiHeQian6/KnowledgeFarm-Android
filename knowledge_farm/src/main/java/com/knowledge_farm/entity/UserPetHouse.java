package com.knowledge_farm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.knowledge_farm.util.RateRandomNumber;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
        this.growPeriod = 0;
        this.ifUsing = 0;
    }

    public UserPetHouse(User user, Pet pet) {
        this.user = user;
        this.pet = pet;
        this.growPeriod = 0;
        this.ifUsing = 0;
        physical = pet.getPhysical();

        //中间数据大概率随机取值life
        double minLife = 0.7 * pet.getLife();
        double maxLife = pet.getLife();
        List<Integer> lifeSeparates = new ArrayList<>();
        lifeSeparates.add((int) Math.ceil(minLife + 0.2 * (maxLife - minLife)));
        lifeSeparates.add((int) Math.ceil(maxLife - 0.2 * (maxLife - minLife)));
        List<Integer> lifePercents = new ArrayList<>();
        lifePercents.add(10);
        lifePercents.add(80);
        lifePercents.add(10);
        life = RateRandomNumber.produceRateRandomNumber((int) Math.ceil(minLife), (int) Math.ceil(maxLife), lifeSeparates, lifePercents);

        //中间数据大概率随机取值intelligence
        double minIntelligence = 0.7 * pet.getIntelligence();
        double maxIntelligence = pet.getIntelligence();
        List<Integer> intelligenceSeparates = new ArrayList<>();
        intelligenceSeparates.add((int) Math.ceil(minIntelligence + 0.2 * (maxIntelligence - minIntelligence)));
        intelligenceSeparates.add((int) Math.ceil(maxIntelligence - 0.2 * (maxIntelligence - maxIntelligence)));
        List<Integer> intelligencePercents = new ArrayList<>();
        intelligencePercents.add(10);
        intelligencePercents.add(80);
        intelligencePercents.add(10);
        intelligence = RateRandomNumber.produceRateRandomNumber((int) Math.ceil(minIntelligence), (int) Math.ceil(maxIntelligence), intelligenceSeparates, intelligencePercents);
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

    @Column(name = "if_using", columnDefinition = "int default 0")
    public Integer getIfUsing() {
        return ifUsing;
    }

    public void setIfUsing(Integer ifUsing) {
        this.ifUsing = ifUsing;
    }

    @Column(columnDefinition = "int default 0")
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
    public String toString() {
        return "UserPetHouse{" +
                "id=" + id +
                ", user=" + user +
                ", pet=" + pet +
                ", ifUsing=" + ifUsing +
                ", growPeriod=" + growPeriod +
                ", life=" + life +
                ", intelligence=" + intelligence +
                ", physical=" + physical +
                '}';
    }
}
