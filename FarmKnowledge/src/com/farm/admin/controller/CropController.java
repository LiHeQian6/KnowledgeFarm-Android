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
		String realPath[] = new String[3];
		int count = 0;
		CropService service = new CropService();
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					
				}else {
					if(fi.getName().equals("")) {
						renderText("null");
						return;
					}					
				}
			}
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					switch (fi.getFieldName()) {
						case "name":
							name = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
							break;
						case "price":
							price = Integer.parseInt(new String(fi.getString().getBytes("ISO8859_1"),"utf-8"));
							break;
						case "matureTime":
							matureTime = Integer.parseInt(new String(fi.getString().getBytes("ISO8859_1"),"utf-8"));
							break;
						case "value":
							value = Integer.parseInt(new String(fi.getString().getBytes("ISO8859_1"),"utf-8"));
							break;
						case "experience":
							experience = Integer.parseInt(new String(fi.getString().getBytes("ISO8859_1"),"utf-8"));
							break;
					}	
				}else {
					String cropPhotoName = service.generateCropPhotoName();
					realPath[count] = Strings.photoUrl + count + cropPhotoName;
					File file = new File(Strings.filePath + count + cropPhotoName);
					fi.write(file);
					
					if(count == 2) {
						boolean succeed = service.addCrop(name, price, realPath[0], realPath[1], realPath[2], cropPhotoName, matureTime, value, experience);
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
		String cropPhotoName = "";
		String name = "";
		int price = 0;
		int matureTime = 0;
		int value = 0;
		int experience = 0;
		String realPath[] = new String[3];
		int count = 0;
		CropService service = new CropService();
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					switch (fi.getFieldName()) {
						case "id":
							id = Integer.parseInt(new String(fi.getString().getBytes("ISO8859_1"),"utf-8"));
							break;
						case "img1":
							img1 = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
							break;
						case "img2":
							img2 = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
							break;
						case "img3":
							img3 = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
							break;
						case "cropPhotoName":
							cropPhotoName = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
							break;
						case "name":
							name = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
							break;
						case "price":
							price = Integer.parseInt(new String(fi.getString().getBytes("ISO8859_1"),"utf-8"));
							break;
						case "matureTime":
							matureTime = Integer.parseInt(new String(fi.getString().getBytes("ISO8859_1"),"utf-8"));
							break;
						case "value":
							value = Integer.parseInt(new String(fi.getString().getBytes("ISO8859_1"),"utf-8"));
							break;
						case "experience":
							experience = Integer.parseInt(new String(fi.getString().getBytes("ISO8859_1"),"utf-8"));
							break;
					}	
				}else {
					if(fi.getName().equals("")) {
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
						}
					}else {
						realPath[count] = Strings.photoUrl + count + cropPhotoName;
						File file = new File(Strings.filePath + count + cropPhotoName);
						fi.write(file);
					}
					if(count == 2) {
						boolean succeed = service.updateCrop(id, name, price, realPath[0], realPath[1], realPath[2], cropPhotoName, matureTime, value, experience);
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
