package com.li.knowledgefarm.entity;


/**
 * @program: knowledge_farm
 * @description: 每日任务
 * @author: 景光赞
 * @create: 2020-04-19 11:05
 **/
public class Task {
    private int signIn;
    private int water;
    private int fertilize;
    private int crop;
    private int harvest;
    private int helpWater;
    private int helpFertilize;

    public Task() {
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getSignIn() {
        return signIn;
    }

    public void setSignIn(int signIn) {
        this.signIn = signIn;
    }

    public int getFertilize() {
        return fertilize;
    }

    public void setFertilize(int fertilize) {
        this.fertilize = fertilize;
    }

    public int getCrop() {
        return crop;
    }

    public void setCrop(int crop) {
        this.crop = crop;
    }

    public int getHarvest() {
        return harvest;
    }

    public void setHarvest(int harvest) {
        this.harvest = harvest;
    }

    public int getHelpWater() {
        return helpWater;
    }

    public void setHelpWater(int helpWater) {
        this.helpWater = helpWater;
    }

    public int getHelpFertilize() {
        return helpFertilize;
    }

    public void setHelpFertilize(int helpFertilize) {
        this.helpFertilize = helpFertilize;
    }

}
