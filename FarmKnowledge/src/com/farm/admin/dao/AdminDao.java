package com.farm.admin.dao;

import java.util.List;

import com.farm.model.Admin;
import com.jfinal.plugin.activerecord.Page;

public class AdminDao {
	
	
	/**
	 * 	��
	 * @throws
	 */
	//admin���������
	public boolean addAdmin(String accout, String password) {
		boolean succeed = new Admin().set("accout", accout).set("password", password).save();
		return succeed;
	}
	
	
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	//admin��ɾ����������
	public boolean deleteThoroughAdmin(int id) {
		boolean succeed = Admin.dao.deleteById(id);
		return succeed;
	}
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//admin���޸�accout��ָ��exist=1��
	public boolean updateAdminAccout(String oldAccout, String newAccout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",oldAccout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("accout", newAccout).update();
		}
		return succeed;
	}
	//admin���жϾ�password�Ƿ���ȷ���޸�password��ָ��exist=1��
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
	//admin���޸�exist
	public boolean updateExist(int id, int exist) {
		boolean succeed = Admin.dao.findById(id).set("exist", exist).update();
		return succeed;
	}

	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//admin��鿴�Ƿ���ڸ������ݣ�ָ��accout��
	public boolean isExistAdminByAccoutAll(String accout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=?",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//admin��鿴�Ƿ���ڸ������ݣ�ָ��accout��exist=1��
	public boolean isExistAdminByAccout(String accout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//admin��鿴�Ƿ���ڸ������ݣ���accout2�⣨ָ��accout1��
	public boolean isExistAdminByAccout(String accout1, String accout2) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and accout!=?",accout1,accout2);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//admin���ѯ���ݣ�ָ��accout��password��
	public Admin loginAdmin(String accout,String password) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and password=? and exist=1",accout,password);
		if(list.size() != 0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	//admin���ҳ��ѯ���ݣ�ָ��accout��pageNumber��pageSize��exist��
	public Page<Admin> findAdminPage(int pageNumber,int everyCount,String accout,int exist) {
		Page<Admin> adminPage;
		if(accout == null || accout.equals("")) {
			adminPage = Admin.dao.paginate(pageNumber, everyCount, "select *","from admin where exist=?",exist);	
		}else {
			adminPage = Admin.dao.paginate(pageNumber, everyCount, "select *","from admin where exist=? and accout like?",exist,"%"+accout+"%");
		}
		return adminPage;
	}	
	//admin���ѯ���ݣ�ָ��id��
	public Admin getUpdateAdminInfo(int id) {
		Admin admin = Admin.dao.findById(id);
		return admin;
	}

}
