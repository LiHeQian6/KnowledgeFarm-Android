package com.farm.user.controller;

import java.net.URLDecoder;

import org.json.JSONObject;

import com.farm.crop.service.CropService;
import com.farm.model.User;
import com.farm.user.service.UserService;
import com.farm.userbag.service.BagService;
import com.farm.usercrop.service.UserCropService;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

public class UserController extends Controller{
	
	//�û���¼
	public void loginByOpenId() {
		String jsonStr =  HttpKit.readData(getRequest());
		System.out.print(jsonStr);
		JSONObject jsonObject = new JSONObject(jsonStr);
		
		String openId = jsonObject.getString("openId");
		String nickName = jsonObject.getString("nickName");
		String photo = URLDecoder.decode(jsonObject.getString("photo"));
		
		System.out.println(jsonStr);
		System.out.println(jsonObject.toString());
		
		UserService service = new UserService();
		if(service.isExistUserByOpenId(openId)) { //�Ѵ��ڸ��û�����ѯ�û���Ϣ
			User user = service.findUserByOpenId(openId);
			renderJson(user);
		}else { 						  //�����ڸ��û�������û�
			boolean addUser = service.addUser(openId, nickName, photo, "QQ", openId);
			if(addUser) { 				  //������û��ɹ�
				User user = service.findUserByOpenId(openId);
				renderJson(user);
			}else { 					  //������û�ʧ��
				renderJson("fail");
			}
		}
	}
	
	//Tokenδ���ڣ�����openId��ѯ�û���Ϣ
	public void findByOpenId() {
		String jsonStr =  HttpKit.readData(getRequest());
		JSONObject jsonObject = new JSONObject(jsonStr);
		String openId = jsonObject.getString("openId");
		
		UserService service = new UserService();
		if(service.isExistUserByOpenId(openId)) { //��ЧopenId���ڣ��ɵ�¼
			User user = new UserService().findUserByOpenId(openId);
			renderJson(user);
		}else { //�ѱ���ţ���Ч��openId������
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
	
	//���˺�
	public void bindingAccout() {
		String accout = get("accout");
		String openId = get("openId");
		
		UserService service = new UserService();
		if(!service.isExistUserByOpenIdAll(openId)) {
			boolean succeed = service.addUserAuthority(service.getUserIdByAccout(accout), openId, "QQ");
			renderJson(""+succeed);
		}else { //��QQ���ѱ���
			renderJson("already");
		}
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
