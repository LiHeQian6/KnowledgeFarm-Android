package com.farm.userbag.controller;

import java.util.ArrayList;
import java.util.List;

import com.farm.crop.service.CropService;
import com.farm.model.Crop;
import com.farm.userbag.service.BagService;
import com.jfinal.core.Controller;

public class BagController extends Controller{
	
	//加载用户背包
	public void initUserBag() {
		int userId = getInt("userId");
		
		List<Integer> cropIdList = new BagService().getCropIdByUserId(userId);
		if(cropIdList != null) {
			List<Crop> list = new ArrayList<Crop>();
			CropService service = new CropService();
			for(int cropId : cropIdList) {
				Crop crop = service.getUpdateCropInfo(cropId);
				list.add(crop);
			}
			renderJson(list);
		}else {
			renderJson("[]");
		}
		
		
		
	}

}
