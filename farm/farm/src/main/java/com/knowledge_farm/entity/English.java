package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName English
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 14:09
 */
@Entity
@Table(name = "english")
public class English {
    private Integer id;
    private String word;
    private String trans;
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
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
        return "English{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", trans='" + trans + '\'' +
                ", ifDone='" + ifDone + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }

}
