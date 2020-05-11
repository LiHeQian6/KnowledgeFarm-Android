package com.li.knowledgefarm.entity;

/**
 * @ClassName Judgment
 * @Description
 * @Author 张帅华
 * @Date 2020-05-09 09:07
 */
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
