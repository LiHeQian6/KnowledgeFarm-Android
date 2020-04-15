package com.atguigu.farm.entity;

public class BagItem {
	private int number;
	private Crop crop;

	public BagItem() {
		super();
	}
	public BagItem(int number, Crop crop) {
		super();
		this.number = number;
		this.crop = crop;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public Crop getCrop() {
		return crop;
	}
	public void setCrop(Crop crop) {
		this.crop = crop;
	}
	
}
