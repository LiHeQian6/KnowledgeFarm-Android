package com.farm.usercrop.dao;

import com.farm.entity.UserCropItem;
import com.farm.model.UserCrop;

public class UserCropDao {
	
	
	/**
	 * 	��
	 * @throws
	 */
	
	
	
	
	/**
	 * ɾ
	 * @throws
	 */
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//��ˮ
	public boolean waterCrop(int ucId, int progress) {
		UserCrop uc = UserCrop.dao.findById(ucId);
		if(uc!=null) {
			return uc.set("progress", progress).update();
		}
		return false;
	}
	
	//ʩ��
	public boolean fertilizerCrop(int ucId, int progress) {
		UserCrop uc = UserCrop.dao.findById(ucId);
		if(uc!=null) {
			return uc.set("progress", progress).update();
		}
		return false;
	}
	//�鿴�������
	public int getCropProgress(int ucId) {
		UserCrop userCrop = UserCrop.dao.findById(ucId);
		if(userCrop != null) {
			return userCrop.getInt("progress");
		}
		return 0;
	}
	//��ֲ������,,,(û�п��ǵ�ǰ�����Ƿ���������ռ�ã���ǰ�˿���QAQ��
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
	
	//����userCropId��ѯcropId
	public int getCropIdByUserCropId(int id){
		UserCrop userCrop = UserCrop.dao.findById(id);
		if(userCrop != null) {
			return userCrop.getInt("cropId");
		}
		return 0;
	}
	
	//����userCropId��ѯcropId��progress���ŵ�UserCropItem��cropId����userCropId��
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
	
	//ɾ�����ջ������
	public boolean deleteCrop(int ucId) {
		boolean succeed = UserCrop.dao.deleteById(ucId);
		return succeed;
	}
	
	//����userCropId��ѯ������Ϣ
	public UserCrop findUserCropById(int ucId) {
		UserCrop userCrop = UserCrop.dao.findById(ucId);
		if(userCrop != null) {
			return userCrop;
		}
		return null;
	}
	
	//�޸�����״̬
	public boolean updateCropState(int userCropId, int state) {
		UserCrop userCrop = UserCrop.dao.findById(userCropId);
		if(userCrop != null) {
			return userCrop.set("state", state).update();
		}
		return false;
	}
}
