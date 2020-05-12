package com.li.knowledgefarm.entity.QuestionEntity;

import com.li.knowledgefarm.entity.QuestionEntity.Question;

/**
 * @ClassName SingleChoice
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 08:59
 */
public class SingleChoice extends Question {
    private String answer;
    private String choice1;
    private String choice2;
    private String choice3;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

}
