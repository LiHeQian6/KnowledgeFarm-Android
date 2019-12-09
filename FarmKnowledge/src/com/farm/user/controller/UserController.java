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
	
	//用户登录
	public void loginByOpenId() {
		String jsonStr =  HttpKit.readData(getRequest());
		JSONObject jsonObject = new JSONObject(jsonStr);
		
		String openId = jsonObject.getString("openId");
		String nickName = jsonObject.getString("nickName");
		String photo = URLDecoder.decode(jsonObject.getString("photo"));
		
		UserService service = new UserService();
		if(service.isExistUserByOpenId(openId)) { //已存在该用户，查询用户信息
			User user = service.findUserByOpenId(openId);
			renderJson(user);
		}else { 						  
			if(!service.isExistUserByOpenIdAll(openId)) { //不存在该用户，添加用户
				boolean addUser = service.addUser(openId, nickName, photo, "QQ", "");
				if(addUser) { //添加新用户成功
					User user = service.findUserByOpenId(openId);
					renderJson(user);
				}else { 	  //添加新用户失败
					renderJson("error");
				}
			}else { //exist=0;
				renderJson("fail");
			}
		}
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
		String oldPassword = get("oldPassword");
		String newPassword = get("newPassword");
		String accout = get("accout");
		
		//返回0说明旧密码错误，返回1说明修改成功，返回2说明修改失败
		int flag = new UserService().updateUserPassword(oldPassword, newPassword, accout);
		renderJson(flag);
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
		String photo = URLDecoder.decode(get("photo"));
		
		UserService service = new UserService();
		if(!service.isExistUserByOpenIdAll(openId)) {
			
			boolean succeed = Db.tx(new IAtom() {
				@Override
				public boolean run() throws SQLException {
					boolean a1 = service.addUserAuthority(service.getUserIdByAccout(accout), openId, "QQ"); //添加到授权表
					boolean a2 = service.updateUserPhoto(accout, photo); //修改成QQ头像
					if(a1 && a2) {
						return true;
					}
					return false;
				}
			});
			
			renderJson(""+succeed);
			
		}else { //该QQ号已被绑定
			renderJson("already");
		}
	}
	
	//解绑QQ
	public void unBindingQQ() {
		String accout = get("accout");
		
		UserService service = new UserService();
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a1 = service.deleteOpenIdByUserId(service.getUserIdByAccout(accout)); //从授权表删除
				boolean a2 = new UserService().updateUserPhoto(accout, Strings.userPhotoUrl+"0.png"); //修改成默认头像
				if(a1 && a2) {
					return true;
				}
				return false;
			}
		});
		
		renderJson(""+succeed);
	}
	
	//增加浇水、施肥次数（userId，数量）
	public void addUserWater() {
		int userId = getInt("userId");
		int water = getInt("water");
		int fertilizer = getInt("fertilizer");
		
		//返回为"-1":操作失败；返回为"非-1":剩余奖励次数
		int rewardCount = new UserService().addWaterAndFer(userId, water, fertilizer);
		renderJson(rewardCount);
	}
	
	//查询剩余奖励次数
	public void getRewardCount() {
		int userId = getInt("userId");
		renderJson(""+new UserService().getRewardCount(userId));
	}

	//浇水（userId）
	public void waterCrop() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		
		//progress为作物当前进度；若为-1，则浇水失败
		int progress = new UserCropService().waterCr(userId, landNumber);
		renderJson(progress);
	}
	
	//施肥（userId）
	public void fertilizerCrop() {
		int userId = getInt("userId");
		String landNumber = get("landNumber");
		
		//progress为作物当前进度；若为-1，则施肥失败
		int progress = new UserCropService().fertilizerCr(userId, landNumber);
		renderJson(progress);
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

		//返回为"true":收获成功；返回为"false":收获失败
		boolean succeed = new UserCropService().getCrop(userId, landNumber);
		renderJson(""+succeed);
	}
	
	
}
