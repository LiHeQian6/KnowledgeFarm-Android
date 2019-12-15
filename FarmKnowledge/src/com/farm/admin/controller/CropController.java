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
	
	//查询Crop表内所有作物信息，跳转到作物列表页面（Crop表）
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
	
	//删除Crop表内单个作物信息，跳转到作物列表页面（Crop表修改exist字段为0）
	public void deleteOneCrop() {
		int id = getInt("id");
		boolean succeed = new CropService().deleteOneCrop(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//删除Crop表内批量作物信息，跳转到作物列表页面（Crop表修改exist字段为0）
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

	//恢复Crop表内单个作物信息，跳转到作物列表页面（Crop表修改exist字段为1）
	public void recoveryOneCrop() {
		int id = getInt("id");
		boolean succeed = new CropService().recoveryOneCrop(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//恢复Crop表内批量作物信息，跳转到作物列表页面（Crop表修改exist字段为1）
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
	
	//彻底删除Crop表内作物信息，跳转到作物列表页面（Crop表delete）
	public void deleteThoroughCrop() {
		int id = getInt("id");
		boolean succeed = new CropService().deleteThoroughCrop(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//添加作物信息
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
			//过滤掉图片为空的情况
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					
				}else {
					if(fi.getName().equals("")) {
						renderText("null");
						return;
					}					
				}
			}
			//图片不为空
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
					//构造cropPhotoName，并判断是否与其他作物的cropPhotoName重复
					if(count == 0) {
						do {
							cropPhotoName = "";
							cropPhotoName = service.generateRandom() + fi.getName();
						} while (service.isExistCropPhotoName(cropPhotoName));
					}				
					
					//构造img
					realPath[count] = Strings.cropPhotoUrl + count + cropPhotoName + "?" + service.generateRandom();
					
					//把图片写入文件
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
	
	//根据作物id获取到要修改的作物信息
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
	
	//修改作物信息
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
					if(fi.getName().equals("")) { //图片为空，默认展示之前的图片
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
					}else { //图片不为空
						//构造img，并判断是否与上次的img重复
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
						
						//把图片写入文件
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
