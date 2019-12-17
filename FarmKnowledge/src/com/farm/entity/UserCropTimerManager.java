package com.farm.entity;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.farm.crop.service.CropService;
import com.farm.model.UserCrop;
import com.farm.usercrop.service.UserCropService;

public class UserCropTimerManager {
	private int userCropId;
	private int count = 0;
	
    private static final long PERIOD_HOUR = 60 * 60 * 1000;  //24 * 60 * 60 * 1000
    
    public UserCropTimerManager(int userCropId) {  
    	this.userCropId = userCropId;
    	addProgress();
    }
    
    private void addProgress() {
    	Calendar calendar = Calendar.getInstance(); 
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int nextHour;
        
        if(hour == 23) {
        	nextHour = 0;
        }else {
        	nextHour = hour + 1;
        }
        
        calendar.set(Calendar.HOUR_OF_DAY, nextHour);
        calendar.set(Calendar.MINUTE, minute);  
        calendar.set(Calendar.SECOND, second); 
        Date date=calendar.getTime();
       	
        Timer timer = new Timer();  
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				UserCropService userCropService = new UserCropService();
		    	CropService cropService = new CropService();
				UserCrop userCrop = userCropService.findUserCropById(userCropId);
				if(userCrop != null) {
					int progress = userCrop.getInt("progress");
					int matureTime = cropService.getUpdateCropInfo(userCrop.getInt("cropId")).getInt("matureTime");
					if(progress < matureTime) {
						if(count%3 == 0 && count != 0) {
							int suiji = (int)(Math.random()*100);
							System.out.println(suiji);
							if(suiji <= 25) {
								userCropService.updateCropState(userCropId, 0);
								timer.cancel();
							}
						}
						userCropService.waterCrop(userCropId, progress + 1);
					}else {
						timer.cancel();
					}
				}else {
					timer.cancel();
				}
				count ++;
			}
		},date,PERIOD_HOUR); 
	}

}
