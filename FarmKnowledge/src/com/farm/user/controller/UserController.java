package com.farm.user.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import com.farm.crop.service.CropService;
import com.farm.entity.Email;
import com.farm.entity.Strings;
import com.farm.model.User;
import com.farm.user.service.UserService;
import com.farm.userbag.service.BagService;
import com.farm.usercrop.service.UserCropService;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

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
	
	//����userId��ѯ�û���Ϣ
	public void findUserInfoByUserId() {
		int userId = getInt("userId");
		
		User user = new UserService().getUpdateUserInfo(userId);
		if(user != null) {
			renderJson(user);
		}else {
			renderJson("{}");
		}
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
		if(service.addUser(service.generateAccout(), openId, nickName, password, photo, "", email, grade, "QQ")) {
			User user = service.findUserByOpenId(openId);
			renderJson(user);
		}else {
			renderJson("fail");
		}
	}
	
	//�����˺ŵ�¼
	public void loginByAccount() {

		String account = get("accout");
		String password = get("password");
		
		UserService service = new UserService();
		if(service.isExistUserByAccoutAll(account)) { //���ڸ��˺�
			if(service.isExistUserByAccout(account)) { //������Ч���˺�
				User user = service.findUserByAccountPassword(account, password);
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
		int grade = getInt("grade");
		String email = get("email");
		String password = get("password");
		
		UserService service = new UserService();
		if(email == null) {
			email = "";
		}
		String accout = service.generateAccout();
		if(service.addUser(accout, nickName, password, Strings.userPhotoUrl + "0.png", "", email, grade)) {
			User user = service.findUserByAccout(accout);
			renderJson(user);
		}else {
			renderJson("fail");
		}
	}
	
	//������֤�������һ����룬��������֤��
	public void sendTestCodePassword() {
		String accout = get("accout");
		String email = get("email");
		
		UserService service = new UserService();
		if(service.isExistUserByAccoutAll(accout)) {
			String userEmail = service.findUserByAccout(accout).get("email");
			if(!userEmail.equals("")) {
				if(userEmail.equals(email)) {
					if(Email.findPasswordByMail(email)) {
						renderJson(Email.getCode());
					}else {
						renderJson("fail");
					}
				}else {
					renderJson("EmailError");
				}
			}else {
				renderJson("notBindingEmail");
			}
		}else {
			renderJson("notExistAccount");
		}
	}

	//�һ����루���¸��˺��������룩
	public void resetUserPassword() {
		String accout = get("accout");
		String password = get("password");
		
		boolean succeed = new UserService().updateUserPassword(accout, password);
		renderJson(""+succeed);
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
		String password = get("password");
		String accout = get("accout");
		
		//����0˵����������󣬷���1˵���޸ĳɹ�������2˵���޸�ʧ��
		boolean succeed = new UserService().updateUserPassword(accout, password);
		renderJson(""+succeed);
	}
	
	//�޸�ͷ��
	public void updatePhoto() {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		HttpServletRequest request = getRequest();
		
		String accout = "";
		String photo = "";
		String photoName = "";
		String newPhoto = "";
		
		UserService service = new UserService();
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					switch (fi.getFieldName()) {
						case "accout":
							accout = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
							break;
						case "photo":
							photo = URLDecoder.decode(new String(fi.getString().getBytes("ISO8859_1"),"utf-8"));
							break;
						case "photoName":
							photoName = new String(fi.getString().getBytes("ISO8859_1"),"utf-8");
							break;
					}	
				}else {
					//�ж�photoName��MIMETYPE�����Ƿ���ͼƬ�����������¹��죬���ж��Ƿ��������û���photoName�ظ�
					CropService cropService = new CropService();
					if(request.getServletContext().getMimeType(photoName) == null) {
						do {
							photoName = "";
							photoName = cropService.generateRandom() + fi.getName();
						} while (service.isExistPhotoName(photoName));
					}
					
					//����photo�����ж��Ƿ����ϴε�photo�ظ�
					do {
						newPhoto = "";
						newPhoto = Strings.userPhotoUrl + photoName + "?" + cropService.generateRandom();
					} while (newPhoto.equals(photo));
					
					//��ͷ��д���ļ�
					File file = new File(Strings.userfilePath + photoName);
					fi.write(file);
					
					boolean succeed = service.updateUserPhoto(accout, newPhoto, photoName);
					if(succeed) {
						User user = service.findUserByAccout(accout);
						if(user != null) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("photo", user.get("photo"));
							map.put("photoName", user.get("photoName"));
							renderJson(map);
						}else {
							renderJson("");
						}
					}else {
						renderJson("false");
					}
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
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
		
		UserService service = new UserService();
		if(!service.isExistUserByOpenIdAll(openId)) {
			boolean succeed = service.addUserAuthority(service.getUserIdByAccout(accout), openId, "QQ"); //��ӵ���Ȩ��
			renderJson(""+succeed);
		}else { //��QQ���ѱ���
			renderJson("already");
		}
	}
	
	//���QQ
	public void unBindingQQ() {
		String accout = get("accout");
		
		UserService service = new UserService();
		boolean succeed = service.deleteOpenIdByUserId(service.getUserIdByAccout(accout)); //����Ȩ��ɾ��
		renderJson(""+succeed);
	}
	
	//������֤�����ڰ����䣬��������֤��
	public void sendTestCodeBingEmail() {
		String email = get("email");
		
		UserService service = new UserService();
		if(!service.isBindingEmail(email)) {
			if(Email.bindingMail(email)) {
				renderJson(Email.getCode());
			}else {
				renderJson("fail");
			}
		}else { //�������ѱ������˺Ű�
			renderJson("already");
		}
	}
	
	//������
	public void bindingEmail() {
		String accout  = get("accout");
		String email = get("email");
		
		UserService service = new UserService();
		boolean succeed = service.updateUserEmail(accout, email);
		renderJson(""+succeed);
	}
	
	//�������
	public void unBindingEmail() {
		String accout = get("accout");
		
		UserService service = new UserService();
		boolean succeed = service.updateUserEmail(accout, "");
		renderJson("" +succeed);
	}
	
	//���ٽ������������ӽ�ˮ��ʩ�ʴ���
	public void lessRewardCount() {
		int userId = getInt("userId");
		int water = getInt("water");
		int fertilizer = getInt("fertilizer");
		String subject = get("subject");
		
		//����Ϊ"-1":����ʧ�ܣ�����Ϊ"��-1":ʣ�ཱ������
		int rewardCount = new UserService().lessRewardCount(userId, water, fertilizer, subject);
		renderJson(rewardCount);
	}
	
	//��ѯʣ�ཱ������
	public void getRewardCount() {
		int userId = getInt("userId");
		String subject = get("subject");
		renderJson(""+new UserService().getUpdateUserInfo(userId).get(subject+"RewardCount"));
	}

	//��ˮ��userId��
	public void waterCrop() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		
		//progressΪ���ﵱǰ���ȣ���Ϊ-1����ˮʧ��
		boolean succeed = new UserCropService().waterCr(userId, landNumber);
		renderJson(""+succeed);
	}
	
	//ʩ�ʣ�userId��
	public void fertilizerCrop() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		
		//progressΪ���ﵱǰ���ȣ���Ϊ-1����ʩ��ʧ��
		boolean succeed = new UserCropService().fertilizerCr(userId, landNumber);
		renderJson(""+succeed);
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

		//����Ϊ"up":�ջ�ɹ�������;����Ϊ"true":�ջ�ɹ�;����Ϊ"false":�ջ�ʧ��
		String result = new UserCropService().getCrop(userId, landNumber);
		renderJson(result);
	}
	
	//��������
	public void extensionLand() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		int needMoney = getInt("needMoney");
		
		UserService service = new UserService();
		int userMoney = new UserService().getUpdateUserInfo(userId).getInt("money");
		if(userMoney >= needMoney) {
			boolean succeed = service.extensionLand(userId, landNumber, needMoney);
			renderJson(""+succeed);
		}else {
			renderJson("notEnoughMoney");
		}
	}
	
	public void aaa() {
		int ucId = getInt("ucId");
		renderJson(new UserCropService().findUserCropById(ucId));
	}
	
}
