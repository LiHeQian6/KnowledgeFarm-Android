package com.farm.userfriend.service;

import com.farm.model.UserFriend;
import com.farm.userfriend.dao.UserFriendDao;
import com.jfinal.plugin.activerecord.Page;

public class UserFriendService {
	
	/**
	 * ��
	 */
	//��Ӻ���
	public boolean addFriend(int userId, int friendId) {
		return new UserFriendDao().addFriend(userId, friendId);
	}
	
	
	
	/**
	 * ɾ
	 */
	//ɾ������
	public boolean deleteFriend(int userId, int friendId) {
		return new UserFriendDao().deleteFriend(userId, friendId);
	}
	
	
	/**
	 * ��
	 */
	
	
	/**
	 * ��
	 */
	//����userId��ҳ��ѯfriendId
	public Page<UserFriend> findUserFriendByUserId(int userId, int pageNumber, int pageSize){
		return new UserFriendDao().findUserFriendByUserId(userId, pageNumber, pageSize);
	}
	
	
}
