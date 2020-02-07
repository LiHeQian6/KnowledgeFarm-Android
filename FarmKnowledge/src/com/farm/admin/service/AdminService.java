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
	//��̨����ϵͳ����ɾ����������Ա��Ϣ
	public boolean deleteThoroughAdmin(int id) {
		return new AdminDao().deleteThoroughAdmin(id);
	}
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�޸Ĺ���Ա�˺�
	public boolean updateAdminAccout(String oldAccout, String newAccout) {
		return new AdminDao().updateAdminAccout(oldAccout, newAccout);
	}
	//�жϾ������Ƿ���ȷ���޸Ĺ���Ա����
	public int updateAdminPassword(String oldPassword, String newPassword, String accout) {
		return new AdminDao().updateAdminPassword(oldPassword, newPassword, accout);
	}
	//��̨����ϵͳ�޸ĵ�������Ա��Ϣ��exist
	public boolean updateExist(int id, int exist) {
		return new AdminDao().updateExist(id, exist);
	}
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�鿴�Ƿ���ڸù���Ա��ָ��accout��
	public boolean isExistAdminByAccoutAll(String accout) {
		return new AdminDao().isExistAdminByAccoutAll(accout);
	}
	//�鿴�Ƿ���ڸù���Ա��ָ��accout��exist=1��
	public boolean isExistAdminByAccout(String accout) {
		return new AdminDao().isExistAdminByAccout(accout);
	}
	//�鿴�Ƿ���ڸù���Ա����ָ��accout2�⣨ָ��accout1��
	public boolean isExistAdminByAccout(String accout1, String accout2) {
		return new AdminDao().isExistAdminByAccout(accout1, accout2);
	}
	//��֤����Ա�ĵ�½�˺š����루ָ��accout��password��
	public Admin loginAdmin(String accout,String password) {
		return new AdminDao().loginAdmin(accout,password);
	}
	//��ҳ��ѯ����Ա��Ϣ��ָ��accout��pageNumber��pageSize��exist��
	public Page<Admin> findAdminPage(int pageNumber,int everyCount,String accout,int exist) {
		return new AdminDao().findAdminPage(pageNumber,everyCount,accout,exist);
	}	
	//��ȡ��Ҫ�޸ĵĹ���Ա��Ϣ��ָ��id��
	public Admin getUpdateAdminInfo(int id) {
		return new AdminDao().getUpdateAdminInfo(id);
	}

}
