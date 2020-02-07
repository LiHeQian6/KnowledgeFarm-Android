package com.farm.admin.controller;

import java.sql.SQLException;

import com.farm.admin.service.AdminService;
import com.farm.config.AdminInterceptor;
import com.farm.model.Admin;
import com.farm.user.service.UserService;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

public class AdminController extends Controller{
	
	//跳转到登录页面
	@Clear(AdminInterceptor.class)
	public void index() {
		renderJsp("/login.jsp");
	}
	
	//跳转到主页面
	public void gotoIndex() {
		renderJsp("/index.jsp");
	}
	
	//注销
	public void registAdmin() {
		renderJsp("/login.jsp");
		getSession().invalidate();
	}
	
	@Clear(AdminInterceptor.class)
	//管理员登陆（accout、password）
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
	
	//分页查询管理员信息，跳转到管理员列表页面（accout、pageNumber、pageSize、exist）
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
	
	//后台管理系统删除单个管理员信息，跳转到管理员列表页面（id）
	public void deleteOneAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().updateExist(id, 0);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//后台管理系统删除批量管理员信息，跳转到管理员列表页面（id字符串）
	public void deleteMultiAdmin() {
		String deleteStr = get("deleteStr");
		String deleteId[] = deleteStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a = true;
				AdminService service = new AdminService();
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
	
	//后台管理系统恢复单个管理员信息，跳转到管理员列表页面（id）
	public void recoveryOneAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().updateExist(id, 1);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//后台管理系统恢复批量管理员信息，跳转到管理员列表页面（id字符串）
	public void recoveryMultiAdmin() {
		String recoveryStr = get("recoveryStr");
		String recoveryId[] = recoveryStr.split(",");
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a = true;
				AdminService service = new AdminService();
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
	
	//后台管理系统彻底删除单个管理员信息，跳转到管理员列表页面（id）
	public void deleteThoroughAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().deleteThoroughAdmin(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//添加管理员信息（accout、password）
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
	
	//查询要修改的管理员信息（id）
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
	
	//修改管理员账号（oldAccout、newAccout）
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
	
	//修改管理员密码（accout、oldPassword、newPassword）
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
