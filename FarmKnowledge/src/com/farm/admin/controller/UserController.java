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
import com.farm.model.User;
import com.farm.user.service.UserService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

public class UserController extends Controller{
	
	//查询User表内用户信息，跳转到用户列表页面（User表）
	public void findUserPage() {
		String accout = get("accout");
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
		
		removeSessionAttr("user");
		Page<User> list = new UserService().findUserPage(pageNumber,everyCount,accout,exist);
		setAttr("userPage", list);
		if(exist == 1) {
			renderJsp("/member-list.jsp");
		}else {
			renderJsp("/member-del.jsp");
		}
	}

	//删除User表内单个用户信息，跳转到用户列表页面（User、UserAuthority表修改exist字段为0）
	public void deleteOneUser() {
		int userId = getInt("userId");
		
		UserService service = new UserService();
		boolean succeed = service.deleteOneUser(userId);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//删除User表内批量用户信息，跳转到用户列表页面（User、UserAuthority表修改exist字段为0）
	public void deleteMultiUser() {
		String deleteStr = get("deleteStr");
		String deleteId[] = deleteStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a = false;
				UserService service = new UserService();
				for(String aString : deleteId) {
					a = service.deleteOneUser(Integer.parseInt(aString));
					if(a == false) {
						return false;
					}
				}
				return true;
			}
		});
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//恢复User表内单个用户信息，跳转到用户列表页面（User、UserAuthority表修改exist字段为1）
	public void recoveryOneUser() {
		int userId = getInt("userId");
		
		UserService service = new UserService();
		boolean succeed = service.recoveryOneUser(userId);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//恢复User表内批量用户信息，跳转到用户列表页面（User、UserAuthority表修改exist字段为1）
	public void recoveryMultiUser() {
		String recoveryStr = get("recoveryStr");
		String recoveryId[] = recoveryStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a = false;
				UserService service = new UserService();
				for(String aString : recoveryId) {
					a = service.recoveryOneUser(Integer.parseInt(aString));
					if(a == false) {
						return false;
					}
				}
				return true;
			}
		});
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//彻底删除User表内用户信息，跳转到用户列表页面（User、UserAuthority表delete）
	public void deleteThoroughUser() {
		int userId = getInt("userId");
		
		UserService service = new UserService();
		boolean succeed = service.deleteThoroughUser(userId);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//添加用户信息
	public void addUser() {		
		UserService service = new UserService();
		String nickName = get("nickName");
		String password = service.stringMD5(get("password"));
		String email = get("email");
		int grade = getInt("grade");
		
		boolean succeed = service.addUser(service.generateAccout(), nickName, password, Strings.userPhotoUrl + "0.png", "", email, grade);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
		
	}

	//根据用户id获取到要修改的用户信息（账号、别名、头像）
	public void getUpdateUserInfo() {
		int id = getInt("id");
		User user = new UserService().getUpdateUserInfo(id);
		if(user != null) {
			setSessionAttr("user", user);
			renderText("succeed");
		}else {
			renderText("fail");
		}			
	}
	
	//修改用户信息（账号、别名、头像），根据修改前账号索引到
	public void updateUser() {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		HttpServletRequest request = getRequest();
		
		String oldAccout = "";
		String photo = "";
		String photoName = "";
		String newAccout = "";
		String nickName = "";
		String email = "";
		int grade = 0;
		int level = 0;
		int experience = 0;
		int money = 0;
		int mathRewardCount = 0;
		int englishRewardCount = 0;
		int chineseRewardCount = 0;
		int water = 0;
		int fertilizer = 0;
		int online = 0;
		String newPhoto = "";
		
		UserService service = new UserService();
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					String aString = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
					switch (fi.getFieldName()) {
						case "oldAccout":
							oldAccout = aString;
							break;
						case "photo":
							photo = aString;
							break;
						case "photoName":
							photoName = aString;
							break;
						case "newAccout":
							newAccout = aString;
							if(service.isExistUserByAccout(oldAccout,newAccout)) {
								renderText("already");
								return;
							}
							break;
						case "nickName":
							nickName = aString;
							break;
						case "email":
							email = aString;
							break;
						case "grade":
							grade = Integer.parseInt(aString);
							break;
						case "level":
							level = Integer.parseInt(aString);
							break;
						case "experience":
							experience = Integer.parseInt(aString);
							break;
						case "money":
							money = Integer.parseInt(aString);
							break;
						case "mathRewardCount":
							mathRewardCount = Integer.parseInt(aString);
							break;
						case "englishRewardCount":
							englishRewardCount = Integer.parseInt(aString);
							break;
						case "chineseRewardCount":
							chineseRewardCount = Integer.parseInt(aString);
							break;
						case "water":
							water = Integer.parseInt(aString);
							break;
						case "fertilizer":
							fertilizer = Integer.parseInt(aString);
							break;
						case "online":
							if(aString.equals("on")) {
								online = 1;
							}else {
								online = 0;
							}
							break;
					}	
				}else {
					if(fi.getName().equals("")) { //图片为空，默认展示之前的头像
						newPhoto = photo;
					}else { //图片不为空
						//判断photoName的MIMETYPE类型是否是图片，若是则重新构造，并判断是否与其他用户的photoName重复
						CropService cropService = new CropService();
						if(request.getServletContext().getMimeType(photoName) == null) {
							do {
								photoName = "";
								photoName = cropService.generateRandom() + fi.getName();
							} while (service.isExistPhotoName(photoName));
						}
						
						//构造photo，并判断是否与上次的photo重复
						do {
							newPhoto = "";
							newPhoto = Strings.userPhotoUrl + photoName + "?" + cropService.generateRandom();
						} while (newPhoto.equals(photo));
						
						//把头像写入文件
						File file = new File(Strings.userfilePath + photoName);
						fi.write(file);
					}
					boolean succeed = service.updateUser(oldAccout, newAccout, nickName, newPhoto, photoName, email, grade, level, experience, money
						, mathRewardCount, englishRewardCount, chineseRewardCount, water, fertilizer, online);
					if(succeed == true) {
						renderText("succeed");
					}else {
						renderText("fail");
					}	
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	//修改用户密码
	public void updateUserPassword() {
		UserService service = new UserService();
		String accout = get("accout");
		String oldPassword = service.stringMD5(get("oldPassword"));
		String newPassword = service.stringMD5(get("newPassword"));
		
		int result = new UserService().updateUserPassword(oldPassword, newPassword, accout);
		if(result == 0) {
			renderText("PasswordError");
		}else if(result == 1){
			renderText("succeed");
		}else if(result == 2){
			renderText("fail");
		}
	}
	
}
