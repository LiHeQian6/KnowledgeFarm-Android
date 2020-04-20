package com.knowledge_farm.entity;

public class BagCropItem {
	private int number;
	private Crop crop;

	public BagCropItem() {
		super();
	}
	public BagCropItem(int number, Crop crop) {
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
