package com.farm.model;

import com.jfinal.plugin.activerecord.Model;

public class UserFriend extends Model<UserFriend>{
	
	public static final UserFriend dao = new UserFriend().dao();
	
}
