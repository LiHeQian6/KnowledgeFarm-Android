package com.knowledge_farm.entity;

/**
 * @ClassName Grade
 * @Description
 * @Author 张帅华
 * @Date 2020-05-16 18:45
 */
public class Grade {
    private Integer id;
    private String name;

    public Grade(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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
