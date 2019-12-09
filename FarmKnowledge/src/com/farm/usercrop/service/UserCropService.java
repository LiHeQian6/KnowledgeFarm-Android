package com.farm.usercrop.service;

import java.sql.SQLException;

import com.farm.crop.service.CropService;
import com.farm.model.Crop;
import com.farm.user.dao.UserDao;
import com.farm.user.service.UserService;
import com.farm.userbag.service.BagService;
import com.farm.usercrop.dao.UserCropDao;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class UserCropService {
	
	//浇水(参数:usercropId，userId)
	public int waterCr(int userId,String landNumber) {
		int ucId = new UserService().findUcId(userId, landNumber);
		boolean succeed = Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				boolean a1 = new UserCropDao().waterCrop(ucId);
				boolean a2 = new UserService().lessW(userId);
				
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
	
	//施肥(参数:usercropId,userId)
	public int fertilizerCr(int userId,String landNumber) {
		int ucId = new UserService().findUcId(userId, landNumber);
		boolean succeed = Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				boolean a1 = new UserCropDao().fertilizerCrop(ucId);
				boolean a2 = new UserService().lessF(userId);
				
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
				if(a1 == true && a2 == true && a3 == true) {
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
				int userCropId = new UserCropDao().addNewCrop(cropId);
				boolean a2 = false;
				boolean a3 = false;
				if(userCropId != 0) {
					a2 = new UserService().updateLandCrop(userId, landNumber, userCropId);
					a3 = new BagService().decreaseOneCrop(userId, cropId);
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
	
	//查看作物进度
	public int getCropProgress(int userId,String landNumber) {
		int ucId = new UserDao().findUcIdByLand(userId, landNumber);
		return new UserCropDao().getCropProgress(ucId);
	}
	
}
