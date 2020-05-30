package com.knowledge_farm.entity;

import javax.persistence.*;

/**
 * @ClassName Completion
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 09:05
 */
@Entity
@Table(name = "completion")
@PrimaryKeyJoinColumn(name = "completion_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
public class Completion extends Question{
    private String answer;

    public Completion() {
    }

    public Completion(QuestionTitle title, String subject, QuestionType type, Integer grade, String answer) {
        super(title, subject, type, grade);
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
