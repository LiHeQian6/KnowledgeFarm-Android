package com.farm.admin.service;

import com.farm.admin.dao.AdminDao;
import com.farm.model.Admin;
import com.jfinal.plugin.activerecord.Page;

public class AdminService {
	
	/**
	 * 	增
	 * @throws
	 */
	//添加管理员信息
	public boolean addAdmin(String accout, String password) {
		return new AdminDao().addAdmin(accout, password);
	}
	
	
	
	
	/**
	 * 	删
	 * @throws
	 */
	//后台管理系统彻底删除单个管理员信息
	public boolean deleteThoroughAdmin(int id) {
		return new AdminDao().deleteThoroughAdmin(id);
	}
	
	
	
	
	/**
	 * 	改
	 * @throws
	 */
	//修改管理员账号
	public boolean updateAdminAccout(String oldAccout, String newAccout) {
		return new AdminDao().updateAdminAccout(oldAccout, newAccout);
	}
	//判断旧密码是否正确，修改管理员密码
	public int updateAdminPassword(String oldPassword, String newPassword, String accout) {
		return new AdminDao().updateAdminPassword(oldPassword, newPassword, accout);
	}
	//后台管理系统修改单个管理员信息的exist
	public boolean updateExist(int id, int exist) {
		return new AdminDao().updateExist(id, exist);
	}
	
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//查看是否存在该管理员（指定accout）
	public boolean isExistAdminByAccoutAll(String accout) {
		return new AdminDao().isExistAdminByAccoutAll(accout);
	}
	//查看是否存在该管理员（指定accout，exist=1）
	public boolean isExistAdminByAccout(String accout) {
		return new AdminDao().isExistAdminByAccout(accout);
	}
	//查看是否存在该管理员，除指定accout2外（指定accout1）
	public boolean isExistAdminByAccout(String accout1, String accout2) {
		return new AdminDao().isExistAdminByAccout(accout1, accout2);
	}
	//验证管理员的登陆账号、密码（指定accout、password）
	public Admin loginAdmin(String accout,String password) {
		return new AdminDao().loginAdmin(accout,password);
	}
	//分页查询管理员信息（指定accout、pageNumber、pageSize、exist）
	public Page<Admin> findAdminPage(int pageNumber,int everyCount,String accout,int exist) {
		return new AdminDao().findAdminPage(pageNumber,everyCount,accout,exist);
	}	
	//获取到要修改的管理员信息（指定id）
	public Admin getUpdateAdminInfo(int id) {
		return new AdminDao().getUpdateAdminInfo(id);
	}

}
