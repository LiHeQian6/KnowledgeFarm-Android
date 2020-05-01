package com.li.knowledgefarm.entity;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String account;
    private String password;
    private String nickName;
    private String photo;
    private String photoName;
    private String email;
    private int level;
    private long experience;
    private int grade;
    private int money;
    private int mathRewardCount;
    private int englishRewardCount;
    private int chineseRewardCount;
    private int water;
    private int fertilizer;
    private int online;
    private int exist;
    private UserAuthority userAuthority;

    public UserAuthority getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(UserAuthority userAuthority) {
        this.userAuthority = userAuthority;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMathRewardCount() {
        return mathRewardCount;
    }

    public void setMathRewardCount(int mathRewardCount) {
        this.mathRewardCount = mathRewardCount;
    }

    public int getEnglishRewardCount() {
        return englishRewardCount;
    }

    public void setEnglishRewardCount(int englishRewardCount) {
        this.englishRewardCount = englishRewardCount;
    }

    public int getChineseRewardCount() {
        return chineseRewardCount;
    }

    public void setChineseRewardCount(int chineseRewardCount) {
        this.chineseRewardCount = chineseRewardCount;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(int fertilizer) {
        this.fertilizer = fertilizer;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getExist() {
        return exist;
    }

    public void setExist(int exist) {
        this.exist = exist;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", photo='" + photo + '\'' +
                ", photoName='" + photoName + '\'' +
                ", email='" + email + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                ", grade=" + grade +
                ", money=" + money +
                ", water=" + water +
                ", fertilizer=" + fertilizer +
                ", online=" + online +
                ", exist=" + exist +
                '}';
    }
}
