package com.li.knowledgefarm.entity.QuestionEntity;

import java.io.Serializable;

/**
 * @ClassName Completion
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 09:05
 */
public class Completion extends Question implements Serializable {
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
