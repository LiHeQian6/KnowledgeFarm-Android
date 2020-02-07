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
	 * 	增
	 * @throws
	 */

	
	
	
	/**
	 * 	删
	 * @throws
	 */
	//删除指定作物
	public boolean deleteCropByUserIdAndCropId(int userId,int cropId) {
		return new BagDao().deleteCropByUserIdAndCropId(userId, cropId);
	}
	
	
	
	
	/**
	 * 	改
	 * @throws
	 */
	
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//查询用户背包是否存在该作物（指定userId、cropId）
	public boolean isExistCrop(int userId, int cropId) {
		return new BagDao().isExistCrop(userId, cropId);
	}
	//查询用户背包cropId列表（指定userId）
	public List<BagCropItem> getCropIdByUserId(int userId){
		return new BagDao().getCropIdByUserId(userId);
	}
	//查询指定作物的剩余数量（指定userId、cropId）
	public int findNumberByCropId(int userId, int cropId) {
		return new BagDao().findNumberByCropId(userId, cropId);
	}
	
	
	
	/**
	 * 	操作
	 * @throws
	 */
	//购买作物（背包中不存在此作物）
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
	//购买作物（背包中已存在此作物）
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
	//指定作物数量减少一个
	public boolean decreaseOneCrop(int userId,int cropId) {
		BagDao bagDao = new BagDao();
		int number = bagDao.findNumberByCropId(userId, cropId);
		if(number == 1) {
			return bagDao.deleteCropByUserIdAndCropId(userId, cropId);
		}
		return bagDao.updateNumber(userId, cropId, number-1);
	}
	
}
