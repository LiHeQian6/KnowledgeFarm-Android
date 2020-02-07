package com.farm.userbag.controller;

import java.util.ArrayList;
import java.util.List;

import com.farm.crop.service.CropService;
import com.farm.entity.BagCropItem;
import com.farm.entity.CropItem;
import com.farm.model.Crop;
import com.farm.userbag.service.BagService;
import com.jfinal.core.Controller;

public class BagController extends Controller{
	
	//加载用户背包中所有作物信息（userId）
	public void initUserBag() {
		int userId = getInt("userId");
		
		List<CropItem> cropItemList = new BagService().getCropIdByUserId(userId);
		if(cropItemList != null) {
			List<BagCropItem> list = new ArrayList<>();
			CropService service = new CropService();
			for(CropItem cropItem : cropItemList) {
				Crop crop = service.getUpdateCropInfo(cropItem.getCropId());
				BagCropItem item = new BagCropItem(cropItem.getNumber(),crop);
				list.add(item);
			}
			renderJson(list);
		}else {
			renderJson("[]");
		}
	}

}
