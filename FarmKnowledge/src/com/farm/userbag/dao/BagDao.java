package com.farm.userbag.dao;

import java.util.ArrayList;
import java.util.List;

import com.farm.entity.BagCropItem;
import com.farm.entity.CropItem;
import com.farm.model.Crop;
import com.farm.model.UserBag;

public class BagDao {
	
	
	/**
	 * 	增
	 * @throws
	 */
	//添加种子到背包（UserBag）
	public boolean addCropToBag(int userId,int cropId,int num) {
		return new UserBag().set("userId", userId).set("cropId", cropId).set("number", num).save();
	}
	
	
	
	/**
	 * 	删
	 * @throws
	 */
	
	
	
	
	/**
	 * 	改
	 * @throws
	 */
	//增加指定作物的数量（UserBag）
	public boolean addCropNumber(int userId,int cropId,int num) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId = ? and cropId = ?",userId,cropId);
		if(list.size()!=0) {
			UserBag bag = list.get(0);
			return bag.set("number", bag.getInt("number")+num).update();
		}
		return false;
	}
	//减少指定作物一个（UserBag）
	public boolean decreaseOneCrop(int userId,int cropId) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId = ? and cropId = ?",userId,cropId);
		if(list.size()!=0) {
			UserBag bag = list.get(0);
			return bag.set("number", bag.getInt("number")-1).update();
		}
		return false;
	}
	//使用背包中种子
	public boolean lessCropInBag(int userId,int cropId) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId = ? and cropId = ?",userId,cropId);
		if(list.size()!=0) {
			UserBag bag = list.get(0);
			if(bag.getInt("number")==1) {
				return UserBag.dao.deleteById(bag.getInt("id"));
			}else {
				return bag.set("number", bag.getInt("number")-1).update();
			}
		}
		return false;
	}
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//查询背包是否存在该作物
	public boolean isExistCrop(int userId, int cropId) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId = ? and cropId = ?",userId,cropId);
		if(list.size()!=0) {
			return true;
		}
		return false;
	}
	//根据userId查询用户背包cropId列表
	public List<CropItem> getCropIdByUserId(int userId){
		List<UserBag> userBag = UserBag.dao.find("select * from userbag where userId=? order by cropId ASC",userId);
		List<CropItem> cropIdList = new ArrayList<CropItem>();
		if(userBag.size() != 0) {
			for(UserBag userBag2 : userBag) {
				CropItem item = new CropItem(userBag2.getInt("cropId"),userBag2.getInt("number"));
				cropIdList.add(item);
			}
			return cropIdList;
		}
		return null;
	}
	


}
