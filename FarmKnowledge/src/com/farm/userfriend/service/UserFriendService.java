package com.farm.userfriend.service;

import java.sql.SQLException;

import com.farm.crop.dao.CropDao;
import com.farm.entity.UserCropTimerManager;
import com.farm.model.Crop;
import com.farm.model.UserCrop;
import com.farm.model.UserFriend;
import com.farm.user.service.UserService;
import com.farm.usercrop.dao.UserCropDao;
import com.farm.userfriend.dao.UserFriendDao;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

public class UserFriendService {
	
	/**
	 * 增
	 */
	//添加好友
	public boolean addFriend(int userId, int friendId) {
		return new UserFriendDao().addFriend(userId, friendId);
	}
	
	
	
	/**
	 * 删
	 */
	//删除好友
	public boolean deleteFriend(int userId, int friendId) {
		return new UserFriendDao().deleteFriend(userId, friendId);
	}
	
	
	
	/**
	 * 改
	 */
	
	
	
	/**
	 * 查
	 */
	//userfriend表分页查询数据（指定userId、friendId、pageNumber、pageSize）
	public Page<UserFriend> findUserFriendByUserId(int userId, int friendId, int pageNumber, int pageSize){
		return new UserFriendDao().findUserFriendByUserId(userId, friendId, pageNumber, pageSize);
	}
	
	
	
	/**
	 * 操作
	 */
	//浇水
	public boolean waterForFriend(int userId, int friendId, String landNumber) {
		UserService userService = new UserService();
		int ucId = userService.findUcId(friendId, landNumber);
		UserCropDao dao = new UserCropDao();
		UserCrop userCrop = dao.findUserCropById(ucId);
		
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a1 = false;
				boolean a2 = userService.lessW(userId);
				boolean a3 = false;
				boolean a4 = userService.addMoney(userId, 10);
				
				int cropId = userCrop.getInt("cropId");
				int progress = dao.getCropProgress(ucId);
				Crop crop = new CropDao().getUpdateCropInfo(cropId);
				int matureTime = crop.getInt("matureTime");
				
				if(progress+5 >= matureTime) {
					a1 = dao.updateProgress(ucId, crop.getInt("matureTime"));
				}else {
					a1 = dao.updateProgress(ucId, progress+5);
				}
				
				if(a1 == true && a2 == true && a4 == true) {
					if(userCrop.getInt("state") == 0) {
						a3 = dao.updateCropState(ucId, 1);
						new UserCropTimerManager(ucId); 
					}else {
						a3 = true;
					}
					if(a3) {
						return true;
					}
					return false;
				}
				return false;
			}
		});
		
		if(succeed) {
			return true;
		}
		return false;				
	}
	//施肥
	public boolean fertilizerForFriend(int userId, int friendId, String landNumber) {
		UserService userService = new UserService();
		UserCropDao dao = new UserCropDao();
		int ucId = new UserService().findUcId(friendId, landNumber);
		
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				if(dao.findUserCropById(ucId).getInt("state") != 0) {
					boolean a1 = false;
					boolean a2 = new UserService().lessF(userId);
					boolean a3 = userService.addMoney(userId, 20);
					
					Crop crop = new CropDao().getUpdateCropInfo(dao.getCropIdByUserCropId(ucId));
					int progress = dao.getCropProgress(ucId);
					int matureTime = crop.getInt("matureTime");
					
					if(progress+10 >= matureTime) {
						a1 = dao.updateProgress(ucId, crop.getInt("matureTime"));
					}else {
						a1 = dao.updateProgress(ucId, progress+10);
					}
					
					if(a1 == true && a2 == true && a3 == true) {
						return true;
					}
					return false;
				}else {
					return false;
				}
			}
		});
		
		if(succeed) {
			return true;
		}
		return false;	
	}
	
}
