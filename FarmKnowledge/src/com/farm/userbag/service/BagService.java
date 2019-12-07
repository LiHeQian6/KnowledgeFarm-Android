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
	 * 	增
	 * @throws
	 */
	//购买后添加种子到背包（UserBag）
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
	 * 	删
	 * @throws
	 */
	
	
	
	/**
	 * 	改
	 * @throws
	 */
	//增加指定作物的数量（UserBag）
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
	//减少指定作物一个（UserBag）
	public boolean decreaseOneCrop(int userId,int cropId) {
		return new BagDao().decreaseOneCrop(userId, cropId);
	}
	//使用背包中种子
	public boolean lessCropBag(int userId,int cropId) {
		return new BagDao().lessCropInBag(userId, cropId);
	}
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//查询背包是否存在该作物
	public boolean isExistCrop(int userId, int cropId) {
		return new BagDao().isExistCrop(userId, cropId);
	}
	//根据userId查询用户背包cropId列表
	public List<Integer> getCropIdByUserId(int userId){
		return new BagDao().getCropIdByUserId(userId);
	}
	
}
