package com.li.knowledgefarm.entity;

/**
 * @ClassName QuestionTitle
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 17:28
 */
public class QuestionTitle {
    private Integer id;
    private String title;
    private Question question;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
