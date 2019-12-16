package com.li.knowledgefarm.entity;

public class English {
    private int id;
    private String word;
    private String trans;
    private String ifDone = "false";

    public English(String word, String trans) {
        this.word = word;
        this.trans = trans;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getIfDone() {
        return ifDone;
    }

    public void setIfDone(String ifDone) {
        this.ifDone = ifDone;
    }

    @Override
    public String toString() {
        return "English{" +
                "word='" + word + '\'' +
                ", trans='" + trans + '\'' +
                ", ifDone='" + ifDone + '\'' +
                '}';
    }
}
