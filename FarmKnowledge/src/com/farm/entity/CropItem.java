package com.farm.entity;

public class CropItem {
	private int cropId;
	private int number;
	
	public CropItem() {
		super();
	}
	public CropItem(int cropId, int number) {
		super();
		this.cropId = cropId;
		this.number = number;
	}
	public int getCropId() {
		return cropId;
	}
	public void setCropId(int cropId) {
		this.cropId = cropId;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

}
