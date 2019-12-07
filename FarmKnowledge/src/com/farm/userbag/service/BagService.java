package com.farm.userbag.service;

import java.sql.SQLException;
import java.util.List;

import com.farm.crop.service.CropService;
import com.farm.user.service.UserService;
import com.farm.userbag.dao.BagDao;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class BagService {
	
	/**
	 * 	��
	 * @throws
	 */
	//�����������ӵ�������UserBag��
	public boolean addToBag(int userId,int cropId,int num) {
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				int money = num*new CropService().getUpdateCropInfo(cropId).getInt("price");
				boolean a1 = new BagDao().addCropToBag(userId, cropId, num);
				boolean a2 = new UserService().decreaseMoney(userId, money);
				if(a1 && a2) {
					return true;
				}
				return false;
			}
		});
		return succeed;
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
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				int money = num*new CropService().getUpdateCropInfo(cropId).getInt("price");
				boolean a1 = new BagDao().addCropNumber(userId, cropId, num);
				boolean a2 = new UserService().decreaseMoney(userId, money);
				if(a1 && a2) {
					return true;
				}
				return false;
			}
		});
		return succeed;
	}
	//����ָ������һ����UserBag��
	public boolean decreaseOneCrop(int userId,int cropId) {
		return new BagDao().decreaseOneCrop(userId, cropId);
	}
	//ʹ�ñ���������
	public boolean lessCropBag(int userId,int cropId) {
		return new BagDao().lessCropInBag(userId, cropId);
	}
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//��ѯ�����Ƿ���ڸ�����
	public boolean isExistCrop(int userId, int cropId) {
		return new BagDao().isExistCrop(userId, cropId);
	}
	//����userId��ѯ�û�����cropId�б�
	public List<Integer> getCropIdByUserId(int userId){
		return new BagDao().getCropIdByUserId(userId);
	}
	
}
