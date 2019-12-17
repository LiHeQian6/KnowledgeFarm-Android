package com.farm.admin.controller;

import java.sql.SQLException;

import com.farm.admin.service.AdminService;
import com.farm.model.Admin;
import com.farm.user.service.UserService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

public class AdminController extends Controller{
	
	//跳转到登录页面
	public void index() {
		renderJsp("/login.jsp");
	}
	
	//注销
	public void registAdmin() {
		renderJsp("/login.jsp");
		getSession().invalidate();
	}
	
	//跳转到主页面
	public void gotoIndex() {
		renderJsp("/index.jsp");
	}			
	
	//管理员登陆
	public void login() {
		String accout = get("accout");
		String password = new UserService().stringMD5(get("password"));
		AdminService service = new AdminService();
		boolean isExist = service.isExistAdminByAccout(accout);
		if(isExist) {
			Admin admin = service.loginAdmin(accout, password);
			if(admin != null) {
				setSessionAttr("admin",admin);
				renderText("succeed");
			}else {
				renderText("fail");
			}
		}else {
			renderText("notExist");
		}
	}
	
	//查询Admin表内所有管理员信息，跳转到管理员列表页面（Admin表）
	public void findAdminPage() {
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
		
		removeSessionAttr("adminInfo");
		Page<Admin> list = new AdminService().findAdminPage(pageNumber,everyCount,accout,exist);
		setAttr("adminPage", list);
		if(exist == 1) {
			renderJsp("/admin-list.jsp");
		}else {
			renderJsp("/admin-del.jsp");
		}
	}
	
	//删除Admin表内单个管理员信息，跳转到管理员列表页面（Admin表修改exist字段为0）
	public void deleteOneAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().deleteOneAdmin(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//删除Admin表内批量管理员信息，跳转到管理员列表页面（Admin表修改exist字段为0）
	public void deleteMultiAdmin() {
		String deleteStr = get("deleteStr");
		String deleteId[] = deleteStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a = true;
				AdminService service = new AdminService();
				for(String aString : deleteId) {
					a = service.deleteOneAdmin(Integer.parseInt(aString));
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
	
	//恢复Admin表内单个管理员信息，跳转到管理员列表页面（Admin表修改exist字段为1）
	public void recoveryOneAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().recoveryOneAdmin(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//恢复Admin表内批量管理员信息，跳转到管理员列表页面（Admin表修改exist字段为1）
	public void recoveryMultiAdmin() {
		String recoveryStr = get("recoveryStr");
		String recoveryId[] = recoveryStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a = true;
				AdminService service = new AdminService();
				for(String aString : recoveryId) {
					a = service.recoveryOneAdmin(Integer.parseInt(aString));
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
	
	//彻底删除Admin表内管理员信息，跳转到管理员列表页面（Admin表delete）
	public void deleteThoroughAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().deleteThoroughAdmin(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//添加管理员信息
	public void addAdmin() {
		String accout = get("accout");
		String password = new UserService().stringMD5(get("password"));
		
		AdminService service = new AdminService();
		if(!service.isExistAdminByAccoutAll(accout)) { //不存在该管理员账户，可以添加
			boolean succeed = service.addAdmin(accout, password);
			if(succeed == true) {
				renderText("succeed");
			}else {
				renderText("fail");
			}
		}else { //已存在该管理员账户，不可添加
			renderText("already");
		}		
	}
	
	//根据管理员id获取到要修改的管理员信息
	public void getUpdateAdminInfo() {
		int id = getInt("id");
		Admin admin = new AdminService().getUpdateAdminInfo(id);
		if(admin != null) {
			setSessionAttr("adminInfo", admin);
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//修改管理员信息（账号），根据修改前账号索引到
	public void updateAdminAccout() {
		String oldAccout = get("oldAccout");
		String newAccout = get("newAccout");
		
		AdminService service = new AdminService();
		if(!service.isExistAdminByAccout(oldAccout,newAccout)) {
			boolean succeed = service.updateAdminAccout(oldAccout, newAccout);
			if(succeed == true) {
				renderText("succeed");
			}else {
				renderText("fail");
			}
		}else { //已存在该账号，不可修改
			renderText("already");
		}	
	}
	
	//修改管理员信息（密码），根据管理员账号索引到
	public void updateAdminPassword() {
		UserService service = new UserService();
		String accout = get("accout");
		String oldPassword = service.stringMD5(get("oldPassword"));
		String newPassword = service.stringMD5(get("newPassword"));
		
		AdminService adminService = new AdminService();
		int result = adminService.updateAdminPassword(oldPassword, newPassword, accout);
		if(result == 0) {
			renderText("PasswordError");
		}else if(result == 1){
			renderText("succeed");
		}else if(result == 2){
			renderText("fail");
		}
	}
	
}
