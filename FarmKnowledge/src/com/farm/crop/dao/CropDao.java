package com.farm.crop.dao;

import java.util.ArrayList;
import java.util.List;

import com.farm.entity.Strings;
import com.farm.model.Crop;
import com.jfinal.plugin.activerecord.Page;

public class CropDao {
	
	
	/**
	 * 	��
	 * @throws
	 */
	//���������Ϣ
	public boolean addCrop(String name, int price, String img1, String img2, String img3, String img4, String cropPhotoName, int matureTime, int value, int experience) {
		boolean succeed = new Crop().set("name", name).set("price", price).set("img1", img1).set("img2", img2).set("img3", img3).set("img4", img4).set("cropPhotoName", cropPhotoName).set("matureTime", matureTime).set("value", value).set("experience", experience).save();
		return succeed;
	}
	
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	//����ɾ��Crop����������Ϣ��Crop��delete��
	public boolean deleteThoroughCrop(int id) {
		boolean succeed = Crop.dao.deleteById(id);
		return succeed;
	}
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�޸�������Ϣ
	public boolean updateCrop(int id, String name, int price, String img1, String img2, String img3, String img4, String cropPhotoName, int matureTime, int value, int experience) {
		boolean succeed = Crop.dao.findById(id).set("name", name).set("price", price).set("img1", img1).set("img2", img2).set("img3", img3).set("img4", img4).set("cropPhotoName", cropPhotoName).set("matureTime", matureTime).set("value", value).set("experience", experience).update();
		return succeed;
	}
	//ɾ��Crop���ڵ���������Ϣ��Crop���޸�exist�ֶ�Ϊ0��
	public boolean deleteOneCrop(int id) {
		boolean succeed = Crop.dao.findById(id).set("exist", 0).update();
		return succeed;
	}
	
	//�ָ�Crop���ڵ���������Ϣ��Crop���޸�exist�ֶ�Ϊ1��
	public boolean recoveryOneCrop(int id) {
		boolean succeed = Crop.dao.findById(id).set("exist", 1).update();
		return succeed;
	}
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//��ѯCrop��������������Ϣ��Crop��
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
	//��ѯ�̵�����������Ϣ
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
	//��̨ ��������id��ȡ��Ҫ�޸ĵ�������Ϣ
	public Crop getUpdateCropInfo0(int id) {
		Crop crop = Crop.dao.findById(id);
		return crop;
	}
	//��������id��ȡ��Ҫ�޸ĵ�������Ϣ
	public Crop getUpdateCropInfo(int id) {
		Crop crop = Crop.dao.findById(id);
		crop.set("img1", Strings.cropPhotoUrl+crop.get("img1")).set("img2", Strings.cropPhotoUrl+crop.get("img2")).set("img3", Strings.cropPhotoUrl+crop.get("img3")).set("img4", Strings.cropPhotoUrl+crop.get("img4"));
		return crop;
	}
	//��ѯ�Ƿ��Ѵ��ڸ�cropPhotoName
	public boolean isExistCropPhotoName(String cropPhotoName) {
		List<Crop> list = Crop.dao.find("select * from crop where cropPhotoName=?",cropPhotoName);
		if(list.size() != 0) {
			return true;
		}
		return false;
	}
	
}
