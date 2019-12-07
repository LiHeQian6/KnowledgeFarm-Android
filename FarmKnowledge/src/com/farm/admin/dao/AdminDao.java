package com.farm.admin.dao;

import java.util.List;

import com.farm.model.Admin;
import com.jfinal.plugin.activerecord.Page;

public class AdminDao {
	
	
	/**
	 * 	增
	 * @throws
	 */
	//添加管理员信息
	public boolean addAdmin(String accout, String password) {
		boolean succeed = new Admin().set("accout", accout).set("password", password).save();
		return succeed;
	}
	
	
	/**
	 * 	删
	 * @throws
	 */
	//彻底删除Admin表内管理员信息（Admin表delete）
	public boolean deleteThoroughAdmin(int id) {
		boolean succeed = Admin.dao.deleteById(id);
		return succeed;
	}
	
	
	
	/**
	 * 	改
	 * @throws
	 */
	//修改管理员信息（账号），根据修改前账号索引到
	public boolean updateAdminAccout(String oldAccout, String newAccout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",oldAccout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("accout", newAccout).update();
		}
		return succeed;
	}
	//修改管理员信息（密码），根据修改前账号索引到
	public boolean updateAdminPassword(String accout, String password) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",accout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("password", password).update();
		}
		return succeed;
	}
	//删除Admin表内单个管理员信息（Admin表修改exist字段为0）
	public boolean deleteOneAdmin(int id) {
		boolean succeed = Admin.dao.findById(id).set("exist", 0).update();
		return succeed;
	}
	//恢复Admin表内单个管理员信息（Admin表修改exist字段为1）
	public boolean recoveryOneAdmin(int id) {
		boolean succeed = Admin.dao.findById(id).set("exist", 1).update();
		return succeed;
	}
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//查看是否存在该管理员exist=1（Admin表）
	public boolean isExistAdminByAccout(String accout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//查看是否存在该管理员（Admin表）
	public boolean isExistAdminByAccoutAll(String accout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=?",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//查看是否存在该管理员，除指定账号外（Admin表）
	public boolean isExistAdminByAccout(String accout1, String accout2) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and accout!=?",accout1,accout2);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//验证管理员的登陆账号、密码（Admin表）
	public Admin loginAdmin(String accout,String password) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and password=? and exist=1",accout,password);
		if(list.size() != 0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	//查询Admin表内所有管理员信息（Admin表）
	public Page<Admin> findAdminPage(int pageNumber,int everyCount,String accout,int exist) {
		Page<Admin> adminPage;
		if(accout == null || accout.equals("")) {
			adminPage = Admin.dao.paginate(pageNumber, everyCount, "select *","from admin where exist=?",exist);	
		}else {
			adminPage = Admin.dao.paginate(pageNumber, everyCount, "select *","from admin where exist=? and accout like?",exist,"%"+accout+"%");
		}
		return adminPage;
	}	
	//根据管理员id获取到要修改的管理员信息
	public Admin getUpdateAdminInfo(int id) {
		Admin admin = Admin.dao.findById(id);
		return admin;
	}

}
