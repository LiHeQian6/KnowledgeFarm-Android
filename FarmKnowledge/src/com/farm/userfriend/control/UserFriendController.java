package com.farm.userfriend.control;

import java.util.ArrayList;
import java.util.List;

import com.farm.model.User;
import com.farm.model.UserFriend;
import com.farm.user.service.UserService;
import com.farm.userfriend.service.UserFriendService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class UserFriendController extends Controller{
	
	//查询用户的好友列表
	public void findUserFriend() {
		int userId = getInt("userId");
		String accout = get("accout");
		String page = get("pageNumber");
		String size = get("pageSize");
		int pageNumber;
		int pageSize;
		 
		UserFriendService service = new UserFriendService();
		UserService userService = new UserService();
		if(page == null) {
			pageNumber = 1;
		}else {
			pageNumber = Integer.parseInt(page);
		}
		if(size == null) {
			pageSize = 4;
		}else {
			pageSize = Integer.parseInt(size);
		}
		
		if(accout == null) {
			List<User> userList = new ArrayList<User>();
			Page<UserFriend> uPage = service.findUserFriendByUserId(userId, pageNumber, pageSize);
			if(uPage != null) {
				for(UserFriend userFriend : uPage.getList()) {
					User user = userService.getUpdateUserInfo(userFriend.getInt("friendId"));
					userList.add(user);
				}
				renderJson(userList);
			}else {
				renderJson("[]");
			}
		}else {
			int friendId = userService.getUserIdByAccout(accout);
			User user = userService.getUpdateUserInfo(friendId);
			if(user != null) {
				List<User> list = new ArrayList<User>();
				list.add(user);
				renderJson(user);
			}else {
				renderJson("[]");
			}
		}
	}
	
	//添加好友
	public void addFriend() {
		int userId = getInt("userId");
		int friendId = getInt("friendId");
		
		
	}
	
	
	//删除好友
	public void deleteFriend() {
		int userId = getInt("userId");
		int friendId = getInt("friendId");
		
		boolean succeed = new UserFriendService().deleteFriend(userId, friendId);
		renderJson(""+succeed);
	}
	
}
