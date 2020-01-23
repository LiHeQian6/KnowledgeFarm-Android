package com.farm.admin.dao;

import java.util.List;

import com.farm.model.Admin;
import com.jfinal.plugin.activerecord.Page;

public class AdminDao {
	
	
	/**
	 * 	增
	 * @throws
	 */
	//admin表插入数据
	public boolean addAdmin(String accout, String password) {
		boolean succeed = new Admin().set("accout", accout).set("password", password).save();
		return succeed;
	}
	
	
	
	
	/**
	 * 	删
	 * @throws
	 */
	//admin表删除整条数据
	public boolean deleteThoroughAdmin(int id) {
		boolean succeed = Admin.dao.deleteById(id);
		return succeed;
	}
	
	
	
	
	/**
	 * 	改
	 * @throws
	 */
	//admin表修改accout（指定exist=1）
	public boolean updateAdminAccout(String oldAccout, String newAccout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",oldAccout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("accout", newAccout).update();
		}
		return succeed;
	}
	//admin表判断旧password是否正确，修改password（指定exist=1）
	public int updateAdminPassword(String oldPassword, String newPassword, String accout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",accout);
		if(list.size() != 0) {
			Admin admin = list.get(0);
			if(oldPassword.equals(""+admin.get("password"))) {
				boolean succeed = admin.set("password", newPassword).update();
				if(succeed) {
					return 1;
				}
				return 2;
			}
			return 0;
		}
		return -1;
	}
	//admin表修改exist
	public boolean updateExist(int id, int exist) {
		boolean succeed = Admin.dao.findById(id).set("exist", exist).update();
		return succeed;
	}

	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//admin表查看是否存在该条数据（指定accout）
	public boolean isExistAdminByAccoutAll(String accout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=?",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//admin表查看是否存在该条数据（指定accout，exist=1）
	public boolean isExistAdminByAccout(String accout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//admin表查看是否存在该条数据，除accout2外（指定accout1）
	public boolean isExistAdminByAccout(String accout1, String accout2) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and accout!=?",accout1,accout2);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//admin表查询数据（指定accout、password）
	public Admin loginAdmin(String accout,String password) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and password=? and exist=1",accout,password);
		if(list.size() != 0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	//admin表分页查询数据（指定accout、pageNumber、pageSize、exist）
	public Page<Admin> findAdminPage(int pageNumber,int everyCount,String accout,int exist) {
		Page<Admin> adminPage;
		if(accout == null || accout.equals("")) {
			adminPage = Admin.dao.paginate(pageNumber, everyCount, "select *","from admin where exist=?",exist);	
		}else {
			adminPage = Admin.dao.paginate(pageNumber, everyCount, "select *","from admin where exist=? and accout like?",exist,"%"+accout+"%");
		}
		return adminPage;
	}	
	//admin表查询数据（指定id）
	public Admin getUpdateAdminInfo(int id) {
		Admin admin = Admin.dao.findById(id);
		return admin;
	}

}
