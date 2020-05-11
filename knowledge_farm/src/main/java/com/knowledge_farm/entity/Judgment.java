package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName Judgment
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 09:07
 */
@Entity
@Table(name = "judgment")
@PrimaryKeyJoinColumn(name = "judgment_id")
public class Judgment extends Question{
    private Integer answer;
    private Integer choice;

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public Integer getChoice() {
        return choice;
    }

    public void setChoice(Integer choice) {
        this.choice = choice;
    }

}
