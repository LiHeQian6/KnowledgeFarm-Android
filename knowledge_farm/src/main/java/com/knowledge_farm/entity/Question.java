package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName Question
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 08:53
 */
@Entity
@Table(name = "question")
@Inheritance(strategy = InheritanceType.JOINED)
public class Question {
    private Integer id;
    private QuestionTitle questionTitle;
    private String subject;
    private QuestionType questionType;
    private Integer grade;
    private Integer ifDone;

    public Question(){

    }
    public Question(QuestionTitle title,String subject,QuestionType type,Integer grade){
        this.questionTitle = title;
        this.subject = subject;
        this.questionType = type;
        this.grade = grade;
        this.ifDone = 0;
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

    @OneToOne(mappedBy = "question",cascade = CascadeType.ALL)
    @org.hibernate.annotations.ForeignKey(name = "none")
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

    @ManyToOne
    @JoinColumn(name = "question_type_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
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

    @Column(name = "if_done")
    public Integer getIfDone() {
        return ifDone;
    }

    public void setIfDone(Integer ifDone) {
        this.ifDone = ifDone;
    }

}
