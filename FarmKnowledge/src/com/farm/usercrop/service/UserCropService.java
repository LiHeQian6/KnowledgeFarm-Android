package com.farm.usercrop.service;

import java.sql.SQLException;

import com.farm.crop.dao.CropDao;
import com.farm.crop.service.CropService;
import com.farm.entity.Strings;
import com.farm.entity.UserCropItem;
import com.farm.model.Crop;
import com.farm.model.User;
import com.farm.user.dao.UserDao;
import com.farm.user.service.UserService;
import com.farm.userbag.service.BagService;
import com.farm.usercrop.dao.UserCropDao;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class UserCropService {
	
	//浇水
	public int waterCr(int userId,String landNumber) {
		int ucId = new UserService().findUcId(userId, landNumber);
		UserCropDao dao = new UserCropDao();
		boolean succeed = Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				boolean a1 = false;
				boolean a2 = new UserService().lessW(userId);
				Crop crop = new CropDao().getUpdateCropInfo(dao.getCropIdByUserCropId(ucId));
				int progress = dao.getCropProgress(ucId);
				int matureTime = crop.getInt("matureTime");
				
				if(progress+5 >= matureTime) {
					a1 = dao.waterCrop(ucId, crop.getInt("matureTime"));
				}else {
					a1 = dao.waterCrop(ucId, progress+5);
				}
				
				if(a1 == true && a2 == true) {
					return true;
				}
				return false;
			}
		});
		if(succeed) {
			return dao.getCropProgress(ucId);
		}
		return -1;				
	}
	
	//施肥
	public int fertilizerCr(int userId,String landNumber) {
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
			return new UserCropDao().getCropProgress(ucId);
		}
		return -1;	
	}

	//收获
	public boolean getCrop(int userId, String landNumber) {
		int ucId = new UserService().findUcId(userId, landNumber);
		Crop crop = new CropService().getUpdateCropInfo(getCropIdByUserCropId(ucId));
		int price = crop.getInt("value");
		int experience= crop.getInt("experience");
		
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				UserService service = new UserService();
				boolean a1 = new UserCropDao().deleteCrop(ucId);
				boolean a2 = service.addEandM(userId, price, experience);
				boolean a3 = service.updateLandCrop(userId, landNumber, 0);
				
				boolean a4 = false;
				User user = service.getUpdateUserInfo(userId);
				int userLevel = user.getInt("level");
				int userExperience = user.getInt("experience");
				if(userExperience >= Strings.userLevel[userLevel]) {
					a4 = user.set("level", userLevel+1).update();
				}else {
					a4 = true;
				}
				if(a1 == true && a2 == true && a3 == true && a4 == true) {
					return true;
				}
				return false;
			}
		});
		return succeed;
				
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
					return true;
				}
				return false;
			}
		});
		return succeed;
	}
	
	//根据userCropId查询cropId
	public int getCropIdByUserCropId(int id){
		return new UserCropDao().getCropIdByUserCropId(id);
	}
	
	//根据userCropId查询cropId、progress（放到UserCropItem中cropId当成userCropId）
	public UserCropItem getCropIdProgressByUserCropId(int id){
		return new UserCropDao().getCropIdProgressByUserCropId(id);
	}
	
	//查看作物进度
	public int getCropProgress(int userId,String landNumber) {
		int ucId = new UserDao().findUcIdByLand(userId, landNumber);
		return new UserCropDao().getCropProgress(ucId);
	}
	
}
