package com.li.knowledgefarm.entity;

import java.io.Serializable;

/**
 * @auther 孙建旺
 * @description 语文题实体类
 * @date 2019/12/17 下午 3:35
 */

public class Chinese implements Serializable {
    private String quantify;
    private int id;
    private String word;
    private String ifDone;
    public void setQuantify(String quantify) {
        this.quantify = quantify;
    }
    public String getQuantify() {
        return quantify;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public String getWord() {
        return word;
    }
    public String getIfDone() {
        return ifDone;
    }
    public void setIfDone(String ifDone) {
        this.ifDone = ifDone;
    }
}
