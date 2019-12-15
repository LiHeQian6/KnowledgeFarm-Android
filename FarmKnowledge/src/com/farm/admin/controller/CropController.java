package com.farm.admin.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.farm.crop.service.CropService;
import com.farm.entity.Strings;
import com.farm.model.Crop;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

public class CropController extends Controller{
	
	//��ѯCrop��������������Ϣ����ת�������б�ҳ�棨Crop��
	public void findCropPage() {
		String name = get("name");
		String page = get("pageNumber");
		String count = get("pageSize");
		int exist = getInt("exist");
		int pageNumber;
		int everyCount;
		
		if(page == null) {
			pageNumber = 1;
		}else {
			pageNumber = Integer.parseInt(page);
		}
		if(count == null) {
			everyCount = 4;
		}else {
			everyCount = Integer.parseInt(count);
		}
		
		removeSessionAttr("crop");
		Page<Crop> list = new CropService().findCropPage(pageNumber,everyCount,name,exist);
		setAttr("cropPage", list);
		if(exist == 1) {
			renderJsp("/crop-list.jsp");
		}else {
			renderJsp("/crop-del.jsp");
		}
	}
	
	//ɾ��Crop���ڵ���������Ϣ����ת�������б�ҳ�棨Crop���޸�exist�ֶ�Ϊ0��
	public void deleteOneCrop() {
		int id = getInt("id");
		boolean succeed = new CropService().deleteOneCrop(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//ɾ��Crop��������������Ϣ����ת�������б�ҳ�棨Crop���޸�exist�ֶ�Ϊ0��
	public void deleteMultiCrop() {
		String deleteStr = get("deleteStr");
		String deleteId[] = deleteStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a = true;
				CropService service = new CropService();
				for(String aString : deleteId) {
					a = service.deleteOneCrop(Integer.parseInt(aString));
				}
				if(a == true) {
					return true;
				}else {
					return false;
				}
			}
		});
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}

	//�ָ�Crop���ڵ���������Ϣ����ת�������б�ҳ�棨Crop���޸�exist�ֶ�Ϊ1��
	public void recoveryOneCrop() {
		int id = getInt("id");
		boolean succeed = new CropService().recoveryOneCrop(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//�ָ�Crop��������������Ϣ����ת�������б�ҳ�棨Crop���޸�exist�ֶ�Ϊ1��
	public void recoveryMultiCrop() {
		String recoveryStr = get("recoveryStr");
		String recoveryId[] = recoveryStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a = true;
				CropService service = new CropService();
				for(String aString : recoveryId) {
					a = service.recoveryOneCrop(Integer.parseInt(aString));
				}
				if(a == true) {
					return true;
				}else {
					return false;
				}
			}
		});
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//����ɾ��Crop����������Ϣ����ת�������б�ҳ�棨Crop��delete��
	public void deleteThoroughCrop() {
		int id = getInt("id");
		boolean succeed = new CropService().deleteThoroughCrop(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//���������Ϣ
	public void addCrop() {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		HttpServletRequest request = getRequest();
		
		String name = "";
		int price = 0;
		int matureTime = 0;
		int value = 0;
		int experience = 0;
		String realPath[] = new String[4];
		int count = 0;
		String cropPhotoName = "";
		
		CropService service = new CropService();
		try {
			List<FileItem> items = upload.parseRequest(request);
			//���˵�ͼƬΪ�յ����
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					
				}else {
					if(fi.getName().equals("")) {
						renderText("null");
						return;
					}					
				}
			}
			//ͼƬ��Ϊ��
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					String aString = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
					switch (fi.getFieldName()) {
						case "name":
							name = aString;
							break;
						case "price":
							price = Integer.parseInt(aString);
							break;
						case "matureTime":
							matureTime = Integer.parseInt(aString);
							break;
						case "value":
							value = Integer.parseInt(aString);
							break;
						case "experience":
							experience = Integer.parseInt(aString);
							break;
					}	
				}else {
					//����cropPhotoName�����ж��Ƿ������������cropPhotoName�ظ�
					if(count == 0) {
						do {
							cropPhotoName = "";
							cropPhotoName = service.generateRandom() + fi.getName();
						} while (service.isExistCropPhotoName(cropPhotoName));
					}				
					
					//����img
					realPath[count] = Strings.cropPhotoUrl + count + cropPhotoName + "?" + service.generateRandom();
					
					//��ͼƬд���ļ�
					File file = new File(Strings.cropfilePath + count + cropPhotoName);
					fi.write(file);
					
					if(count == 3) {
						boolean succeed = service.addCrop(name, price, realPath[0], realPath[1], realPath[2], realPath[3], cropPhotoName, matureTime, value, experience);
						if(succeed == true) {
							renderText("succeed");
						}else {
							renderText("fail");
						}
					}
					count++;
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//��������id��ȡ��Ҫ�޸ĵ�������Ϣ
	public void getUpdateCropInfo() {
		int id = getInt("id");
		Crop crop = new CropService().getUpdateCropInfo(id);
		if(crop != null) {
			setSessionAttr("crop", crop);
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//�޸�������Ϣ
	public void updateCrop() {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		HttpServletRequest request = getRequest();
		
		int id = 0;
		String img1 = "";
		String img2 = "";
		String img3 = "";
		String img4 = "";
		String cropPhotoName = "";
		String name = "";
		int price = 0;
		int matureTime = 0;
		int value = 0;
		int experience = 0;
		String realPath[] = new String[4];
		int count = 0;
		
		CropService service = new CropService();
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem fi : items) {
				String aString = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
				if(fi.isFormField()) {
					switch (fi.getFieldName()) {
						case "id":
							id = Integer.parseInt(aString);
							break;
						case "img1":
							img1 = aString;
							break;
						case "img2":
							img2 = aString;
							break;
						case "img3":
							img3 = aString;
							break;
						case "img4":
							img4 = aString;
							break;
						case "cropPhotoName":
							cropPhotoName = aString;
							break;
						case "name":
							name = aString;
							break;
						case "price":
							price = Integer.parseInt(aString);
							break;
						case "matureTime":
							matureTime = Integer.parseInt(aString);
							break;
						case "value":
							value = Integer.parseInt(aString);
							break;
						case "experience":
							experience = Integer.parseInt(aString);
							break;
					}	
				}else {
					if(fi.getName().equals("")) { //ͼƬΪ�գ�Ĭ��չʾ֮ǰ��ͼƬ
						switch(count) {
							case 0:
								realPath[count] = img1;
								break;
							case 1:
								realPath[count] = img2;
								break;
							case 2:
								realPath[count] = img3;
								break;
							case 3:
								realPath[count] = img4;
								break;
						}
					}else { //ͼƬ��Ϊ��
						//����img�����ж��Ƿ����ϴε�img�ظ�
						switch (count) {
							case 0:
								do {
									realPath[count] = "";
									realPath[count] = Strings.cropPhotoUrl + count + cropPhotoName + "?" + service.generateRandom();
								} while (realPath[count].equals(img1));
								break;
							case 1:
								do {
									realPath[count] = "";
									realPath[count] = Strings.cropPhotoUrl + count + cropPhotoName + "?" + service.generateRandom();
								} while (realPath[count].equals(img2));
								break;
							case 2:
								do {
									realPath[count] = "";
									realPath[count] = Strings.cropPhotoUrl + count + cropPhotoName + "?" + service.generateRandom();
								} while (realPath[count].equals(img3));
								break;
							case 3:
								do {
									realPath[count] = "";
									realPath[count] = Strings.cropPhotoUrl + count + cropPhotoName + "?" + service.generateRandom();
								} while (realPath[count].equals(img4));
								break;
						}
						
						//��ͼƬд���ļ�
						File file = new File(Strings.cropfilePath + count + cropPhotoName);
						fi.write(file);
					}
					if(count == 3) {
						boolean succeed = service.updateCrop(id, name, price, realPath[0], realPath[1], realPath[2], realPath[3], cropPhotoName, matureTime, value, experience);
						if(succeed == true) {
							renderText("succeed");
						}else {
							renderText("fail");
						}
					}
					count++;
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
