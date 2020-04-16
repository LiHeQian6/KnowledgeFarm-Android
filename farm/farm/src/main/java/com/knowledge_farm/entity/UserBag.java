package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName UserBag
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 14:12
 */
@Entity
@Table(name = "user_bag")
public class UserBag {
    private Integer id;
    private Crop crop;
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
    @JoinColumn(name = "crop_id")
    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "UserBag{" +
                "id=" + id +
                ", crop=" + crop +
                ", number=" + number +
                '}';
    }

}
