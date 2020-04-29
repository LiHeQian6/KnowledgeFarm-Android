package com.knowledge_farm.entity;

import javax.persistence.*;

/**
 * @ClassName UserTag
 * @Description
 * @Author 张帅华
 * @Date 2020-04-29 15:40
 */
@Entity
@Table(name = "user_tag")
public class UserTag {
    private Integer id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
