package com.li.knowledgefarm.entity;

/**
 * @auther 孙建旺
 * @description 数学题字符实体类
 * @date 2019/12/11 上午 9:02
 */

public class MathBean {
    private int result;
    private String signal1;
    private String signal2;
    private int num1;
    private int num3;
    private int num2;
    public void setResult(int result) {
        this.result = result;
    }
    public int getResult() {
        return result;
    }

    public void setSignal1(String signal1) {
        this.signal1 = signal1;
    }
    public String getSignal1() {
        return signal1;
    }

    public void setSignal2(String signal2) {
        this.signal2 = signal2;
    }
    public String getSignal2() {
        return signal2;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }
    public int getNum1() {
        return num1;
    }

    public void setNum3(int num3) {
        this.num3 = num3;
    }
    public int getNum3() {
        return num3;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }
    public int getNum2() {
        return num2;
    }
}
