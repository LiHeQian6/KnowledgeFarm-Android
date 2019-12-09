package com.li.knowledgefarm.entity;

/**
 * @auther 孙建旺
 * @description 获取背包种子数量
 * @date 2019/12/09 下午 4:35
 */

public class BagCropNumber{
    private int number;

    private Crop crop;

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }
    public Crop getCrop() {
        return crop;
    }
}
