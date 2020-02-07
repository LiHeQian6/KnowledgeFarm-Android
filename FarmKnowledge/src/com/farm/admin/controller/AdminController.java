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
	
	//��ת����¼ҳ��
	@Clear(AdminInterceptor.class)
	public void index() {
		renderJsp("/login.jsp");
	}
	
	//��ת����ҳ��
	public void gotoIndex() {
		renderJsp("/index.jsp");
	}
	
	//ע��
	public void registAdmin() {
		renderJsp("/login.jsp");
		getSession().invalidate();
	}
	
	@Clear(AdminInterceptor.class)
	//����Ա��½��accout��password��
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
	
	//��ҳ��ѯ����Ա��Ϣ����ת������Ա�б�ҳ�棨accout��pageNumber��pageSize��exist��
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
	
	//��̨����ϵͳɾ����������Ա��Ϣ����ת������Ա�б�ҳ�棨id��
	public void deleteOneAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().updateExist(id, 0);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//��̨����ϵͳɾ����������Ա��Ϣ����ת������Ա�б�ҳ�棨id�ַ�����
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
	
	//��̨����ϵͳ�ָ���������Ա��Ϣ����ת������Ա�б�ҳ�棨id��
	public void recoveryOneAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().updateExist(id, 1);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//��̨����ϵͳ�ָ���������Ա��Ϣ����ת������Ա�б�ҳ�棨id�ַ�����
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
	
	//��̨����ϵͳ����ɾ����������Ա��Ϣ����ת������Ա�б�ҳ�棨id��
	public void deleteThoroughAdmin() {
		int id = getInt("id");
		boolean succeed = new AdminService().deleteThoroughAdmin(id);
		if(succeed == true) {
			renderText("succeed");
		}else {
			renderText("fail");
		}
	}
	
	//��ӹ���Ա��Ϣ��accout��password��
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
	
	//��ѯҪ�޸ĵĹ���Ա��Ϣ��id��
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
	
	//�޸Ĺ���Ա�˺ţ�oldAccout��newAccout��
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
	
	//�޸Ĺ���Ա���루accout��oldPassword��newPassword��
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
