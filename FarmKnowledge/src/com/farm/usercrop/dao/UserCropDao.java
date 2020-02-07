package com.farm.usercrop.dao;

import java.util.List;

import com.farm.entity.UserCropItem;
import com.farm.model.UserCrop;

public class UserCropDao {
	
	
	/**
	 * 	��
	 * @throws
	 */
	//usercrop���������(��ǰ�����Ƿ�����ֲ������ǰ���жϣ�
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
	
	
	
	/**
	 * ɾ
	 * @throws
	 */
	//usercrop��ɾ����������
	public boolean deleteCrop(int id) {
		boolean succeed = UserCrop.dao.deleteById(id);
		return succeed;
	}
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//usercrop���޸�progress��ָ��id��
	public boolean updateProgress(int id, int progress) {
		UserCrop uc = UserCrop.dao.findById(id);
		if(uc != null) {
			return uc.set("progress", progress).update();
		}
		return false;
	}
	//usercrop���޸�state
	public boolean updateCropState(int id, int state) {
		UserCrop userCrop = UserCrop.dao.findById(id);
		if(userCrop != null) {
			return userCrop.set("state", state).update();
		}
		return false;
	}

	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//usercrop���ѯ���ݣ�ָ��id��
	public UserCrop findUserCropById(int id) {
		List<UserCrop> list = UserCrop.dao.find("select * from usercrop where id=?",id);
		if(list.size() != 0) {
			return list.get(0);
		}
		return null;
	}
	//usercrop�����id��ѯcropId��progress���ŵ�UserCropItem��cropId����userCropId��
	public UserCropItem getCropIdProgressStateByUserCropId(int id){
		UserCrop userCrop = UserCrop.dao.findById(id);
		if(userCrop != null) {
			UserCropItem item = new UserCropItem();
			item.setUserCropId(userCrop.getInt("cropId"));
			item.setProgress(userCrop.getInt("progress"));
			item.setState(userCrop.getInt("state"));
			return item;
		}
		return null;
	}

}
