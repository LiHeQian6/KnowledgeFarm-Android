package com.farm.usercrop.dao;

import com.farm.entity.UserCropItem;
import com.farm.model.UserCrop;

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
	//浇水
	public boolean waterCrop(int ucId, int progress) {
		UserCrop uc = UserCrop.dao.findById(ucId);
		if(uc!=null) {
			return uc.set("progress", progress).update();
		}
		return false;
	}
	
	//施肥
	public boolean fertilizerCrop(int ucId, int progress) {
		UserCrop uc = UserCrop.dao.findById(ucId);
		if(uc!=null) {
			return uc.set("progress", progress).update();
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
	
	//根据userCropId查询cropId、progress（放到UserCropItem中cropId当成userCropId）
	public UserCropItem getCropIdProgressByUserCropId(int id){
		UserCrop userCrop = UserCrop.dao.findById(id);
		if(userCrop != null) {
			UserCropItem item = new UserCropItem();
			item.setUserCropId(userCrop.getInt("cropId"));
			item.setProgress(userCrop.getInt("progress"));
			return item;
		}
		return null;
	}
	
	//删除已收获的作物
	public boolean deleteCrop(int ucId) {
		boolean succeed = UserCrop.dao.deleteById(ucId);
		return succeed;
	}
	
	//根据userCropId查询整条信息
	public UserCrop findUserCropById(int ucId) {
		UserCrop userCrop = UserCrop.dao.findById(ucId);
		if(userCrop != null) {
			return userCrop;
		}
		return null;
	}
	
	//修改土地状态
	public boolean updateCropState(int userCropId, int state) {
		UserCrop userCrop = UserCrop.dao.findById(userCropId);
		if(userCrop != null) {
			return userCrop.set("state", state).update();
		}
		return false;
	}
}
