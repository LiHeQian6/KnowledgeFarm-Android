package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName QuestionType
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 09:55
 */
@Entity
@Table(name = "question_type")
public class QuestionType {
    private Integer id;
    private String name;

    public QuestionType() {
    }

    public QuestionType(int id,String name) {
        this.id = id;
        this.name = name;
    }

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
