package com.farm.userfriend.dao;

import java.util.List;

import com.farm.model.UserFriend;
import com.jfinal.plugin.activerecord.Page;

public class UserFriendDao {
	
	/**
	 * 增
	 */
	//userfriend表添加数据
	public boolean addFriend(int userId, int friendId) {
		boolean succeed = UserFriend.dao.set("userId", userId).set("friendId", friendId).save();
		return succeed;
	}
	
	
	
	/**
	 * 删
	 */
	//userfriend表删除整条数据
	public boolean deleteFriend(int userId, int friendId) {
		List<UserFriend> list = UserFriend.dao.find("select * from userfriend where userId=? and friendId=?",userId,friendId);
		if(list.size() != 0) {
			boolean succeed = list.get(0).deleteById(list.get(0).getInt("id"));
			return succeed;
		}
		return false;
	}
	
	
	/**
	 * 改
	 */
	
	
	
	/**
	 * 查
	 */
	//userfriend表分页查询数据（指定userId、friendId、pageNumber、pageSize）
	public Page<UserFriend> findUserFriendByUserId(int userId, int friendId, int pageNumber, int pageSize){
		Page<UserFriend> userPage;
		switch (friendId) {
			case -1:
				userPage = UserFriend.dao.paginate(pageNumber, pageSize, "select *","from userfriend where userId=? and status=1",userId);
				break;
			case 0:
				userPage = null;
				break;
			default:
				userPage = UserFriend.dao.paginate(pageNumber, pageSize, "select *","from userfriend where userId=? and friendId=? and status=1",userId,friendId);
				break;
		}
		return userPage;
	}
}
