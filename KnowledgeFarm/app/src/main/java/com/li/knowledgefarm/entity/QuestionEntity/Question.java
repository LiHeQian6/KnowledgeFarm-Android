package com.li.knowledgefarm.entity.QuestionEntity;

import java.io.Serializable;

/**
 * @ClassName Question
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 08:53
 */
public class Question implements Serializable {
    private Integer id;
    private QuestionTitle questionTitle;
    private String subject;
    private QuestionType questionType;
    private Integer grade;
    private Integer ifDone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public QuestionTitle getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(QuestionTitle questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getIfDone() {
        return ifDone;
    }

    public void setIfDone(Integer ifDone) {
        this.ifDone = ifDone;
    }

}
