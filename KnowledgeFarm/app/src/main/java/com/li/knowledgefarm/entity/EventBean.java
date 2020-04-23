package com.li.knowledgefarm.entity;

import java.util.List;

/**
 * @auther 孙建旺
 * @description EvenBus
 * @date 2019/12/17 下午 2:17
 */

public class EventBean {
    String account;
    private List<Question3Num> mathList;
    private String message;

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public List<Question3Num> getMathList() {
        return mathList;
    }

    public void setMathList(List<Question3Num> mathList) {
        this.mathList = mathList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
