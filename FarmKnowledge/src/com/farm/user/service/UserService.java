package  com.farm.user.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

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
	//后台管理系统添加新用户（没有绑定QQ）
	public boolean addUser(String accout, String nickName, String password, String photo, String photoName, String email, int grade){
		return new UserDao().addUser(accout, nickName, password, photo, photoName, email, grade);
	}
	//userauthority表添加QQ授权信息
	public boolean addUserAuthority(int userId, String openId, String type){
		return new UserDao().addUserAuthority(userId, openId, type);
	}
	
	
	
	
	/**
	 * 	删
	 * @throws
	 */
	//后台管理系统彻底删除单个用户信息
	public boolean deleteThoroughUser(int id) {
		return new UserDao().deleteThoroughUser(id);
	}
	//删除QQ授权信息
	public boolean deleteOpenIdByUserId(int userId) {
		return new UserDao().deleteOpenIdByUserId(userId);
	}

	
	
	
	/**
	 * 	改
	 * @throws
	 */
	//修改用户信息
	public boolean updateUser(String oldAccout, String newAccout, String nickName, String photo, String photoName, String email, int grade, int level, int experience, int money
			, int mathRewardCount, int englishRewardCount, int chineseRewardCount, int water, int fertilizer, int online) {
		return new UserDao().updateUser(oldAccout, newAccout, nickName, photo, photoName, email, grade, level, experience, money, mathRewardCount, englishRewardCount, chineseRewardCount, water, fertilizer, online);
	}
	//修改用户昵称
	public boolean updateUserNickName(String accout, String nickName) {
		return new UserDao().updateUserNickName(accout, nickName);
	}
	//修改用户年级
	public boolean updateUserGrade(String accout, int grade) {
		return new UserDao().updateUserGrade(accout, grade);
	}
	//修改用户密码
	public boolean updateUserPassword(String accout,String password) {
		return new UserDao().updateUserPassword(accout, password);
	}
	//修改用户头像
	public boolean updateUserPhoto(String accout, String photo, String photoName) {
		return new UserDao().updateUserPhoto(accout, photo, photoName);
	}
	//修改用户邮箱
	public boolean updateUserEmail(String accout, String email) {
		return new UserDao().updateUserEmail(accout, email);
	}
	//减少金币
	public boolean decreaseMoney(int id, int money) {
		return new UserDao().decreaseMoney(id, money);
	}
	//增加用户经验，金币
	public boolean addEandM(int id,int ex,int money) {
		return new UserDao().addExandMoney(id, ex, money);
	}
	//增加用户金币
	public boolean addMoney(int id, int money) {
		return new UserDao().addMoney(id, money);
	}
	//减少浇水次数
	public boolean lessW(int id) {
		return new UserDao().lessWater(id);
	}
	//减少施肥次数
	public boolean lessF(int id) {
		return new UserDao().lessFertilizer(id);
	}
	//设置用户指定土地种植何种作物
	public boolean updateLandCrop(int userId,String landNumber,int userCropId) {
		return new UserDao().updateLandCrop(userId, landNumber, userCropId);
	}
	//后台管理系统修改land1-18
	public boolean updateLand1_18(String accout, int land1, int land2, int land3, int land4, int land5, int land6, int land7, int land8, int land9, int land10
			, int land11, int land12, int land13, int land14, int land15, int land16, int land17, int land18){
		return new UserDao().updateLand1_18(accout, land1, land2, land3, land4, land5, land6, land7, land8, land9, land10, land11, land12, land13, land14, land15, land16, land17, land18);
	}
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//分页查询用户信息（指定accout、pageNumber、pageSize）
	public Page<User> findUserPageAll(int pageNumber,int everyCount,String accout) {
		return new UserDao().findUserPageAll(pageNumber, everyCount, accout);
	}
	//分页查询用户信息（指定accout、pageNumber、pageSize、exist）
	public Page<User> findUserPage(int pageNumber,int everyCount,String accout,int exist) {
		return new UserDao().findUserPage(pageNumber,everyCount,accout,exist);
	}
	//查询用户信息（指定openId）
	public User findUserByOpenId(String openId){
        return new UserDao().findUserByOpenId(openId);
    }
	//查询用户信息（指定账号）
	public User findUserByAccout(String accout){
		return new UserDao().findUserByAccout(accout);
	}
	//判断userauthority表内是否存在该用户（指定openId）
	public boolean isExistUserByOpenIdAll(String openId){
		return new UserDao().isExistUserByOpenIdAll(openId);
	}
	//判断UserAuthority表内是否存在该用户（指定openId、exist=1）
	public boolean isExistUserByOpenId(String openId){
        return new UserDao().isExistUserByOpenId(openId);
    }
	//判断是否存在该用户（指定账号）
	public boolean isExistUserByAccoutAll(String accout){
        return new UserDao().isExistUserByAccoutAll(accout);
    }
	//判断是否存在该用户（指定账号、exist=1）
	public boolean isExistUserByAccout(String accout){
        return new UserDao().isExistUserByAccout(accout);
    }
	//判断是否存在该用户，除指定账号外
	public boolean isExistUserByAccout(String accout1,String accout2){
        return new UserDao().isExistUserByAccout(accout1,accout2);
    }
	//查询用户信息（指定账号、密码）
	public User findUserByAccountPassword(String account,String pwd){
		return new UserDao().findUserByAccountPassword(account, pwd);
	}
	//后台管理系统 查询要修改的用户信息（指定id）
	public User getUpdateUserInfo0(int id) {
		return new UserDao().getUpdateUserInfo0(id);
	}
	//查询要修改的用户信息（指定id）
	public User getUpdateUserInfo(int id) {
		return new UserDao().getUpdateUserInfo(id);
	}
	//查询user表用户id（指定账号）
	public int getUserIdByAccout(String accout) {
		return new UserDao().getUserIdByAccout(accout);
	}
	//user表查询某块土地的userCropId（指定id，landNumber）
	public int findUcId(int userId,String landNumber) {
		return new UserDao().findUcIdByLand(userId, landNumber);
	}
	//user表查询用户所有土地的userCropId列表（指定id）
	public List<Integer> getUserCropIdById(int id) {
		return new UserDao().getUserCropIdById(id);
	}
	//判断账号是否已绑定QQ
	public boolean isBindingQQ(String accout) {
		return new UserDao().isBindingQQ(accout);
	}
	//判断邮箱是否已被其它账号绑定
	public boolean isBindingEmail(String email) {
		return new UserDao().isBindingEmail(email);
	}
	//查询是否已存在该photoName
	public boolean isExistPhotoName(String photoName) {
		return new UserDao().isExistPhotoName(photoName);
	}
	
	
	/**
	 * 	操作
	 * @throws
	 */
	//QQ第一次登录，添加用户信息
	public boolean addUser(String accout, String openId, String nickName, String password, String photo, String photoName, String email, int grade, String type){
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				UserDao userDao = new UserDao();
				
				//user表插入别名、头像
				boolean a1 = userDao.addUser(accout, nickName, password, photo, photoName, email, grade);
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
	//添加浇水，施肥次数，减少奖励次数
	public int lessRewardCount(int id,int water, int fertilizer, String subject) {
		UserDao dao = new UserDao();
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a1 = false;
				boolean a2 = false;
				int rewardCount = dao.getUpdateUserInfo(id).getInt(subject+"RewardCount");
				if(rewardCount >= 1) {
					a1 = dao.lessRewardCount(id,rewardCount-1,subject);
					a2 = dao.addWaterAndFertilizer(id, water, fertilizer);
				}else {
					a1 = true;
					a2 = true;
				}
				
				if(a1 && a2) {
					return true;
				}
				return false;
			}
		});
		if(succeed) {
			return dao.getUpdateUserInfo(id).getInt(subject+"RewardCount");
		}else {
			return -1;
		}
	}
	//扩建土地
	public boolean extensionLand(int userId, String landNumber, int money) {
		UserDao userDao = new UserDao();
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean a1 = userDao.decreaseMoney(userId, money);
				boolean a2 = userDao.extensionLand(userId, landNumber);
				if(a1 && a2) {
					return true;
				}
				return false;
			}
		});
		return succeed;
	}
	//后台管理系统删除单个用户信息
	public boolean deleteOneUser(int id) {
		UserDao userDao = new UserDao();
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean deleteUser = userDao.updateUserExist(id, 0);
				boolean deleteUserAuthority = false;
				if(userDao.isExistUserByUserId(id)) {
					deleteUserAuthority = userDao.updateUserAuthorityExist(id, 0);
				}else {
					deleteUserAuthority = true;
				}
				if(deleteUser && deleteUserAuthority) {
					return true;
				}
				return false;
			}
		});
		return succeed;
	}
	//后台管理系统恢复单个用户信息
	public boolean recoveryOneUser(int id) {
		UserDao userDao = new UserDao();
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean recoveryUser = userDao.updateUserExist(id, 1);
				boolean recoveryUserAuthority = false;
				if(userDao.isExistUserByUserId(id)) {
					recoveryUserAuthority = userDao.updateUserAuthorityExist(id, 1);
				}else {
					recoveryUserAuthority = true;
				}
				if(recoveryUser && recoveryUserAuthority) {
					return true;
				}
				return false;
			}
		});
		return succeed;
	}
	//生成八位数字随机数
	public String generateAccout() {
		String accout = "";
		do{
			accout = "";
			for(int n = 1;n < 9;n++) {
				accout += (int)(Math.random()*10);
			}
		}while(isExistUserByAccoutAll(accout) || accout.charAt(0) == '0' || 
				accout.charAt(accout.length()-1) == '0');
		return accout;
	}
	//MD5加密
	 public String stringMD5(String input) {
		  try {
		     // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
		     MessageDigest messageDigest =MessageDigest.getInstance("MD5");
		     // 输入的字符串转换成字节数组
		     byte[] inputByteArray = input.getBytes();
		     // inputByteArray是输入字符串转换得到的字节数组
		     messageDigest.update(inputByteArray);
		     // 转换并返回结果，也是字节数组，包含16个元素
		     byte[] resultByteArray = messageDigest.digest();
		     // 字符数组转换成字符串返回
		     return byteArrayToHex(resultByteArray);
		  } catch (NoSuchAlgorithmException e) {
		     return null;
		  }
	 }
	 //将字节数组换成成16进制的字符串
	 public String byteArrayToHex(byte[] byteArray) {
	   // 首先初始化一个字符数组，用来存放每个16进制字符
	   char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
	   // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
	   char[] resultCharArray =new char[byteArray.length * 2];
	   // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
	   int index = 0;
	   for (byte b : byteArray) {
	      resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
	      resultCharArray[index++] = hexDigits[b& 0xf];
	   }
	   // 字符数组组合成字符串返回
	   return new String(resultCharArray);
	 }

}
