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
	private int cropId;
	private int count = 0;
	
    private static final long PERIOD_DAY = 2000;  //24 * 60 * 60 * 1000
    
    public UserCropTimerManager(int userCropId, int cropId) {  
    	this.userCropId = userCropId;
    	this.cropId = cropId;   
    	addProgress();
    }
    
    private void addProgress() {
    	Calendar calendar = Calendar.getInstance(); 
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);  
        calendar.set(Calendar.SECOND, second+30); 
        Date date=calendar.getTime(); //第一次执行定时任务的时间  
       	
        Timer timer = new Timer();  
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("run");
				System.out.println("userCropId="+userCropId);
				UserCropService userCropService = new UserCropService();
		    	CropService cropService = new CropService();
				UserCrop userCrop = userCropService.findUserCropById(userCropId);
				System.out.println(userCrop);
				if(userCrop == null) {
					System.out.println("null");
				}
				if(userCrop != null) {
					System.out.println("notNull");
					int progress = userCropService.getCropProgress(userCropId);
					int matureTime = cropService.getUpdateCropInfo(cropId).getInt("matureTime");
					System.out.println("progress:"+progress+"matureTime:"+matureTime);
					if(progress < matureTime) {
						if(count%3 == 0 && count !=0) {
							int suiji = (int)(Math.random()*100);
							System.out.println(suiji);
							if(suiji <= 40) {
								userCropService.updateCropState(userCropId, 0);
								timer.cancel();
							}
						}
						userCropService.waterCrop(userCropId, progress + 1);
					}else {
						timer.cancel();
					}
				}else {
					System.out.println("timer cancel");
					timer.cancel();
				}
				count ++;
			}
		},date,PERIOD_DAY); 
	}

}
