package com.farm.admin.service;

import com.farm.admin.dao.AdminDao;
import com.farm.model.Admin;
import com.jfinal.plugin.activerecord.Page;

public class AdminService {
	
	
	/**
	 * 	��
	 * @throws
	 */
	//��ӹ���Ա��Ϣ
	public boolean addAdmin(String accout, String password) {
		return new AdminDao().addAdmin(accout, password);
	}
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	//����ɾ��Admin���ڹ���Ա��Ϣ��Admin��delete��
	public boolean deleteThoroughAdmin(int id) {
		return new AdminDao().deleteThoroughAdmin(id);
	}
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�޸Ĺ���Ա��Ϣ���˺ţ��������޸�ǰ�˺�������
	public boolean updateAdminAccout(String oldAccout, String newAccout) {
		return new AdminDao().updateAdminAccout(oldAccout, newAccout);
	}
	//�жϾ������Ƿ���ȷ���޸Ĺ���Ա���룬�����˺Ų�ѯ��
	public int updateAdminPassword(String oldPassword, String newPassword, String accout) {
		return new AdminDao().updateAdminPassword(oldPassword, newPassword, accout);
	}
	//ɾ��Admin���ڵ�������Ա��Ϣ��Admin���޸�exist�ֶ�Ϊ0��
	public boolean deleteOneAdmin(int id) {
		return new AdminDao().deleteOneAdmin(id);
	}
	//�ָ�Admin���ڵ�������Ա��Ϣ��Admin���޸�exist�ֶ�Ϊ1��
	public boolean recoveryOneAdmin(int id) {
		return new AdminDao().recoveryOneAdmin(id);
	}
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�鿴�Ƿ���ڸù���Աexist=1��Admin��
	public boolean isExistAdminByAccout(String accout) {
		return new AdminDao().isExistAdminByAccout(accout);
	}
	//�鿴�Ƿ���ڸù���Ա��Admin��
	public boolean isExistAdminByAccoutAll(String accout) {
		return new AdminDao().isExistAdminByAccoutAll(accout);
	}
	//�鿴�Ƿ���ڸù���Ա����ָ���˺��⣨Admin��
	public boolean isExistAdminByAccout(String accout1, String accout2) {
		return new AdminDao().isExistAdminByAccout(accout1, accout2);
	}
	//��֤����Ա�ĵ�½�˺š����루Admin��
	public Admin loginAdmin(String accout,String password) {
		return new AdminDao().loginAdmin(accout,password);
	}
	//��ѯAdmin�������й���Ա��Ϣ��Admin��
	public Page<Admin> findAdminPage(int pageNumber,int everyCount,String accout,int exist) {
		return new AdminDao().findAdminPage(pageNumber,everyCount,accout,exist);
	}	
	//���ݹ���Աid��ȡ��Ҫ�޸ĵĹ���Ա��Ϣ
	public Admin getUpdateAdminInfo(int id) {
		return new AdminDao().getUpdateAdminInfo(id);
	}

}
