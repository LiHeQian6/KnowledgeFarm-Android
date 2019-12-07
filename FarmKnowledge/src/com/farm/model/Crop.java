package com.farm.model;

import com.jfinal.plugin.activerecord.Model;

public class Crop extends Model<Crop>{
	
	public static final Crop dao = new Crop().dao();
	
}
