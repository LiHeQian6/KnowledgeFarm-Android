package com.li.knowledgefarm.entity.QuestionEntity;

import java.io.Serializable;

/**
 * @auther 孙建旺
 * @description 语文题实体类
 * @date 2019/12/17 下午 3:35
 */

public class Chinese implements Serializable {
    private int id;
    private String quantify;
    private String word;
    private String ifDone;
    private String grade;
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setQuantify(String quantify) {
        this.quantify = quantify;
    }
    public String getQuantify() {
        return quantify;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public String getWord() {
        return word;
    }

    public void setIfDone(String ifDone) {
        this.ifDone = ifDone;
    }
    public String getIfDone() {
        return ifDone;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getGrade() {
        return grade;
    }
}
