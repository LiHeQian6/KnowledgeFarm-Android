package com.farm.usercrop.controller;

import java.util.ArrayList;
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
		List<Map<Integer,Crop>> cropList = new ArrayList<Map<Integer,Crop>>();		
		UserCropService userCropService =  new UserCropService();
		CropService service = new CropService();
		
		for(int userCropId : userCropIdList) {
			if(userCropId != -1 && userCropId != 0) { 
				Map<Integer,Crop> map = new HashMap<Integer, Crop>();
				//����userCropId��ѯcropId
				int cropId = userCropService.getCropIdByUserCropId(userCropId);
				//����cropId���crop������Ϣ
				Crop crop = service.getUpdateCropInfo(cropId);
				map.put(userCropId, crop);
				cropList.add(map);
			}else { //�������δ����(-1)����δ��ֲ����(0)����Crop�������Ϊnull
				Map<Integer,Crop> map = new HashMap<Integer, Crop>();
				map.put(userCropId, null);
			}
		}
		
		renderJson(cropList);
	}
	
	//�鿴�������
	public void getCropProgress() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		renderText(""+new UserCropService().getCropProgress(userId, landNumber));
	}

}
