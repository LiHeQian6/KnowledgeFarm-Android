package com.farm.admin.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.farm.crop.service.CropService;
import com.farm.entity.Strings;
import com.farm.model.User;
import com.farm.user.service.UserService;
import com.farm.usercrop.service.UserCropService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

public class UserController extends Controller{
	private boolean isDelete = false;
	
	//分页查询用户信息，跳转到用户列表页面
	public void findUserPage() {
		String accout = get("accout");
		String page = get("pageNumber");
		String count = get("pageSize");
		String exist = get("exist");
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
		Page<User> list;
		if(exist == null || exist.equals("")) {
			list = new UserService().findUserPageAll(pageNumber, everyCount, accout);
			setAttr("userPage", list);
			renderJsp("/member-land-list.jsp");
		}else {
			list = new UserService().findUserPage(pageNumber,everyCount,accout,Integer.parseInt(exist));
			setAttr("userPage", list);
			if(Integer.parseInt(exist) == 1) {
				renderJsp("/member-list.jsp");
			}else {
				renderJsp("/member-del.jsp");
			}
		}
	}

	//后台管理系统删除单个用户信息，跳转到用户列表页面
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
	
	//后台管理系统删除User表内批量用户信息，跳转到用户列表页面（User、UserAuthority表修改exist字段为0）
	public void deleteMultiUser() {
		String deleteStr = get("deleteStr");
		String deleteId[] = deleteStr.split(",");
		UserService service = new UserService();
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean deleteMultiUser = false;
				for(String aString : deleteId) {
					deleteMultiUser = service.deleteOneUser(Integer.parseInt(aString));
					if(!deleteMultiUser) {
						return false;
					}
				}
				return true;
			}
		});
		if(succeed) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//后台管理系统恢复U单个用户信息，跳转到用户列表页面
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
	
	//后台管理系统恢复批量用户信息，跳转到用户列表页面
	public void recoveryMultiUser() {
		String recoveryStr = get("recoveryStr");
		String recoveryId[] = recoveryStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean recoveryMultiUser = false;
				UserService service = new UserService();
				for(String aString : recoveryId) {
					recoveryMultiUser = service.recoveryOneUser(Integer.parseInt(aString));
					if(!recoveryMultiUser) {
						return false;
					}
				}
				return true;
			}
		});
		if(succeed) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//后台管理系统彻底删除单个用户信息，跳转到用户列表页面
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
		
		boolean succeed = service.addUser(service.generateAccout(), nickName, password, "0.png", "", email, grade);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}

	//查询要修改的用户信息（指定id）
	public void getUpdateUserInfo() {
		int id = getInt("id");
		User user = new UserService().getUpdateUserInfo0(id);
		if(user != null) {
			setSessionAttr("user", user);
			renderText("succeed");
		}else {
			renderText("fail");
		}			
	}
	
	//修改用户信息
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
							newPhoto = photoName + "?" + cropService.generateRandom();
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
		String password = service.stringMD5(get("password"));
		
		boolean succeed = new UserService().updateUserPassword(accout, password);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}	
	}
	
	//修改用户土地1-18
	public void updateUserLand() {
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		List<Integer> land = new ArrayList<Integer>();
		List<Integer> list = new ArrayList<Integer>();
		String accout = get("accout");
		
		land.add(0);
		land.add(getInt("land1"));
		land.add(getInt("land2"));
		land.add(getInt("land3"));
		land.add(getInt("land4"));
		land.add(getInt("land5"));
		land.add(getInt("land6"));
		land.add(getInt("land7"));
		land.add(getInt("land8"));
		land.add(getInt("land9"));
		land.add(getInt("land10"));
		land.add(getInt("land11"));
		land.add(getInt("land12"));
		land.add(getInt("land13"));
		land.add(getInt("land14"));
		land.add(getInt("land15"));
		land.add(getInt("land16"));
		land.add(getInt("land17"));
		land.add(getInt("land18"));
		

		for(int i = 1;i < 19;i++) {
			if(land.get(i) != user.getInt("land"+i)) {
				list.add(user.getInt("land"+i));
			}
		}
		
		UserCropService userCropService = new UserCropService();
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				for(int value : list) {
					if(value != 0 && value != -1){
						isDelete = userCropService.deleteCrop(value);
						if(!isDelete) {
							return false;
						}
						System.out.println(value);
					}
				}
				isDelete = new UserService().updateLand1_18(accout, land.get(1), land.get(2), land.get(3), land.get(4), land.get(5), land.get(6),
						land.get(7), land.get(8), land.get(9), land.get(10), land.get(11), land.get(12), land.get(13), land.get(14), land.get(15), 
						land.get(16), land.get(17), land.get(18));
				if(isDelete) {
					return true;
				}
				return false;
			}
		});
			
		if(succeed) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
}
