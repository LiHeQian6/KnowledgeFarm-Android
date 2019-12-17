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
	
	//��ת����¼ҳ��
	public void index() {
		renderJsp("/login.jsp");
	}
	
	//ע��
	public void registAdmin() {
		renderJsp("/login.jsp");
		getSession().invalidate();
	}
	
	//��ת����ҳ��
	public void gotoIndex() {
		renderJsp("/index.jsp");
	}			
	
	//����Ա��½
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
	
	//��ѯAdmin�������й���Ա��Ϣ����ת������Ա�б�ҳ�棨Admin��
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
	
	//ɾ��Admin���ڵ�������Ա��Ϣ����ת������Ա�б�ҳ�棨Admin���޸�exist�ֶ�Ϊ0��
	public void deleteOneAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().deleteOneAdmin(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//ɾ��Admin������������Ա��Ϣ����ת������Ա�б�ҳ�棨Admin���޸�exist�ֶ�Ϊ0��
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
	
	//�ָ�Admin���ڵ�������Ա��Ϣ����ת������Ա�б�ҳ�棨Admin���޸�exist�ֶ�Ϊ1��
	public void recoveryOneAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().recoveryOneAdmin(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//�ָ�Admin������������Ա��Ϣ����ת������Ա�б�ҳ�棨Admin���޸�exist�ֶ�Ϊ1��
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
	
	//����ɾ��Admin���ڹ���Ա��Ϣ����ת������Ա�б�ҳ�棨Admin��delete��
	public void deleteThoroughAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().deleteThoroughAdmin(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//��ӹ���Ա��Ϣ
	public void addAdmin() {
		String accout = get("accout");
		String password = new UserService().stringMD5(get("password"));
		
		AdminService service = new AdminService();
		if(!service.isExistAdminByAccoutAll(accout)) { //�����ڸù���Ա�˻����������
			boolean succeed = service.addAdmin(accout, password);
			if(succeed == true) {
				renderText("succeed");
			}else {
				renderText("fail");
			}
		}else { //�Ѵ��ڸù���Ա�˻����������
			renderText("already");
		}		
	}
	
	//���ݹ���Աid��ȡ��Ҫ�޸ĵĹ���Ա��Ϣ
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
	
	//�޸Ĺ���Ա��Ϣ���˺ţ��������޸�ǰ�˺�������
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
		}else { //�Ѵ��ڸ��˺ţ������޸�
			renderText("already");
		}	
	}
	
	//�޸Ĺ���Ա��Ϣ�����룩�����ݹ���Ա�˺�������
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
