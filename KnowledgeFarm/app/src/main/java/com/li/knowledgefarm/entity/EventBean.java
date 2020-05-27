package com.li.knowledgefarm.entity;

import com.li.knowledgefarm.entity.QuestionEntity.Question3Num;

import java.util.List;

/**
 * @auther 孙建旺
 * @description EvenBus
 * @date 2019/12/17 下午 2:17
 */

public class EventBean {
    String account;
    private List<Question3Num> mathList;
    private PetVO petVO;
    private Boolean ifRead = false;
    private String message;
    private String notifyType;
    private String result;

    public Boolean getIfRead() {
        return ifRead;
    }

    public void setIfRead(Boolean ifRead) {
        this.ifRead = ifRead;
    }

    public PetVO getPetVO() {
        return petVO;
    }

    public void setPetVO(PetVO petVO) {
        this.petVO = petVO;
    }

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

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
