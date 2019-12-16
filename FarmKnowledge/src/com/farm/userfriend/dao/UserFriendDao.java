package com.farm.userfriend.dao;

import java.util.List;

import com.farm.model.UserFriend;
import com.jfinal.plugin.activerecord.Page;

public class UserFriendDao {
	
	/**
	 * ��
	 */
	//��Ӻ���
	public boolean addFriend(int userId, int friendId) {
		boolean succeed = UserFriend.dao.set("userId", userId).set("friendId", friendId).save();
		return succeed;
	}
	
	
	
	/**
	 * ɾ
	 */
	//ɾ������
	public boolean deleteFriend(int userId, int friendId) {
		List<UserFriend> list = UserFriend.dao.find("select * from userfriend where userId=? and friendId=?",userId,friendId);
		if(list.size() != 0) {
			boolean succeed = list.get(0).deleteById(list.get(0).getInt("id"));
			return succeed;
		}
		return false;
	}
	
	
	/**
	 * ��
	 */
	
	
	
	/**
	 * ��
	 */
	//����userId��ҳ��ѯfriendId
	public Page<UserFriend> findUserFriendByUserId(int userId, int pageNumber, int pageSize){
		Page<UserFriend> userPage = UserFriend.dao.paginate(pageNumber, pageSize, "select *","from userfriend where userId=?",userId);
		return userPage;
	}
}
