package com.farm.admin.dao;

import java.util.List;

import com.farm.model.Admin;
import com.jfinal.plugin.activerecord.Page;

public class AdminDao {
	
	
	/**
	 * 	��
	 * @throws
	 */
	//��ӹ���Ա��Ϣ
	public boolean addAdmin(String accout, String password) {
		boolean succeed = new Admin().set("accout", accout).set("password", password).save();
		return succeed;
	}
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	//����ɾ��Admin���ڹ���Ա��Ϣ��Admin��delete��
	public boolean deleteThoroughAdmin(int id) {
		boolean succeed = Admin.dao.deleteById(id);
		return succeed;
	}
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�޸Ĺ���Ա��Ϣ���˺ţ��������޸�ǰ�˺�������
	public boolean updateAdminAccout(String oldAccout, String newAccout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",oldAccout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("accout", newAccout).update();
		}
		return succeed;
	}
	//�޸Ĺ���Ա��Ϣ�����룩�������޸�ǰ�˺�������
	public boolean updateAdminPassword(String accout, String password) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",accout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("password", password).update();
		}
		return succeed;
	}
	//ɾ��Admin���ڵ�������Ա��Ϣ��Admin���޸�exist�ֶ�Ϊ0��
	public boolean deleteOneAdmin(int id) {
		boolean succeed = Admin.dao.findById(id).set("exist", 0).update();
		return succeed;
	}
	//�ָ�Admin���ڵ�������Ա��Ϣ��Admin���޸�exist�ֶ�Ϊ1��
	public boolean recoveryOneAdmin(int id) {
		boolean succeed = Admin.dao.findById(id).set("exist", 1).update();
		return succeed;
	}
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�鿴�Ƿ���ڸù���Աexist=1��Admin��
	public boolean isExistAdminByAccout(String accout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and exist=1",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//�鿴�Ƿ���ڸù���Ա��Admin��
	public boolean isExistAdminByAccoutAll(String accout) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=?",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//�鿴�Ƿ���ڸù���Ա����ָ���˺��⣨Admin��
	public boolean isExistAdminByAccout(String accout1, String accout2) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and accout!=?",accout1,accout2);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//��֤����Ա�ĵ�½�˺š����루Admin��
	public Admin loginAdmin(String accout,String password) {
		List<Admin> list = Admin.dao.find("select * from admin where accout=? and password=? and exist=1",accout,password);
		if(list.size() != 0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	//��ѯAdmin�������й���Ա��Ϣ��Admin��
	public Page<Admin> findAdminPage(int pageNumber,int everyCount,String accout,int exist) {
		Page<Admin> adminPage;
		if(accout == null || accout.equals("")) {
			adminPage = Admin.dao.paginate(pageNumber, everyCount, "select *","from admin where exist=?",exist);	
		}else {
			adminPage = Admin.dao.paginate(pageNumber, everyCount, "select *","from admin where exist=? and accout like?",exist,"%"+accout+"%");
		}
		return adminPage;
	}	
	//���ݹ���Աid��ȡ��Ҫ�޸ĵĹ���Ա��Ϣ
	public Admin getUpdateAdminInfo(int id) {
		Admin admin = Admin.dao.findById(id);
		return admin;
	}

}
