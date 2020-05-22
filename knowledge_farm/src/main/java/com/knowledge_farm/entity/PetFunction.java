package com.knowledge_farm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @ClassName PetFunction
 * @Description
 * @Author 张帅华
 * @Date 2020-05-20 15:22
 */
@Entity
@Table(name = "pet_function")
public class PetFunction {
    private Integer petFunctionId;
    private Pet pet;
    private Integer harvestHour1;
    private Integer harvestHour2;
    private Integer harvestHour3;
    private Integer growHour1;
    private Integer growHour2;
    private Integer growHour3;

    public PetFunction() {
    }

    public PetFunction(Pet pet, Integer harvestHour1, Integer harvestHour2, Integer harvestHour3, Integer growHour1, Integer growHour2, Integer growHour3){
        this.pet = pet;
        this.harvestHour1 = harvestHour1;
        this.harvestHour2 = harvestHour2;
        this.harvestHour3 = harvestHour3;
        this.growHour1 = growHour1;
        this.growHour2 = growHour2;
        this.growHour3 = growHour3;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getPetFunctionId() {
        return petFunctionId;
    }

    public void setPetFunctionId(Integer petFunctionId) {
        this.petFunctionId = petFunctionId;
    }

    @OneToOne
    @JoinColumn(name = "pet_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Column(name = "harvest_hour1")
    public Integer getHarvestHour1() {
        return harvestHour1;
    }

    public void setHarvestHour1(Integer harvestHour1) {
        this.harvestHour1 = harvestHour1;
    }

    @Column(name = "harvest_hour2")
    public Integer getHarvestHour2() {
        return harvestHour2;
    }

    public void setHarvestHour2(Integer harvestHour2) {
        this.harvestHour2 = harvestHour2;
    }

    @Column(name = "harvest_hour3")
    public Integer getHarvestHour3() {
        return harvestHour3;
    }

    public void setHarvestHour3(Integer harvestHour3) {
        this.harvestHour3 = harvestHour3;
    }

    @Column(name = "grow_hour1")
    public Integer getGrowHour1() {
        return growHour1;
    }

    public void setGrowHour1(Integer growHour1) {
        this.growHour1 = growHour1;
    }

    @Column(name = "grow_hour2")
    public Integer getGrowHour2() {
        return growHour2;
    }

    public void setGrowHour2(Integer growHour2) {
        this.growHour2 = growHour2;
    }

    @Column(name = "grow_hour3")
    public Integer getGrowHour3() {
        return growHour3;
    }

    public void setGrowHour3(Integer growHour3) {
        this.growHour3 = growHour3;
    }

    @Override
    public String toString() {
        return "PetFunction{" +
                "petFunctionId=" + petFunctionId +
                ", pet=" + pet +
                ", harvestHour1=" + harvestHour1 +
                ", harvestHour2=" + harvestHour2 +
                ", harvestHour3=" + harvestHour3 +
                ", growHour1=" + growHour1 +
                ", growHour2=" + growHour2 +
                ", growHour3=" + growHour3 +
                '}';
    }

}
