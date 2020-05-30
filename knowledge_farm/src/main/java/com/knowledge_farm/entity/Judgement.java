package com.knowledge_farm.entity;

import javax.persistence.*;

/**
 * @ClassName Judgment
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 09:07
 */
@Entity
@Table(name = "judgement")
@PrimaryKeyJoinColumn(name = "judgement_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
public class Judgement extends Question{
    private Integer answer;

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

}
