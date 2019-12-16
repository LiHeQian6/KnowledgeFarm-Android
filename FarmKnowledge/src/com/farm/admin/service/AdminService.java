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
	//彻底删除Admin表内管理员信息（Admin表delete）
	public boolean deleteThoroughAdmin(int id) {
		return new AdminDao().deleteThoroughAdmin(id);
	}
	
	
	/**
	 * 	改
	 * @throws
	 */
	//修改管理员信息（账号），根据修改前账号索引到
	public boolean updateAdminAccout(String oldAccout, String newAccout) {
		return new AdminDao().updateAdminAccout(oldAccout, newAccout);
	}
	//判断旧密码是否正确，修改管理员密码，根据账号查询到
	public int updateAdminPassword(String oldPassword, String newPassword, String accout) {
		return new AdminDao().updateAdminPassword(oldPassword, newPassword, accout);
	}
	//删除Admin表内单个管理员信息（Admin表修改exist字段为0）
	public boolean deleteOneAdmin(int id) {
		return new AdminDao().deleteOneAdmin(id);
	}
	//恢复Admin表内单个管理员信息（Admin表修改exist字段为1）
	public boolean recoveryOneAdmin(int id) {
		return new AdminDao().recoveryOneAdmin(id);
	}
	
	
	/**
	 * 	查
	 * @throws
	 */
	//查看是否存在该管理员exist=1（Admin表）
	public boolean isExistAdminByAccout(String accout) {
		return new AdminDao().isExistAdminByAccout(accout);
	}
	//查看是否存在该管理员（Admin表）
	public boolean isExistAdminByAccoutAll(String accout) {
		return new AdminDao().isExistAdminByAccoutAll(accout);
	}
	//查看是否存在该管理员，除指定账号外（Admin表）
	public boolean isExistAdminByAccout(String accout1, String accout2) {
		return new AdminDao().isExistAdminByAccout(accout1, accout2);
	}
	//验证管理员的登陆账号、密码（Admin表）
	public Admin loginAdmin(String accout,String password) {
		return new AdminDao().loginAdmin(accout,password);
	}
	//查询Admin表内所有管理员信息（Admin表）
	public Page<Admin> findAdminPage(int pageNumber,int everyCount,String accout,int exist) {
		return new AdminDao().findAdminPage(pageNumber,everyCount,accout,exist);
	}	
	//根据管理员id获取到要修改的管理员信息
	public Admin getUpdateAdminInfo(int id) {
		return new AdminDao().getUpdateAdminInfo(id);
	}

}
