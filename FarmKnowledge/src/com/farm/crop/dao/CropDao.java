package com.farm.crop.dao;

import java.util.ArrayList;
import java.util.List;

import com.farm.entity.Strings;
import com.farm.model.Crop;
import com.jfinal.plugin.activerecord.Page;

public class CropDao {
	
	
	/**
	 * 	增
	 * @throws
	 */
	//crop表插入整条数据
	public boolean addCrop(String name, int price, String img1, String img2, String img3, String img4, String cropPhotoName, int matureTime, int value, int experience) {
		boolean succeed = new Crop().set("name", name).set("price", price).set("img1", img1).set("img2", img2).set("img3", img3).set("img4", img4).set("cropPhotoName", cropPhotoName).set("matureTime", matureTime).set("value", value).set("experience", experience).save();
		return succeed;
	}
	
	
	
	
	
	/**
	 * 	删
	 * @throws
	 */
	//crop表删除整条数据
	public boolean deleteThoroughCrop(int id) {
		boolean succeed = Crop.dao.deleteById(id);
		return succeed;
	}
	
	
	
	
	
	/**
	 * 	改
	 * @throws
	 */
	//crop表修改数据
	public boolean updateCrop(int id, String name, int price, String img1, String img2, String img3, String img4, String cropPhotoName, int matureTime, int value, int experience) {
		boolean succeed = Crop.dao.findById(id).set("name", name).set("price", price).set("img1", img1).set("img2", img2).set("img3", img3).set("img4", img4).set("cropPhotoName", cropPhotoName).set("matureTime", matureTime).set("value", value).set("experience", experience).update();
		return succeed;
	}
	//crop表修改exist
	public boolean updateExist(int id, int exist) {
		boolean succeed = Crop.dao.findById(id).set("exist", exist).update();
		return succeed;
	}

	
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//crop表分页查询数据（指定name、pageNumber、pageSize、exist）
	public Page<Crop> findCropPage(int pageNumber,int everyCount,String name,int exist) {
		Page<Crop> cropPage;
		if(name == null || name.equals("")) {
			cropPage = Crop.dao.paginate(pageNumber, everyCount, "select *","from crop where exist=?",exist);	
		}else {
			cropPage = Crop.dao.paginate(pageNumber, everyCount, "select *","from crop where exist=? and name like?",exist,"%"+name+"%");
		}
		if(cropPage != null) {
			List<Crop> list = cropPage.getList();
			if(list.size() != 0) {
				int i = 0;
				for(Crop crop : list) {
					list.set(i, crop.set("img1", Strings.cropPhotoUrl+crop.get("img1")).set("img2", Strings.cropPhotoUrl+crop.get("img2")).set("img3", Strings.cropPhotoUrl+crop.get("img3")).set("img4", Strings.cropPhotoUrl+crop.get("img4")));
					i++;
				}
				cropPage.setList(list);
			}
		}
		return cropPage;
	}
	//crop表查询所有数据（指定exist=1）
	public List<Crop> findCrop(){
		List<Crop> list = new ArrayList<Crop>();
		list = Crop.dao.find("select * from crop where exist=1");
		if(list.size() != 0) {
			int i = 0;
			for(Crop crop : list) {
				list.set(i, crop.set("img1", Strings.cropPhotoUrl+crop.get("img1")).set("img2", Strings.cropPhotoUrl+crop.get("img2")).set("img3", Strings.cropPhotoUrl+crop.get("img3")).set("img4", Strings.cropPhotoUrl+crop.get("img4")));
				i++;
			}
			return list;
		}
		return null;
	}
	//后台 crop表查询整条数据（指定id）
	public Crop getUpdateCropInfo0(int id) {
		Crop crop = Crop.dao.findById(id);
		return crop;
	}
	//crop表查询整条数据（指定id）
	public Crop getUpdateCropInfo(int id) {
		Crop crop = Crop.dao.findById(id);
		crop.set("img1", Strings.cropPhotoUrl+crop.get("img1")).set("img2", Strings.cropPhotoUrl+crop.get("img2")).set("img3", Strings.cropPhotoUrl+crop.get("img3")).set("img4", Strings.cropPhotoUrl+crop.get("img4"));
		return crop;
	}
	//crop表判断是否已存在该cropPhotoName
	public boolean isExistCropPhotoName(String cropPhotoName) {
		List<Crop> list = Crop.dao.find("select * from crop where cropPhotoName=?",cropPhotoName);
		if(list.size() != 0) {
			return true;
		}
		return false;
	}
	
}
