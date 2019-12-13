package com.farm.usercrop.controller;

import java.util.ArrayList;
import java.util.List;

import com.farm.crop.service.CropService;
import com.farm.entity.UserCropItem;
import com.farm.model.Crop;
import com.farm.user.dao.UserDao;
import com.farm.user.service.UserService;
import com.farm.usercrop.service.UserCropService;
import com.jfinal.core.Controller;

public class UserCropController extends Controller{

	//加载用户种植作物信息
	public void initUserCrop() {
		//获取客户端发送的用户Id
		int userId = getInt("userId");
		
		//根据用户Id获得userCropId列表
		List<Integer> userCropIdList = new UserService().getUserCropIdById(userId);
		UserCropService userCropService =  new UserCropService();
		CropService service = new CropService();
		if(userCropIdList != null) {
			List<UserCropItem> userCropItems = new ArrayList<>();	
			
			for(int userCropId : userCropIdList) {
				//根据userCropId查询cropId、progress
				UserCropItem item = userCropService.getCropIdProgressByUserCropId(userCropId);
				//根据cropId获得crop作物信息
				Crop crop = service.getUpdateCropInfo(item.getUserCropId());
				item.setCrop(crop);
				item.setUserCropId(userCropId);
				userCropItems.add(item);
			}
			renderJson(userCropItems);
		}else {
			renderJson("[]");
		}
		
	}
	
	//查看作物进度
	public void getCropProgress() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		int ucId = new UserDao().findUcIdByLand(userId, landNumber);
		renderText(""+new UserCropService().getCropProgress(ucId));
	}

}
