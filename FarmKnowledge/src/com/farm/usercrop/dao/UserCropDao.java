package com.farm.usercrop.dao;

import java.util.List;

import com.farm.crop.service.CropService;
import com.farm.model.Crop;
import com.farm.model.User;
import com.farm.model.UserCrop;
import com.farm.user.service.UserService;

public class UserCropDao {
	
	
	/**
	 * 	增
	 * @throws
	 */
	
	
	
	
	/**
	 * 删
	 * @throws
	 */
	
	
	
	
	/**
	 * 	改
	 * @throws
	 */
	
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//浇水(参数:usercropId,userId)
	public boolean waterCrop(int ucId) {
		UserCrop uc = UserCrop.dao.findById(ucId);
		if(uc!=null) {
			if(uc.getInt("progress")<=95) {
				return uc.set("progress", uc.getInt("progress")+5).update();
			}
		}
		return false;
	}
	
	//施肥(参数:usercropId,userId)
	public boolean fertilizerCrop(int ucId) {
		UserCrop uc = UserCrop.dao.findById(ucId);
		if(uc!=null) {
			if(uc.getInt("progress")<=90) {
				return uc.set("progress", uc.getInt("progress")+10).update();
			}
		}
		return false;
	}
	//查看作物进度
	public int getCropProgress(int ucId) {
		UserCrop userCrop = UserCrop.dao.findById(ucId);
		if(userCrop != null) {
			return userCrop.getInt("progress");
		}
		return 0;
	}
	//种植新作物,,,(没有考虑当前土地是否已有作物占用，由前端考虑QAQ）
	public int addNewCrop(int cropId) {
		UserCrop uc = new UserCrop();
		uc.set("cropId", cropId);
		uc.set("progress", 0);
		boolean succeed = uc.save();
		if(succeed) {
			return uc.getInt("id");
		}
		return 0;
	}
	
	//根据userCropId查询cropId
	public int getCropIdByUserCropId(int id){
		UserCrop userCrop = UserCrop.dao.findById(id);
		if(userCrop != null) {
			return userCrop.getInt("cropId");
		}
		return 0;
	}
	//删除已收获的作物
	public boolean deleteCrop(int ucId) {
		boolean succeed = UserCrop.dao.deleteById(ucId);
		return succeed;
	}
}
