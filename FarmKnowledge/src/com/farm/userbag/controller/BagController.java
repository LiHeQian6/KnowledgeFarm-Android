package com.farm.userbag.controller;

import java.util.List;

import com.farm.crop.service.CropService;
import com.farm.entity.BagCropItem;
import com.farm.model.Crop;
import com.farm.userbag.service.BagService;
import com.jfinal.core.Controller;

public class BagController extends Controller{
	
	//加载用户背包中所有作物信息（userId）
	public void initUserBag() {
		int userId = getInt("userId");
		
		List<BagCropItem> bagCropItems = new BagService().getCropIdByUserId(userId);
		CropService service = new CropService();
		if(bagCropItems != null) {
			for(int i = 0;i < bagCropItems.size();i++) {
				BagCropItem item = new BagCropItem();
				Crop crop = service.getUpdateCropInfo(bagCropItems.get(i).getCrop().getInt("id"));
				item.setCrop(crop);
				item.setNumber(bagCropItems.get(i).getNumber());
				bagCropItems.set(i, item);
			}
			renderJson(bagCropItems);
		}else {
			renderJson("[]");
		}
	}

}
