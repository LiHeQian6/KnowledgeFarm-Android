package com.farm.model;

import com.jfinal.plugin.activerecord.Model;

public class Admin extends Model<Admin>{
	
	public static final Admin dao = new Admin().dao();
	
}
