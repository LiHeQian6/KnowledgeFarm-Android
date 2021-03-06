package com.farm.userfriend.control;

import java.util.ArrayList;
import java.util.List;

import com.farm.entity.UserPage;
import com.farm.model.User;
import com.farm.model.UserFriend;
import com.farm.user.service.UserService;
import com.farm.userfriend.service.UserFriendService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class UserFriendController extends Controller{
	
	//查询用户的好友列表（userId、accout、pageNumber、pageSize）
	public void findUserFriend() {
		int userId = getInt("userId");
		String accout = get("accout");
		String page = get("pageNumber");
		String size = get("pageSize");
		int pageNumber;
		int pageSize;
		int friendId;
		 
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
		if(accout == null || accout.equals("")) {
			friendId = -1;
		}else {
			friendId = new UserService().findUserByAccout(accout).getInt("id");
		}
		
		Page<UserFriend> friendPage = service.findUserFriendByUserId(userId, friendId, pageNumber, pageSize);
		List<User> userList = new ArrayList<User>();
		UserPage<User> userPage = new UserPage<User>(pageNumber,pageSize);
		if(friendPage != null) {
			if(friendPage.getTotalRow() != 0) {
				for(UserFriend userFriend : friendPage.getList()) {
					User user = userService.getUpdateUserInfo(userFriend.getInt("friendId"));
					userList.add(user);
				}
			}
			userPage.setTotalCount(friendPage.getTotalRow());
		}else {
			userPage.setTotalCount(0);
		}
		userPage.setList(userList);
		renderJson(userPage);
	}
	
	//查询所有人（accout、pageNumber、pageSize）
	public void findAllUser() {
		String accout = get("accout");
		String page = get("pageNumber");
		String size = get("pageSize");
		int pageNumber;
		int pageSize;
		
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
		
		Page<User> sqlPage = new UserService().findUserPageAll(pageNumber,pageSize,accout);
		UserPage<User> userPage = new UserPage<User>(pageNumber,pageSize);
		userPage.setTotalCount(sqlPage.getTotalRow());
		userPage.setList(sqlPage.getList());
		renderJson(userPage);
	}
	
	//添加好友
	public void addFriend() {
		int userId = getInt("userId");
		int friendId = getInt("friendId");
		
		
	}
	
	//删除好友（userId、friendId）
	public void deleteFriend() {
		int userId = getInt("userId");
		int friendId = getInt("friendId");
		
		boolean succeed = new UserFriendService().deleteFriend(userId, friendId);
		renderJson(""+succeed);
	}
	
	//帮好友浇水（userId、friendId、landNumber）
	public void waterForFriend() {
		int userId = getInt("userId");
		int friendId = getInt("friendId");
		String landNumber = get("landNumber");
		
		boolean succeed = new UserFriendService().waterForFriend(userId, friendId, landNumber);
		renderJson(""+succeed);
	}
	
	//帮好友施肥（userId、friendId、landNumber）
	public void fertilizerForFriend() {
		int userId = getInt("userId");
		int friendId = getInt("friendId");
		String landNumber = get("landNumber");
		
		boolean succeed = new UserFriendService().fertilizerForFriend(userId, friendId, landNumber);
		renderJson(""+succeed);
	}
}
