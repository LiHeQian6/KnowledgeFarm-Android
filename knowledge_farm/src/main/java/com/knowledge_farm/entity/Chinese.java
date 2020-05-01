package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Chinese
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 14:05
 */
@Entity
@Table(name = "chinese")
public class Chinese implements Serializable {
    private Integer id;
    private String quantify;
    private String word;
    private String ifDone;
    private String grade;

    @Id
    @GeneratedValue(generator="identity")
    @GenericGenerator(name="identity", strategy="identity")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuantify() {
        return quantify;
    }

    public void setQuantify(String quantify) {
        this.quantify = quantify;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Column(name = "if_done")
    public String getIfDone() {
        return ifDone;
    }

    public void setIfDone(String ifDone) {
        this.ifDone = ifDone;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Chinese{" +
                "id=" + id +
                ", quantify='" + quantify + '\'' +
                ", word='" + word + '\'' +
                ", ifDone='" + ifDone + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }

}
