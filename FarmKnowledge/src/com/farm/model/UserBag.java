package com.farm.model;

import com.jfinal.plugin.activerecord.Model;

public class UserBag extends Model<UserBag>{
	public static final UserBag dao = new UserBag().dao();
}
