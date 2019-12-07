package com.farm.model;

import com.jfinal.plugin.activerecord.Model;

public class UserCrop extends Model<UserCrop>{
	public static final UserCrop dao = new UserCrop().dao();
}
