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

	//�����û���ֲ������Ϣ
	public void initUserCrop() {
		//��ȡ�ͻ��˷��͵��û�Id
		int userId = getInt("userId");
		
		//�����û�Id���userCropId�б�
		List<Integer> userCropIdList = new UserService().getUserCropIdById(userId);
		if(userCropIdList == null) {
			Map<Integer,Crop> cropMap = new HashMap<>();	
			UserCropService userCropService =  new UserCropService();
			CropService service = new CropService();
			
			for(int userCropId : userCropIdList) {
				//����userCropId��ѯcropId
				int cropId = userCropService.getCropIdByUserCropId(userCropId);
				//����cropId���crop������Ϣ
				Crop crop = service.getUpdateCropInfo(cropId);
				cropMap.put(userCropId, crop);
			}
			renderJson(cropMap);
		}else {
			renderJson("{}");
		}
		
	}
	
	//�鿴�������
	public void getCropProgress() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		renderText(""+new UserCropService().getCropProgress(userId, landNumber));
	}

}
