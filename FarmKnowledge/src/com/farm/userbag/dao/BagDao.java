package com.farm.userbag.dao;

import java.util.ArrayList;
import java.util.List;

import com.farm.entity.BagCropItem;
import com.farm.entity.CropItem;
import com.farm.model.Crop;
import com.farm.model.UserBag;

public class BagDao {
	
	
	/**
	 * 	��
	 * @throws
	 */
	//������ӵ�������UserBag��
	public boolean addCropToBag(int userId,int cropId,int num) {
		return new UserBag().set("userId", userId).set("cropId", cropId).set("number", num).save();
	}
	
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//����ָ�������������UserBag��
	public boolean addCropNumber(int userId,int cropId,int num) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId = ? and cropId = ?",userId,cropId);
		if(list.size()!=0) {
			UserBag bag = list.get(0);
			return bag.set("number", bag.getInt("number")+num).update();
		}
		return false;
	}
	//����ָ������һ����UserBag��
	public boolean decreaseOneCrop(int userId,int cropId) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId = ? and cropId = ?",userId,cropId);
		if(list.size()!=0) {
			UserBag bag = list.get(0);
			return bag.set("number", bag.getInt("number")-1).update();
		}
		return false;
	}
	//ʹ�ñ���������
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
	 * 	��
	 * @throws
	 */
	//��ѯ�����Ƿ���ڸ�����
	public boolean isExistCrop(int userId, int cropId) {
		List<UserBag> list = UserBag.dao.find("select * from userbag where userId = ? and cropId = ?",userId,cropId);
		if(list.size()!=0) {
			return true;
		}
		return false;
	}
	//����userId��ѯ�û�����cropId�б�
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
