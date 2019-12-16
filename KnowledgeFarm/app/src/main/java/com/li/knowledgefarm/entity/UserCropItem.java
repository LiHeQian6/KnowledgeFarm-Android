package com.li.knowledgefarm.entity;

import com.li.knowledgefarm.entity.Crop;

public class UserCropItem {
	private int userCropId;
	private Crop crop;
	private int progress;
	private int state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getUserCropId() {
		return userCropId;
	}
	public void setUserCropId(int userCropId) {
		this.userCropId = userCropId;
	}
	public Crop getCrop() {
		return crop;
	}
	public void setCrop(Crop crop) {
		this.crop = crop;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
}
