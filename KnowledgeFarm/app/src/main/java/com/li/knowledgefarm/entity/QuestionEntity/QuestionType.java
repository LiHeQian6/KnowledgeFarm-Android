package com.li.knowledgefarm.entity.QuestionEntity;

import java.io.Serializable;

/**
 * @ClassName QuestionType
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 09:55
 */
public class QuestionType implements Serializable {
    private Integer id;
    private String name;

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
