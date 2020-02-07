package com.farm.crop.service;

import java.util.List;
import java.util.UUID;

import com.farm.crop.dao.CropDao;
import com.farm.model.Crop;
import com.jfinal.plugin.activerecord.Page;

public class CropService {
	
	/**
	 * 	��
	 * @throws
	 */
	//���������Ϣ
	public boolean addCrop(String name, int price, String img1, String img2, String img3, String img4, String cropPhotoName, int matureTime, int value, int experience) {
		return new CropDao().addCrop(name, price, img1, img2, img3, img4, cropPhotoName, matureTime, value, experience);
	}
	
	
	
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	//��̨����ϵͳ����ɾ������������Ϣ
	public boolean deleteThoroughCrop(int id) {
		return new CropDao().deleteThoroughCrop(id);
	}

	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�޸�������Ϣ
	public boolean updateCrop(int id, String name, int price, String img1, String img2, String img3, String img4, String cropPhotoName, int matureTime, int value, int experience) {
		return new CropDao().updateCrop(id, name, price, img1, img2, img3, img4, cropPhotoName, matureTime, value, experience);
	}
	//��̨����ϵͳ�޸ĵ���������Ϣ��exist
	public boolean updateExist(int id, int exist) {
		return new CropDao().updateExist(id, exist);
	}


	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//��ҳ��ѯ������Ϣ��ָ��name��pageNumber��pageSize��exist��
	public Page<Crop> findCropPage(int pageNumber,int everyCount,String name,int exist) {
		return new CropDao().findCropPage(pageNumber,everyCount,name,exist);
	}
	//��ѯ�̵�����������Ϣ��ָ��exist=1��
	public List<Crop> findCrop(){
		return new CropDao().findCrop();
	}
	//��̨ ��ѯ������Ϣ��ָ��id��
	public Crop getUpdateCropInfo0(int id) {
		return new CropDao().getUpdateCropInfo0(id);
	}
	//��ѯ������Ϣ��ָ��id��
	public Crop getUpdateCropInfo(int id) {
		return new CropDao().getUpdateCropInfo(id);
	}
	//�ж��Ƿ��Ѵ��ڸ�cropPhotoName
	public boolean isExistCropPhotoName(String cropPhotoName) {
		return new CropDao().isExistCropPhotoName(cropPhotoName);
	}
	
	//���ɰ�λ�����
	public String generateRandom() {
		String cropPhotoUrlLast = generateShortUuid();
		return cropPhotoUrlLast;
	}
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	public static String generateShortUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		System.out.println(shortBuffer.toString());
	
		return shortBuffer.toString();
	 
	}

}
