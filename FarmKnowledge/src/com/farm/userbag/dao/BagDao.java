package com.farm.userbag.dao;

import java.util.ArrayList;
import java.util.List;

import com.farm.entity.BagCropItem;
import com.farm.model.Crop;
import com.farm.model.UserBag;

public class BagDao {
	
	
	/**
	 * 	增
	 * @throws
	 */
	//userbag表添加整条数据
	public boolean addCropToBag(int userId,int cropId,int num) {
		return new UserBag().set("userId", userId).set("cropId", cropId).set("number", num).save();
	}
	
	
	
	/**
	 * 	删
	 * @throws
	 */
	//userbag表删除整条数据
	public boolean deleteCropByUserIdAndCropId(int userId,int cropId) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId = ? and cropId = ?",userId,cropId);
		if(list.size() != 0) {
			return UserBag.dao.deleteById(list.get(0).getInt("id"));
		}
		return false;
	}
	
	
	
	
	/**
	 * 	改
	 * @throws
	 */
	//userbag表修改number
	public boolean updateNumber(int userId,int cropId,int num) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId = ? and cropId = ?",userId,cropId);
		if(list.size()!=0) {
			UserBag bag = list.get(0);
			return bag.set("number", num).update();
		}
		return false;
	}

	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//userbag表判断是否存在该条数据（指定userId、cropId）
	public boolean isExistCrop(int userId, int cropId) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId = ? and cropId = ?",userId,cropId);
		if(list.size()!=0) {
			return true;
		}
		return false;
	}
	//userbag表查询cropId列表（指定userId）
	public List<BagCropItem> getCropIdByUserId(int userId){
		List<UserBag> userBag = UserBag.dao.find("select * from userbag where userId=? order by cropId ASC",userId);
		List<BagCropItem> cropIdList = new ArrayList<BagCropItem>();
		if(userBag.size() != 0) {
			for(UserBag userBag2 : userBag) {
				BagCropItem item = new BagCropItem();
				Crop crop = new Crop();
				crop.set("id", userBag2.getInt("cropId"));
				item.setCrop(crop);
				item.setNumber(userBag2.getInt("number"));
				cropIdList.add(item);
			}
			return cropIdList;
		}
		return null;
	}
	//userbag表查询number（指定userId、cropId）
	public int findNumberByCropId(int userId, int cropId) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId=? and cropId=?",userId,cropId);
		if(list.size() != 0) {
			return list.get(0).getInt("number");
		}
		return 0;
	}


}
