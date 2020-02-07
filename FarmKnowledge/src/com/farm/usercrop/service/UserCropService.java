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
	
	
	
	/**
	 * 增
	 */
	
	
	
	/**
	 * 	删
	 * @throws
	 */
	//删除已收获的作物
	public boolean deleteCrop(int id) {
		return new UserCropDao().deleteCrop(id);
	}
	
	

	/**
	 * 	改
	 * @throws
	 */
	//浇水修改作物进程
	public boolean waterCrop(int id, int progress) {
		return new UserCropDao().updateProgress(id, progress);
	}
	//修改土地状态
	public boolean updateCropState(int id, int state) {
		return new UserCropDao().updateCropState(id, state);
	}
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//查询cropId(指定id)
	public int getCropIdByUserCropId(int id){
		return new UserCropDao().getCropIdByUserCropId(id);
	}
	//根据id查询cropId、progress（放到UserCropItem中cropId当成userCropId）
	public UserCropItem getCropIdProgressStateByUserCropId(int id){
		return new UserCropDao().getCropIdProgressStateByUserCropId(id);
	}
	//查询作物进度（指定id）
	public int getCropProgress(int id) {
		return new UserCropDao().getCropProgress(id);
	}
	//查询整条作物信息（指定id）
	public UserCrop findUserCropById(int id) {
		return new UserCropDao().findUserCropById(id);
	}
	
	
	
	/**
	 * 操作
	 */
	//浇水
	public boolean waterCr(int userId,String landNumber) {
		int id = new UserService().findUcId(userId, landNumber);
		UserCropDao dao = new UserCropDao();
		UserCrop userCrop = dao.findUserCropById(id);
		
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a1 = false;
				boolean a2 = new UserService().lessW(userId);
				boolean a3 = false;
				int cropId = userCrop.getInt("cropId");
				Crop crop = new CropDao().getUpdateCropInfo(cropId);
				int progress = dao.getCropProgress(id);
				int matureTime = crop.getInt("matureTime");
				
				if(progress+5 >= matureTime) {
					a1 = dao.updateProgress(id, matureTime);
				}else {
					a1 = dao.updateProgress(id, progress+5);
				}
				
				if(a1 == true && a2 == true) {
					if(userCrop.getInt("state") == 0) {
						a3 = dao.updateCropState(id, 1);
						new UserCropTimerManager(id); 
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
		int id = new UserService().findUcId(userId, landNumber);
		
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				UserCropDao dao = new UserCropDao();
				if(dao.findUserCropById(id).getInt("state") != 0) {
					boolean a1 = false;
					boolean a2 = new UserService().lessF(userId);
					Crop crop = new CropDao().getUpdateCropInfo(dao.getCropIdByUserCropId(id));
					int progress = dao.getCropProgress(id);
					int matureTime = crop.getInt("matureTime");
					
					if(progress+10 >= matureTime) {
						a1 = dao.updateProgress(id, matureTime);
					}else {
						a1 = dao.updateProgress(id, progress+10);
					}
					
					if(a1 == true && a2 == true) {
						return true;
					}
					return false;
				}else {
					return false;
				}
			}
		});
		
		if(succeed) {
			return true;
		}
		return false;	
	}
	//收获
	public String getCrop(int userId, String landNumber) {
		int id = new UserService().findUcId(userId, landNumber);
		Crop crop = new CropService().getUpdateCropInfo(getCropIdByUserCropId(id));
		int price = crop.getInt("value");
		int experience= crop.getInt("experience");
		result = "";
		isLevel = 0;
		
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				UserService service = new UserService();
				boolean a1 = new UserCropDao().deleteCrop(id);
				boolean a2 = false;
				boolean a3 = service.updateLandCrop(userId, landNumber, 0);
				boolean a4 = false;
				
				User user = service.getUpdateUserInfo(userId);
				int userLevel = user.getInt("level");
				int userExperience = user.getInt("experience");
				if(userLevel < Strings.userLevel.length-1) {
					if(userExperience + experience < Strings.userLevel[userLevel]) {
						a2 = service.addEandM(userId,experience,price);
						a4 = true;
					}else {
						isLevel = 1;
						a2 = service.addEandM(userId,experience,price);
						a4 = user.set("level", userLevel+1).update();
					}
				}else {
					if(userExperience + experience <= Strings.userLevel[userLevel]) {
						a2 = service.addEandM(userId,experience,price);
						a4 = true;
					}else {
						a2 = service.addMoney(userId, price);
						a4 = true;

					}
				}
				
				if(a1 && a2 && a3 && a4) {
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
	//种植新作物(当前土地是否已种植作物由前端判断）
	public boolean raiseNewCrop(int userId,String landNumber,int cropId) {
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				BagService bagService = new BagService();
				int id = 0;
				int number = bagService.findNumberByCropId(userId, cropId);
				boolean a2 = false;
				boolean a3 = false;
				if(number > 0) {
					id = new UserCropDao().addNewCrop(cropId);
					if(id != 0) {
						a2 = new UserService().updateLandCrop(userId, landNumber, id);
						a3 = bagService.decreaseOneCrop(userId, cropId);
					}
				}
				
				if(id != 0 && a2 && a3) {
					new UserCropTimerManager(id); 
					return true;
				}
				return false;
			}
		});
		return succeed;
	}
}
