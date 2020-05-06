package com.li.knowledgefarm.entity;


import java.io.Serializable;

/**
 * @program: knowledge_farm
 * @description: 数学23年级
 * @author: 景光赞
 * @create: 2020-04-20 17:11
 **/

public class Math23 implements Serializable {
    private int id;
    private String question;
    private String answer;
    private String answer2;
    private String answer3;
    private String grade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
