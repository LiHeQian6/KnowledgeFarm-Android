package com.farm.model;

import com.jfinal.plugin.activerecord.Model;

public class UserAuthority extends Model<UserAuthority>{
	
	public static final UserAuthority dao = new UserAuthority().dao();
	
}
