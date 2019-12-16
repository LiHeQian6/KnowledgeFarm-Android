package com.farm.userfriend.service;

import com.farm.model.UserFriend;
import com.farm.userfriend.dao.UserFriendDao;
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
	//根据userId分页查询friendId
	public Page<UserFriend> findUserFriendByUserId(int userId, int pageNumber, int pageSize){
		return new UserFriendDao().findUserFriendByUserId(userId, pageNumber, pageSize);
	}
	
	
}
