package  com.farm.user.service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.farm.model.User;
import com.farm.user.dao.UserDao;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

public class UserService {
	
	
	/**
	 * 	增
	 * @throws
	 */
	//添加用户信息（授权id、账号、别名、头像、登陆类型）（User表、UserAuthority表）
	public boolean addUser(String openId, String nickName, String photo,String type, String photoName){
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				UserDao userDao = new UserDao();
				
				//user表插入别名、头像
				boolean a1 = userDao.addUser(generateAccout(), nickName, photo, photoName);
				//获得user表刚插入数据的userId
				int userId = userDao.getLastUserId();
				//UserAuthority表内插入userId、openId、token
				boolean a2 = userDao.addUserAuthority(userId, openId, type);
				
				if(a1 == true && a2 == true) { //添加成功
					return true;
				}else { //添加失败，事务回调
					return false;
				}
			}
		});
		return succeed;
    }
	
	
	
	/**
	 * 	删
	 * @throws
	 */
	//彻底删除User表内用户信息（User表delete）
	public boolean deleteThoroughUser(int userId) {
		return new UserDao().deleteThoroughUser(userId);
	}
	//彻底删除UserAuthority表内授权信息（User表delete）
	public boolean deleteThoroughUserAuthority(int userId) {
		return new UserDao().deleteThoroughUserAuthority(userId);
	}
	
	
	
	/**
	 * 	改
	 * @throws
	 */
	//修改用户信息（账号、别名、头像）根据修改前账号索引到
	public boolean updateUser(String oldAccout, String newAccout, String nickName, String photo, String photoName) {
		return new UserDao().updateUser(oldAccout, newAccout, nickName, photo, photoName);
	}
	//购买作物后，减少金币
	public boolean decreaseMoney(int id, int money) {
		return new UserDao().decreaseMoney(id, money);
	}
	//添加浇水，施肥（User表）
	public int addWaterAndFer(int id,int water, int fertilizer) {
		UserDao dao = new UserDao();
		if(dao.getRewardCount(id) > 0) {
			boolean succeed = Db.tx(new IAtom() {
				@Override
				public boolean run() throws SQLException {
					boolean a1 = dao.addWaterAndFertilizer(id, water, fertilizer);
					boolean a2 = dao.lessRewardCount(id);
					if(a1 && a2) {
						return true;
					}
					return false;
				}
			});
			if(succeed) {
				return dao.getRewardCount(id);
			}else {
				return -1;
			}
		}
		return -1;
	}
	//添加用户经验，金币(User表）
	public boolean addEandM(int id,int ex,int money) {
		return new UserDao().addExandMoney(id, ex, money);
	}
	//减少浇水（User表）
	public boolean lessW(int id) {
		return new UserDao().lessWater(id);
	}
	//减少施肥（User表）
	public boolean lessF(int id) {
		return new UserDao().lessFertilizer(id);
	}
	//设置用户指定土地种植何种作物
	public boolean updateLandCrop(int userId,String landNumber,int userCropId) {
		return new UserDao().updateLandCrop(userId, landNumber, userCropId);
	}
	//删除User表内单个用户信息（User表修改exist字段为0）
	public boolean deleteOneUser(int userId) {
		return new UserDao().deleteOneUser(userId);
	}
	//删除UserAuthority表内单个授权信息（UserAuthority表修改exist字段为0）
	public boolean deleteOneUserAuthority(int userId) {
		return new UserDao().deleteOneUserAuthority(userId);
	}
	//恢复User表内单个用户信息（User表修改exist字段为1）
	public boolean recoveryOneUser(int userId) {
		return new UserDao().recoveryOneUser(userId);
	}
	//恢复UserAuthority表内单个授权信息（UserAuthority表修改exist字段为1）
	public boolean recoveryOneUserAuthority(int userId) {
		return new UserDao().recoveryOneUserAuthority(userId);
	}
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//查询User表内用户信息（User表）
	public Page<User> findUserPage(int pageNumber,int everyCount,String accout,int exist) {
		return new UserDao().findUserPage(pageNumber,everyCount,accout,exist);
	}
	//根据openId查询User表内用户信息（User表、UserAuthority表）
	public User findUserByOpenId(String openId){
        return new UserDao().findUserByOpenId(openId);
    }
	//根据openId判断UserAuthority表内是否存在该用户exist=1（UserAuthority表）
	public boolean isExistUserByOpenId(String openId){
        return new UserDao().isExistUserByOpenId(openId);
    }
	//根据openId判断UserAuthority表内是否存在该用户（UserAuthority表）
	public boolean isExistUserByOpenIdAll(String openId){
		return new UserDao().isExistUserByOpenIdAll(openId);
	}
	//根据账号判断User表内是否存在该用户（User表）
	public boolean isExistUserByAccout(String accout){
        return new UserDao().isExistUserByAccout(accout);
    }
	//根据账号判断User表内是否存在该用户，除指定账号外（User表）
	public boolean isExistUserByAccout(String accout1,String accout2){
        return new UserDao().isExistUserByAccout(accout1,accout2);
    }
	//根据用户id获取到要修改的用户信息（账号、别名、头像）
	public User getUpdateUserInfo(int id) {
		return new UserDao().getUpdateUserInfo(id);
	}
	//根据userId，landId查询userCropId
	public int findUcId(int userId,String landNumber) {
		return new UserDao().findUcIdByLand(userId, landNumber);
	}
	//根据userId查询userCropId列表
	public List<Integer> getUserCropIdById(int id) {
		return new UserDao().getUserCropIdById(id);
	}
	//User表查询剩余奖励次数
	public int getRewardCount(int id) {
		return new UserDao().getRewardCount(id);
	}
	
	//生成账号
	public String generateAccout() {
		String accout = "";
		do{
			for(int n = 1;n < 9;n++) {
				accout += (int)(Math.random()*10);
			}
		}while(isExistUserByAccout(accout) || accout.charAt(0) == '0' || 
				accout.charAt(accout.length()-1) == '0');
		return accout;
	}

}
