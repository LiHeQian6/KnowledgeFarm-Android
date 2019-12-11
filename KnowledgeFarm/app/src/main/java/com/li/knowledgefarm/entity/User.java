package com.li.knowledgefarm.entity;

public class User {
    private int id;
    private String accout;
    private String password;
    private String nickName;
    private String photo;
    private String photoName;
    private String email;
    private int level;
    private long experience;
    private int grade;
    private int money;
    private int rewardCount;
    private int water;
    private int fertilizer;
    private int online;
    private int exist;
    private int land1;
    private int land2;
    private int land3;
    private int land4;
    private int land5;
    private int land6;
    private int land7;
    private int land8;
    private int land9;
    private int land10;
    private int land11;
    private int land12;
    private int land13;
    private int land14;
    private int land15;
    private int land16;
    private int land17;
    private int land18;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getRewardCount() {
        return rewardCount;
    }

    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
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

    public String getAccout() {
        return accout;
    }

    public void setAccout(String accout) {
        this.accout = accout;
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

    public int getLand1() {
        return land1;
    }

    public void setLand1(int land1) {
        this.land1 = land1;
    }

    public int getLand2() {
        return land2;
    }

    public void setLand2(int land2) {
        this.land2 = land2;
    }

    public int getLand3() {
        return land3;
    }

    public void setLand3(int land3) {
        this.land3 = land3;
    }

    public int getLand4() {
        return land4;
    }

    public void setLand4(int land4) {
        this.land4 = land4;
    }

    public int getLand5() {
        return land5;
    }

    public void setLand5(int land5) {
        this.land5 = land5;
    }

    public int getLand6() {
        return land6;
    }

    public void setLand6(int land6) {
        this.land6 = land6;
    }

    public int getLand7() {
        return land7;
    }

    public void setLand7(int land7) {
        this.land7 = land7;
    }

    public int getLand8() {
        return land8;
    }

    public void setLand8(int land8) {
        this.land8 = land8;
    }

    public int getLand9() {
        return land9;
    }

    public void setLand9(int land9) {
        this.land9 = land9;
    }

    public int getLand10() {
        return land10;
    }

    public void setLand10(int land10) {
        this.land10 = land10;
    }

    public int getLand11() {
        return land11;
    }

    public void setLand11(int land11) {
        this.land11 = land11;
    }

    public int getLand12() {
        return land12;
    }

    public void setLand12(int land12) {
        this.land12 = land12;
    }

    public int getLand13() {
        return land13;
    }

    public void setLand13(int land13) {
        this.land13 = land13;
    }

    public int getLand14() {
        return land14;
    }

    public void setLand14(int land14) {
        this.land14 = land14;
    }

    public int getLand15() {
        return land15;
    }

    public void setLand15(int land15) {
        this.land15 = land15;
    }

    public int getLand16() {
        return land16;
    }

    public void setLand16(int land16) {
        this.land16 = land16;
    }

    public int getLand17() {
        return land17;
    }

    public void setLand17(int land17) {
        this.land17 = land17;
    }

    public int getLand18() {
        return land18;
    }

    public void setLand18(int land18) {
        this.land18 = land18;
    }

    public void setLandStauts(int landPosition,int status){
        switch (landPosition){
            case 1:
                land1 = status;
                break;
            case 2:
                land2 = status;
                break;
            case 3:
                land3 = status;
                break;
            case 4:
                land4 = status;
                break;
            case 5:
                land5 = status;
                break;
            case 6:
                land6 = status;
                break;
            case 7:
                land7 = status;
                break;
            case 8:
                land8 = status;
                break;
            case 9:
                land9 = status;
                break;
            case 10:
                land10 = status;
                break;
            case 11:
                land11 = status;
                break;
            case 12:
                land12 = status;
                break;
            case 13:
                land13 = status;
                break;
            case 14:
                land14 = status;
                break;
            case 15:
                land15 = status;
                break;
            case 16:
                land16 = status;
                break;
            case 17:
                land17 = status;
                break;
            case 18:
                land18 = status;
                break;
        }
    }

    public int getLandStauts(int land) {
        int status=-1;
        switch (land){
            case 1:
                status=land1;
                break;
            case 2:
                status=land2;
                break;
            case 3:
                status=land3;
                break;
            case 4:
                status=land4;
                break;
            case 5:
                status=land5;
                break;
            case 6:
                status=land6;
                break;
            case 7:
                status=land7;
                break;
            case 8:
                status=land8;
                break;
            case 9:
                status=land9;
                break;
            case 10:
                status=land10;
                break;
            case 11:
                status=land11;
                break;
            case 12:
                status=land12;
                break;
            case 13:
                status=land13;
                break;
            case 14:
                status=land14;
                break;
            case 15:
                status=land15;
                break;
            case 16:
                status=land16;
                break;
            case 17:
                status=land17;
                break;
            case 18:
                status=land18;
                break;
        }
        return status;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", accout='" + accout + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", photo='" + photo + '\'' +
                ", photoName='" + photoName + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                ", grade=" + grade +
                ", money=" + money +
                ", rewardCount=" + rewardCount +
                ", water=" + water +
                ", fertilizer=" + fertilizer +
                ", online=" + online +
                ", exist=" + exist +
                ", land1=" + land1 +
                ", land2=" + land2 +
                ", land3=" + land3 +
                ", land4=" + land4 +
                ", land5=" + land5 +
                ", land6=" + land6 +
                ", land7=" + land7 +
                ", land8=" + land8 +
                ", land9=" + land9 +
                ", land10=" + land10 +
                ", land11=" + land11 +
                ", land12=" + land12 +
                ", land13=" + land13 +
                ", land14=" + land14 +
                ", land15=" + land15 +
                ", land16=" + land16 +
                ", land17=" + land17 +
                ", land18=" + land18 +
                '}';
    }
}
