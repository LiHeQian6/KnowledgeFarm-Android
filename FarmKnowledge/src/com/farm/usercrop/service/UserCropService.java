package com.farm.usercrop.service;

import java.sql.SQLException;

import com.farm.crop.dao.CropDao;
import com.farm.crop.service.CropService;
import com.farm.entity.Strings;
import com.farm.entity.UserCropItem;
import com.farm.entity.UserCropTimerManager;
import com.farm.model.Crop;
import com.farm.model.User;
import com.farm.model.UserCrop;
import com.farm.user.service.UserService;
import com.farm.userbag.service.BagService;
import com.farm.usercrop.dao.UserCropDao;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class UserCropService {
	private String result;
	private int isLevel;
	
	//浇水
	public boolean waterCr(int userId,String landNumber) {
		int ucId = new UserService().findUcId(userId, landNumber);
		UserCropDao dao = new UserCropDao();
		UserCrop userCrop = dao.findUserCropById(ucId);
		boolean succeed = Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				boolean a1 = false;
				boolean a2 = new UserService().lessW(userId);
				boolean a3 = false;
				int cropId = userCrop.getInt("cropId");
				Crop crop = new CropDao().getUpdateCropInfo(cropId);
				int progress = dao.getCropProgress(ucId);
				int matureTime = crop.getInt("matureTime");
				
				if(progress+5 >= matureTime) {
					a1 = dao.waterCrop(ucId, crop.getInt("matureTime"));
				}else {
					a1 = dao.waterCrop(ucId, progress+5);
				}
				
				if(a1 == true && a2 == true) {
					if(userCrop.getInt("state") == 0) {
						a3 = dao.updateCropState(ucId, 1);
						new UserCropTimerManager(ucId,cropId); 
					}else {
						a3 = true;
					}
					if(a3) {
						return true;
					}
					return false;
				}
				return false;
			}
		});
		if(succeed) {
			return true;
		}
		return false;				
	}
	
	//施肥
	public boolean fertilizerCr(int userId,String landNumber) {
		int ucId = new UserService().findUcId(userId, landNumber);
		boolean succeed = Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				UserCropDao dao = new UserCropDao();
				boolean a1 = false;
				boolean a2 = new UserService().lessF(userId);
				Crop crop = new CropDao().getUpdateCropInfo(dao.getCropIdByUserCropId(ucId));
				int progress = dao.getCropProgress(ucId);
				int matureTime = crop.getInt("matureTime");
				
				if(progress+10 >= matureTime) {
					a1 = dao.fertilizerCrop(ucId, crop.getInt("matureTime"));
				}else {
					a1 = dao.fertilizerCrop(ucId, progress+10);
				}
				
				if(a1 == true && a2 == true) {
					return true;
				}
				return false;
			}
		});
		if(succeed) {
			return true;
		}
		return false;	
	}

	//收获
	public String getCrop(int userId, String landNumber) {
		int ucId = new UserService().findUcId(userId, landNumber);
		Crop crop = new CropService().getUpdateCropInfo(getCropIdByUserCropId(ucId));
		int price = crop.getInt("value");
		int experience= crop.getInt("experience");
		result = "";
		isLevel = 0;
		boolean succeed = Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				UserService service = new UserService();
				boolean a1 = new UserCropDao().deleteCrop(ucId);
				boolean a2 = false;
				boolean a3 = service.updateLandCrop(userId, landNumber, 0);
				boolean a4 = false;
				
				User user = service.getUpdateUserInfo(userId);
				int userLevel = user.getInt("level");
				int userExperience = user.getInt("experience");
				if(userLevel < Strings.userLevel.length) {
					a2 = service.addEandM(userId,experience,price);
					if(userExperience >= Strings.userLevel[userLevel]) {
						isLevel = 1;
						a4 = user.set("level", userLevel+1).update();
					}else {
						a4 = true;
					}
				}else {
					a2 = service.addMoney(userId, price);
					a4 = true;
				}
					
				if(a1 == true && a2 == true && a3 == true && a4 == true) {
					if(isLevel == 1) {
						result = "up";
					}else {
						result = "true";
					}
					return true;
				}
				result = "false";
				return false;
			}
		});
		return result;		
	}
	
	//种植新作物,,,(没有考虑当前土地是否已有作物占用，由前端考虑QAQ）
	public boolean raiseNewCrop(int userId,String landNumber,int cropId) {
		boolean succeed = Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				BagService bagService = new BagService();
				int userCropId = 0;
				boolean a2 = false;
				boolean a3 = false;
				if(bagService.findNumberByCropId(userId, cropId) > 0) {
					userCropId = new UserCropDao().addNewCrop(cropId);
					if(userCropId != 0) {
						a2 = new UserService().updateLandCrop(userId, landNumber, userCropId);
						a3 = bagService.decreaseOneCrop(userId, cropId);
					}
				}
				
				if(userCropId != 0 && a2 && a3) {
					System.out.println("种植成功");
					System.out.println("userCropId:"+userCropId);
					System.out.println(findUserCropById(userCropId).toJson());
					new UserCropTimerManager(userCropId,cropId); 
					return true;
				}
				return false;
			}
		});
		return succeed;
	}
	
	//浇水
	public boolean waterCrop(int ucId, int progress) {
		return new UserCropDao().waterCrop(ucId, progress);
	}
	
	//根据userCropId查询cropId
	public int getCropIdByUserCropId(int id){
		return new UserCropDao().getCropIdByUserCropId(id);
	}
	
	//根据userCropId查询cropId、progress（放到UserCropItem中cropId当成userCropId）
	public UserCropItem getCropIdProgressStateByUserCropId(int id){
		return new UserCropDao().getCropIdProgressStateByUserCropId(id);
	}
	
	//根据userCropId查询作物进度
	public int getCropProgress(int ucId) {
		return new UserCropDao().getCropProgress(ucId);
	}
	
	//根据userCropId查询整条信息
	public UserCrop findUserCropById(int ucId) {
		return new UserCropDao().findUserCropById(ucId);
	}
	
	//修改土地状态
	public boolean updateCropState(int userCropId, int state) {
		return new UserCropDao().updateCropState(userCropId, state);
	}
	
}
