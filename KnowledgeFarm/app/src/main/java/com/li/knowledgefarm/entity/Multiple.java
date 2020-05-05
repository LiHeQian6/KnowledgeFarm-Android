package com.li.knowledgefarm.entity;

/**
 * @program: knowledge_farm
 * @description: 九九乘法
 * @author: 景光赞
 * @create: 2020-04-30 17:37
 **/
public class Multiple {
    private int num1;
    private String signal;
    private int num2;
    private int num;

    public Multiple() {
    }

    public Multiple(int num1, String signal, int num2, int num) {
        this.num1 = num1;
        this.signal = signal;
        this.num2 = num2;
        this.num = num;
    }

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
