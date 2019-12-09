package com.farm.user.controller;

import java.net.URLDecoder;
import java.sql.SQLException;

import org.json.JSONObject;

import com.farm.crop.service.CropService;
import com.farm.entity.Strings;
import com.farm.model.User;
import com.farm.user.service.UserService;
import com.farm.userbag.service.BagService;
import com.farm.usercrop.service.UserCropService;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class UserController extends Controller{
	
	//�û���¼(QQ)
	public void loginByOpenId() {
		String jsonStr =  HttpKit.readData(getRequest());
		JSONObject jsonObject = new JSONObject(jsonStr);
		String openId = jsonObject.getString("openId");
		
		UserService service = new UserService();
		if(service.isExistUserByOpenIdAll(openId)) { //���ڸ�openId�û�
			if(service.isExistUserByOpenId(openId)) { //������Ч�û�����ѯ�û���Ϣ
				User user = service.findUserByOpenId(openId);
				renderJson(user);
			}else { //���ڷ���Ч��openId�û�
				renderJson("notEffect");
			}
		}else { //�����ڸ�openId�û�
			renderJson("notExist");
		}
	}	
	
	//������֤�룬�����ؿͻ���
	public void sendTestCode() {
		String email = get("email");
		
		UserService service = new UserService();
		String testCode = "";
		for(int i = 0;i < 4;i++) {
			testCode = "" + (int)(Math.random()*10);
		}
		String text = "�����ڰ��������֤��Ϊ" + testCode + "��1��������Ч�������Ʊ��ܣ������������";
		service.sendEmail(email, text);
		renderJson(testCode);
	}
	
	//�û���һ�ε�¼QQ������û�
	public void addQQUser() {
		String openId = get("openId");
		String nickName = get("nickName");
		String photo = URLDecoder.decode(get("photo"));
		int grade = getInt("grade");
		String email = get("email");
		String password = get("password");
		
		UserService service = new UserService();
		if(email == null) {
			email = "";
		}
		if(service.addUser(openId, nickName, password, photo, "", email, grade, "QQ")) {
			User user = service.findUserByOpenId(openId);
			renderJson(user);
		}else {
			renderJson("fail");
		}
	}
	
	//�����˺ŵ�¼
	public void loginByAccount() {

		String account = get("account");
		String pwd = get("pwd");
		
		UserService service = new UserService();
		if(service.isExistUserByAccoutAll(account)) { //���ڸ��˺�
			if(service.isExistUserByAccout(account)) { //������Ч���˺�
				User user = service.findUserByAccountPassword(account, pwd);
				if(user != null) {
					renderJson(user);
				}else {
					renderJson("PasswordError");
				}
			}else { //���ڷ���Ч���˺�
				renderJson("notEffect");
			}
		}else { //�����ڸ��˺�
			renderJson("notExist");
		}
	}
	
	//ע���˺�
	public void registAccout() {
		String nickName = get("nickName");
		String photo = URLDecoder.decode(get("photo"));
		int grade = getInt("grade");
		String email = get("email");
		String password = get("password");
		
		UserService service = new UserService();
		if(email == null) {
			email = "";
		}
		String accout = service.generateAccout();
		if(service.addUser(accout, nickName, password, photo, "", email, grade)) {
			User user = service.findUserByAccout(accout);
			renderJson(user);
		}else {
			renderJson("fail");
		}
	}
	
	//�޸��û��ǳ�
	public void updateUserNickName() {
		String accout = get("accout");
		String nickName = get("nickName");
		
		boolean succeed = new UserService().updateUserNickName(accout, nickName);
		renderJson(""+succeed);
	}
	
	//�޸��û��꼶
	public void updateUserGrade() {
		String accout = get("accout");
		int grade = getInt("grade");
		
		boolean succeed = new UserService().updateUserGrade(accout, grade);
		renderJson(""+succeed);
	}
	
	//�޸�����
	public void updateUserPassword() {
		String oldPassword = get("oldPassword");
		String newPassword = get("newPassword");
		String accout = get("accout");
		
		//����0˵����������󣬷���1˵���޸ĳɹ�������2˵���޸�ʧ��
		int flag = new UserService().updateUserPassword(oldPassword, newPassword, accout);
		renderJson(flag);
	}
	
	//��֤�Ƿ��Ѿ���QQ
	public void isBindingQQ() {
		String accout = get("accout");
		
		boolean succeed = new UserService().isBindingQQ(accout);
		renderJson(""+succeed);
	}
	
	//��QQ
	public void bindingQQ() {
		String accout = get("accout");
		String openId = get("openId");
		String photo = URLDecoder.decode(get("photo"));
		
		UserService service = new UserService();
		if(!service.isExistUserByOpenIdAll(openId)) {
			
			boolean succeed = Db.tx(new IAtom() {
				@Override
				public boolean run() throws SQLException {
					boolean a1 = service.addUserAuthority(service.getUserIdByAccout(accout), openId, "QQ"); //��ӵ���Ȩ��
					boolean a2 = service.updateUserPhoto(accout, photo); //�޸ĳ�QQͷ��
					if(a1 && a2) {
						return true;
					}
					return false;
				}
			});
			
			renderJson(""+succeed);
			
		}else { //��QQ���ѱ���
			renderJson("already");
		}
	}
	
	//���QQ
	public void unBindingQQ() {
		String accout = get("accout");
		
		UserService service = new UserService();
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a1 = service.deleteOpenIdByUserId(service.getUserIdByAccout(accout)); //����Ȩ��ɾ��
				boolean a2 = new UserService().updateUserPhoto(accout, Strings.userPhotoUrl+"0.png"); //�޸ĳ�Ĭ��ͷ��
				if(a1 && a2) {
					return true;
				}
				return false;
			}
		});
		
		renderJson(""+succeed);
	}
	
	//���ӽ�ˮ��ʩ�ʴ�����userId��������
	public void addUserWater() {
		int userId = getInt("userId");
		int water = getInt("water");
		int fertilizer = getInt("fertilizer");
		
		//����Ϊ"-1":����ʧ�ܣ�����Ϊ"��-1":ʣ�ཱ������
		int rewardCount = new UserService().addWaterAndFer(userId, water, fertilizer);
		renderJson(rewardCount);
	}
	
	//��ѯʣ�ཱ������
	public void getRewardCount() {
		int userId = getInt("userId");
		renderJson(""+new UserService().getRewardCount(userId));
	}

	//��ˮ��userId��
	public void waterCrop() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		
		//progressΪ���ﵱǰ���ȣ���Ϊ-1����ˮʧ��
		int progress = new UserCropService().waterCr(userId, landNumber);
		renderJson(progress);
	}
	
	//ʩ�ʣ�userId��
	public void fertilizerCrop() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		
		//progressΪ���ﵱǰ���ȣ���Ϊ-1����ʩ��ʧ��
		int progress = new UserCropService().fertilizerCr(userId, landNumber);
		renderJson(progress);
	}
	
	//�������Ӻ���ӵ�����
	public void buyCrop() {
		int userId = getInt("userId");
		int cropId = getInt("cropId");
		int number = getInt("number");
		
		int userMoney = new UserService().getUpdateUserInfo(userId).getInt("money");
		int cropPrice = new CropService().getUpdateCropInfo(cropId).getInt("price");
		BagService service = new BagService();
		boolean succeed = false;
		if(userMoney >= cropPrice*number) { //��Ǯ
			if(service.isExistCrop(userId, cropId)) { //������������
				succeed = service.addCropNumber(userId, cropId, number);
			}else { //�����������Ϣ
				succeed = service.addToBag(userId, cropId, number);
			}
			renderJson(""+succeed);
		}else { //ûǮ
			renderJson("notEnoughMoney");
		}	
	}
	
	//��ֲ����
	public void raiseCrop() {
		int userId = getInt("userId");
		int cropId = getInt("cropId");
		String landNumber = get("landNumber");
		
		//����Ϊ"true":��ֲ�ɹ�������Ϊ"false":��ֲʧ��
		boolean succeed = new UserCropService().raiseNewCrop(userId, landNumber, cropId);
		renderJson(""+succeed);
	}
	
	//�ջ�
	public void harvest() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");

		//����Ϊ"true":�ջ�ɹ�������Ϊ"false":�ջ�ʧ��
		boolean succeed = new UserCropService().getCrop(userId, landNumber);
		renderJson(""+succeed);
	}
	
	
}
