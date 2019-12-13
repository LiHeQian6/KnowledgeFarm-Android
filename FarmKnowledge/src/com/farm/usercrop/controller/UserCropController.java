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

	//�����û���ֲ������Ϣ
	public void initUserCrop() {
		//��ȡ�ͻ��˷��͵��û�Id
		int userId = getInt("userId");
		
		//�����û�Id���userCropId�б�
		List<Integer> userCropIdList = new UserService().getUserCropIdById(userId);
		UserCropService userCropService =  new UserCropService();
		CropService service = new CropService();
		if(userCropIdList != null) {
			List<UserCropItem> userCropItems = new ArrayList<>();	
			
			for(int userCropId : userCropIdList) {
				//����userCropId��ѯcropId��progress
				UserCropItem item = userCropService.getCropIdProgressByUserCropId(userCropId);
				//����cropId���crop������Ϣ
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
	
	//�鿴�������
	public void getCropProgress() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		int ucId = new UserDao().findUcIdByLand(userId, landNumber);
		renderText(""+new UserCropService().getCropProgress(ucId));
	}

}
