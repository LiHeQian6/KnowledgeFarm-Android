package com.li.knowledgefarm.entity;

public class UserCropItem {
	private int userCropId;
	private Crop crop;
	private int progress;
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
