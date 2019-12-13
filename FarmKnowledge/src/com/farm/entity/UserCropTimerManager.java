package com.farm.entity;
import java.util.Timer;
import java.util.TimerTask;

import com.farm.crop.service.CropService;
import com.farm.model.UserCrop;
import com.farm.usercrop.service.UserCropService;

public class UserCropTimerManager {
	private int userCropId;
	private int cropId;
	
    //时间间隔(一天)  
    private static final long PERIOD_DAY = 60 * 60 * 1000;  //24 * 60 * 60 * 1000
    
    public UserCropTimerManager(int userCropId, int cropId) {  
    	this.userCropId = userCropId;
    	this.cropId = cropId;   
    	addProgress();
    }
    
    private void addProgress() {
       	UserCropService userCropService = new UserCropService();
    	CropService cropService = new CropService();
        Timer timer = new Timer();  
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				UserCrop userCrop = userCropService.findUserCropById(userCropId);
				if(userCrop != null) {
					int progress = userCropService.getCropProgress(userCropId);
					int matureTime = cropService.getUpdateCropInfo(cropId).getInt("matureTime");
					if(progress < matureTime) {
						userCropService.waterCrop(userCropId, progress + 1);
					}else {
						timer.cancel();
					}
				}else {
					timer.cancel();
				}
			}
		},0,PERIOD_DAY); 
	}

}
