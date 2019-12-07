package com.farm.crop.control;

import java.util.List;

import com.farm.crop.service.CropService;
import com.farm.model.Crop;
import com.jfinal.core.Controller;

public class CropController extends Controller{
	
	//加载商店所有作物信息
	public void initCrop() {
		List<Crop> list = new CropService().findCrop();
		renderJson(list);
	}
}
