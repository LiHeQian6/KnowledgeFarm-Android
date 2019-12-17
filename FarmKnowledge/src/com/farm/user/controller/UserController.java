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
	
	//用户登录(QQ)
	public void loginByOpenId() {
		String jsonStr =  HttpKit.readData(getRequest());
		JSONObject jsonObject = new JSONObject(jsonStr);
		String openId = jsonObject.getString("openId");
		
		UserService service = new UserService();
		if(service.isExistUserByOpenIdAll(openId)) { //存在该openId用户
			if(service.isExistUserByOpenId(openId)) { //存在有效用户，查询用户信息
				User user = service.findUserByOpenId(openId);
				renderJson(user);
			}else { //存在非有效的openId用户
				renderJson("notEffect");
			}
		}else { //不存在该openId用户
			renderJson("notExist");
		}
	}	
	
	//根据userId查询用户信息
	public void findUserInfoByUserId() {
		int userId = getInt("userId");
		
		User user = new UserService().getUpdateUserInfo(userId);
		if(user != null) {
			renderJson(user);
		}else {
			renderJson("{}");
		}
	}
	
	//用户第一次登录QQ，添加用户
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
	
	//根据账号登录
	public void loginByAccount() {

		String account = get("accout");
		String password = get("password");
		
		UserService service = new UserService();
		if(service.isExistUserByAccoutAll(account)) { //存在该账号
			if(service.isExistUserByAccout(account)) { //存在有效的账号
				User user = service.findUserByAccountPassword(account, password);
				if(user != null) {
					renderJson(user);
				}else {
					renderJson("PasswordError");
				}
			}else { //存在非有效的账号
				renderJson("notEffect");
			}
		}else { //不存在该账号
			renderJson("notExist");
		}
	}
	
	//注册账号
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
	
	//发送验证码用于找回密码，并返回验证码
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

	//找回密码（重新给账号设置密码）
	public void resetUserPassword() {
		String accout = get("accout");
		String password = get("password");
		
		boolean succeed = new UserService().updateUserPassword(accout, password);
		renderJson(""+succeed);
	}
	
	//修改用户昵称
	public void updateUserNickName() {
		String accout = get("accout");
		String nickName = get("nickName");
		
		boolean succeed = new UserService().updateUserNickName(accout, nickName);
		renderJson(""+succeed);
	}
	
	//修改用户年级
	public void updateUserGrade() {
		String accout = get("accout");
		int grade = getInt("grade");
		
		boolean succeed = new UserService().updateUserGrade(accout, grade);
		renderJson(""+succeed);
	}
	
	//修改密码
	public void updateUserPassword() {
		String password = get("password");
		String accout = get("accout");
		
		//返回0说明旧密码错误，返回1说明修改成功，返回2说明修改失败
		boolean succeed = new UserService().updateUserPassword(accout, password);
		renderJson(""+succeed);
	}
	
	//修改头像
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
					//判断photoName的MIMETYPE类型是否是图片，若是则重新构造，并判断是否与其他用户的photoName重复
					CropService cropService = new CropService();
					if(request.getServletContext().getMimeType(photoName) == null) {
						do {
							photoName = "";
							photoName = cropService.generateRandom() + fi.getName();
						} while (service.isExistPhotoName(photoName));
					}
					
					//构造photo，并判断是否与上次的photo重复
					do {
						newPhoto = "";
						newPhoto = Strings.userPhotoUrl + photoName + "?" + cropService.generateRandom();
					} while (newPhoto.equals(photo));
					
					//把头像写入文件
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
	
	//验证是否已经绑定QQ
	public void isBindingQQ() {
		String accout = get("accout");
		
		boolean succeed = new UserService().isBindingQQ(accout);
		renderJson(""+succeed);
	}
	
	//绑定QQ
	public void bindingQQ() {
		String accout = get("accout");
		String openId = get("openId");
		
		UserService service = new UserService();
		if(!service.isExistUserByOpenIdAll(openId)) {
			boolean succeed = service.addUserAuthority(service.getUserIdByAccout(accout), openId, "QQ"); //添加到授权表
			renderJson(""+succeed);
		}else { //该QQ号已被绑定
			renderJson("already");
		}
	}
	
	//解绑QQ
	public void unBindingQQ() {
		String accout = get("accout");
		
		UserService service = new UserService();
		boolean succeed = service.deleteOpenIdByUserId(service.getUserIdByAccout(accout)); //从授权表删除
		renderJson(""+succeed);
	}
	
	//发送验证码用于绑定邮箱，并返回验证码
	public void sendTestCodeBingEmail() {
		String email = get("email");
		
		UserService service = new UserService();
		if(!service.isBindingEmail(email)) {
			if(Email.bindingMail(email)) {
				renderJson(Email.getCode());
			}else {
				renderJson("fail");
			}
		}else { //该邮箱已被其他账号绑定
			renderJson("already");
		}
	}
	
	//绑定邮箱
	public void bindingEmail() {
		String accout  = get("accout");
		String email = get("email");
		
		UserService service = new UserService();
		boolean succeed = service.updateUserEmail(accout, email);
		renderJson(""+succeed);
	}
	
	//解绑邮箱
	public void unBindingEmail() {
		String accout = get("accout");
		
		UserService service = new UserService();
		boolean succeed = service.updateUserEmail(accout, "");
		renderJson("" +succeed);
	}
	
	//减少奖励次数，增加浇水、施肥次数
	public void lessRewardCount() {
		int userId = getInt("userId");
		int water = getInt("water");
		int fertilizer = getInt("fertilizer");
		String subject = get("subject");
		
		//返回为"-1":操作失败；返回为"非-1":剩余奖励次数
		int rewardCount = new UserService().lessRewardCount(userId, water, fertilizer, subject);
		renderJson(rewardCount);
	}
	
	//查询剩余奖励次数
	public void getRewardCount() {
		int userId = getInt("userId");
		String subject = get("subject");
		renderJson(""+new UserService().getUpdateUserInfo(userId).get(subject+"RewardCount"));
	}

	//浇水（userId）
	public void waterCrop() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		
		//progress为作物当前进度；若为-1，则浇水失败
		boolean succeed = new UserCropService().waterCr(userId, landNumber);
		renderJson(""+succeed);
	}
	
	//施肥（userId）
	public void fertilizerCrop() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		
		//progress为作物当前进度；若为-1，则施肥失败
		boolean succeed = new UserCropService().fertilizerCr(userId, landNumber);
		renderJson(""+succeed);
	}
	
	//购买种子后添加到背包
	public void buyCrop() {
		int userId = getInt("userId");
		int cropId = getInt("cropId");
		int number = getInt("number");
		
		int userMoney = new UserService().getUpdateUserInfo(userId).getInt("money");
		int cropPrice = new CropService().getUpdateCropInfo(cropId).getInt("price");
		BagService service = new BagService();
		boolean succeed = false;
		if(userMoney >= cropPrice*number) { //有钱
			if(service.isExistCrop(userId, cropId)) { //增加作物数量
				succeed = service.addCropNumber(userId, cropId, number);
			}else { //新添加作物信息
				succeed = service.addToBag(userId, cropId, number);
			}
			renderJson(""+succeed);
		}else { //没钱
			renderJson("notEnoughMoney");
		}	
	}
	
	//种植作物
	public void raiseCrop() {
		int userId = getInt("userId");
		int cropId = getInt("cropId");
		String landNumber = get("landNumber");
		
		//返回为"true":种植成功；返回为"false":种植失败
		boolean succeed = new UserCropService().raiseNewCrop(userId, landNumber, cropId);
		renderJson(""+succeed);
	}
	
	//收获
	public void harvest() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");

		//返回为"up":收获成功且升级;返回为"true":收获成功;返回为"false":收获失败
		String result = new UserCropService().getCrop(userId, landNumber);
		renderJson(result);
	}
	
	//扩建土地
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
