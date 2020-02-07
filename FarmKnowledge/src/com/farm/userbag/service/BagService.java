package com.farm.userbag.service;

import java.sql.SQLException;
import java.util.List;

import com.farm.crop.service.CropService;
import com.farm.entity.BagCropItem;
import com.farm.user.service.UserService;
import com.farm.userbag.dao.BagDao;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class BagService {
	
	/**
	 * 	��
	 * @throws
	 */

	
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	//ɾ��ָ������
	public boolean deleteCropByUserIdAndCropId(int userId,int cropId) {
		return new BagDao().deleteCropByUserIdAndCropId(userId, cropId);
	}
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//��ѯ�û������Ƿ���ڸ����ָ��userId��cropId��
	public boolean isExistCrop(int userId, int cropId) {
		return new BagDao().isExistCrop(userId, cropId);
	}
	//��ѯ�û�����cropId�б�ָ��userId��
	public List<BagCropItem> getCropIdByUserId(int userId){
		return new BagDao().getCropIdByUserId(userId);
	}
	//��ѯָ�������ʣ��������ָ��userId��cropId��
	public int findNumberByCropId(int userId, int cropId) {
		return new BagDao().findNumberByCropId(userId, cropId);
	}
	
	
	
	/**
	 * 	����
	 * @throws
	 */
	//������������в����ڴ����
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
	//��������������Ѵ��ڴ����
	public boolean addCropNumber(int userId,int cropId,int num) {
		BagDao bagDao = new BagDao();
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				int money = num*new CropService().getUpdateCropInfo(cropId).getInt("price");
				int number = bagDao.findNumberByCropId(userId, cropId);
				boolean a1 = bagDao.updateNumber(userId, cropId, number+num);
				boolean a2 = new UserService().decreaseMoney(userId, money);
				if(a1 && a2) {
					return true;
				}
				return false;
			}
		});
		return succeed;
	}
	//ָ��������������һ��
	public boolean decreaseOneCrop(int userId,int cropId) {
		BagDao bagDao = new BagDao();
		int number = bagDao.findNumberByCropId(userId, cropId);
		if(number == 1) {
			return bagDao.deleteCropByUserIdAndCropId(userId, cropId);
		}
		return bagDao.updateNumber(userId, cropId, number-1);
	}
	
}
