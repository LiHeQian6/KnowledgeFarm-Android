package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName PetUtiltype
 * @Description
 * @Author 张帅华
 * @Date 2020-05-04 17:42
 */
@Entity
@Table(name = "pet_util_type")
public class PetUtilType {
    private Integer id;
    private String name;

    @Id
    @GeneratedValue(generator="identity")
    @GenericGenerator(name="identity", strategy = "identity")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
