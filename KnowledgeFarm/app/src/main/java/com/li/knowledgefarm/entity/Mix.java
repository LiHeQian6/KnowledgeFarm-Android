package com.li.knowledgefarm.entity;

import java.io.Serializable;

/**
 * @program: knowledge_farm
 * @description: 四则混合运算
 * @author: 景光赞
 * @create: 2020-05-01 15:39
 **/
public class Mix implements Serializable {
    private int num1;
    private String signal1;
    private int num2;
    private String signal2;
    private int num3;
    private String brackets1;
    private String brackets2;
    private int num;

    public Mix() {
    }

    public Mix(int num1, String signal1, int num2, String signal2, int num3,String brackets1,
               String brackets2,int num) {
        this.num1 = num1;
        this.signal1 = signal1;
        this.num2 = num2;
        this.signal2 = signal2;
        this.num3 = num3;
        this.brackets1 = brackets1;
        this.brackets2 = brackets2;
        this.num = num;
    }

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public String getSignal1() {
        return signal1;
    }

    public void setSignal1(String signal1) {
        this.signal1 = signal1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public String getSignal2() {
        return signal2;
    }

    public void setSignal2(String signal2) {
        this.signal2 = signal2;
    }

    public int getNum3() {
        return num3;
    }

    public void setNum3(int num3) {
        this.num3 = num3;
    }

    public String getBrackets1() {
        return brackets1;
    }

    public void setBrackets1(String brackets1) {
        this.brackets1 = brackets1;
    }

    public String getBrackets2() {
        return brackets2;
    }

    public void setBrackets2(String brackets2) {
        this.brackets2 = brackets2;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        if (brackets1.equals("false")&&brackets2.equals("false")){
            return num1 + signal1 +num2 +signal2 +num3 + " = ";
        }else if(brackets1.equals("true")&&brackets2.equals("false")){
            return "("+num1 + signal1 + num2 +")" + signal2 + num3 + " = ";
        }else if(brackets1.equals("false")&&brackets2.equals("true")){
            return num1 + signal1 + "("+ num2 + signal2 + num3+")"  + " = ";
        }else {
            return "null";
        }
    }
}
