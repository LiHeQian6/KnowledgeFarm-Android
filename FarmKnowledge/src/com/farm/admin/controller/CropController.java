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
	
	//分页查询作物信息，跳转到作物列表页面（name、pageNumber、pageSize、exist）
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
	
	//后台管理系统删除单个作物信息，跳转到作物列表页面（id）
	public void deleteOneCrop() {
		int id = getInt("id");
		boolean succeed = new CropService().updateExist(id, 0);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//后台管理系统删除批量作物信息，跳转到作物列表页面（id字符串）
	public void deleteMultiCrop() {
		String deleteStr = get("deleteStr");
		String deleteId[] = deleteStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a = true;
				CropService service = new CropService();
				for(String aString : deleteId) {
					a = service.updateExist(Integer.parseInt(aString), 0);
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

	//后台管理系统恢复单个作物信息，跳转到作物列表页面（id）
	public void recoveryOneCrop() {
		int id = getInt("id");
		boolean succeed = new CropService().updateExist(id, 1);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//后台管理系统恢复批量作物信息，跳转到作物列表页面（id字符串）
	public void recoveryMultiCrop() {
		String recoveryStr = get("recoveryStr");
		String recoveryId[] = recoveryStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a = true;
				CropService service = new CropService();
				for(String aString : recoveryId) {
					a = service.updateExist(Integer.parseInt(aString), 1);
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
	
	//后台管理系统彻底删除单个作物信息，跳转到作物列表页面（id）
	public void deleteThoroughCrop() {
		int id = getInt("id");
		boolean succeed = new CropService().deleteThoroughCrop(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//添加作物信息（name、price、matureTime、value、experience、图片）
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
					realPath[count] = count + cropPhotoName + "?" + service.generateRandom();
					
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
	
	//查询要修改的作物信息（id）
	public void getUpdateCropInfo() {
		int id = getInt("id");
		Crop crop = new CropService().getUpdateCropInfo0(id);
		if(crop != null) {
			setSessionAttr("crop", crop);
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//修改作物信息（id、img...、cropPhotoName、name、price、matureTime、value、experience、图片）
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
						System.out.println(realPath[count]);
					}else { //图片不为空
						//构造img，并判断是否与上次的img重复
						switch (count) {
							case 0:
								do {
									realPath[count] = "";
									realPath[count] = count + cropPhotoName + "?" + service.generateRandom();
								} while (realPath[count].equals(img1));
								break;
							case 1:
								do {
									realPath[count] = "";
									realPath[count] = count + cropPhotoName + "?" + service.generateRandom();
								} while (realPath[count].equals(img2));
								break;
							case 2:
								do {
									realPath[count] = "";
									realPath[count] = count + cropPhotoName + "?" + service.generateRandom();
								} while (realPath[count].equals(img3));
								break;
							case 3:
								do {
									realPath[count] = "";
									realPath[count] = count + cropPhotoName + "?" + service.generateRandom();
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
