package com.farm.usercrop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.farm.crop.service.CropService;
import com.farm.model.Crop;
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
		if(userCropIdList == null) {
			Map<Integer,Crop> cropMap = new HashMap<>();	
			UserCropService userCropService =  new UserCropService();
			CropService service = new CropService();
			
			for(int userCropId : userCropIdList) {
				//根据userCropId查询cropId
				int cropId = userCropService.getCropIdByUserCropId(userCropId);
				//根据cropId获得crop作物信息
				Crop crop = service.getUpdateCropInfo(cropId);
				cropMap.put(userCropId, crop);
			}
			renderJson(cropMap);
		}else {
			renderJson("{}");
		}
		
	}
	
	//查看作物进度
	public void getCropProgress() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		renderText(""+new UserCropService().getCropProgress(userId, landNumber));
	}

}
