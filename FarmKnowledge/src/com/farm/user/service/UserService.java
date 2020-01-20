package  com.farm.user.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
	//User表插入账号、别名、头像（accout、nickName、photo）
	public boolean addUser(String accout, String nickName, String password, String photo, String photoName, String email, int grade){
		return new UserDao().addUser(accout, nickName, password, photo, photoName, email, grade);
	}
	//UserAuthority表内插入userId、openId、type
	public boolean addUserAuthority(int userId, String openId, String type){
		return new UserDao().addUserAuthority(userId, openId, type);
	}
	
	
	
	/**
	 * 	删
	 * @throws
	 */
	//删除openId信息
	public boolean deleteOpenIdByUserId(int userId) {
		return new UserDao().deleteOpenIdByUserId(userId);
	}
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
	//修改用户信息，根据修改前账号索引到
	public boolean updateUser(String oldAccout, String newAccout, String nickName, String photo, String photoName, String email, int grade, int level, int experience, int money
			, int mathRewardCount, int englishRewardCount, int chineseRewardCount, int water, int fertilizer, int online) {
		return new UserDao().updateUser(oldAccout, newAccout, nickName, photo, photoName, email, grade, level, experience, money, mathRewardCount, englishRewardCount, chineseRewardCount, water, fertilizer, online);
	}
	//修改用户昵称，根据账号查询到
	public boolean updateUserNickName(String accout, String nickName) {
		return new UserDao().updateUserNickName(accout, nickName);
	}
	//修改用户年级，根据账号查询到
	public boolean updateUserGrade(String accout, int grade) {
		return new UserDao().updateUserGrade(accout, grade);
	}
	//设置密码，根据账号查询到
	public boolean updateUserPassword(String accout,String password) {
		return new UserDao().updateUserPassword(accout, password);
	}
	//修改用户的头像，根据账号查询到
	public boolean updateUserPhoto(String accout, String photo, String photoName) {
		return new UserDao().updateUserPhoto(accout, photo, photoName);
	}
	//设置email，根据账号查询到
	public boolean updateUserEmail(String accout, String email) {
		return new UserDao().updateUserEmail(accout, email);
	}
	//购买作物后，减少金币
	public boolean decreaseMoney(int id, int money) {
		return new UserDao().decreaseMoney(id, money);
	}
	//添加浇水，施肥（User表）
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
	//添加用户经验，金币(User表）
	public boolean addEandM(int id,int ex,int money) {
		return new UserDao().addExandMoney(id, ex, money);
	}
	//添加用户金币(User表）
	public boolean addMoney(int id, int money) {
		return new UserDao().addMoney(id, money);
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
	//修改land1-18
	public boolean updateLand1_18(String accout, int land1, int land2, int land3, int land4, int land5, int land6, int land7, int land8, int land9, int land10
			, int land11, int land12, int land13, int land14, int land15, int land16, int land17, int land18){
		return new UserDao().updateLand1_18(accout, land1, land2, land3, land4, land5, land6, land7, land8, land9, land10, land11, land12, land13, land14, land15, land16, land17, land18);
	}
	
	
	
	/**
	 * 	查
	 * @throws
	 */
	//查询User表内用户信息（User表）
	public Page<User> findUserPageAll(int pageNumber,int everyCount,String accout) {
		return new UserDao().findUserPageAll(pageNumber, everyCount, accout);
	}
	//查询User表内用户信息（User表）
	public Page<User> findUserPage(int pageNumber,int everyCount,String accout,int exist) {
		return new UserDao().findUserPage(pageNumber,everyCount,accout,exist);
	}
	//根据openId查询User表内用户信息（User表、UserAuthority表）
	public User findUserByOpenId(String openId){
        return new UserDao().findUserByOpenId(openId);
    }
	//根据账号查询User表内用户信息
	public User findUserByAccout(String accout){
		return new UserDao().findUserByAccout(accout);
	}
	//根据openId判断UserAuthority表内是否存在该用户exist=1（UserAuthority表）
	public boolean isExistUserByOpenId(String openId){
        return new UserDao().isExistUserByOpenId(openId);
    }
	//根据openId判断UserAuthority表内是否存在该用户（UserAuthority表）
	public boolean isExistUserByOpenIdAll(String openId){
		return new UserDao().isExistUserByOpenIdAll(openId);
	}
	//根据账号判断User表内是否存在该用户exist=1（User表）
	public boolean isExistUserByAccout(String accout){
        return new UserDao().isExistUserByAccout(accout);
    }
	//根据账号判断User表内是否存在该用户（User表）
	public boolean isExistUserByAccoutAll(String accout){
        return new UserDao().isExistUserByAccoutAll(accout);
    }
	//根据账号判断User表内是否存在该用户，除指定账号外（User表）
	public boolean isExistUserByAccout(String accout1,String accout2){
        return new UserDao().isExistUserByAccout(accout1,accout2);
    }
	//账号密码判断正误
	public User findUserByAccountPassword(String account,String pwd){
		return new UserDao().findUserByAccountPassword(account, pwd);
	}
	//后台 根据用户id获取到要修改的用户信息（账号、别名、头像）
	public User getUpdateUserInfo0(int id) {
		return new UserDao().getUpdateUserInfo0(id);
	}
	//根据用户id获取到要修改的用户信息（账号、别名、头像）
	public User getUpdateUserInfo(int id) {
		return new UserDao().getUpdateUserInfo(id);
	}
	//根据账号查询用户id
	public int getUserIdByAccout(String accout) {
		return new UserDao().getUserIdByAccout(accout);
	}
	//根据userId，landId查询userCropId
	public int findUcId(int userId,String landNumber) {
		return new UserDao().findUcIdByLand(userId, landNumber);
	}
	//根据userId查询userCropId列表
	public List<Integer> getUserCropIdById(int id) {
		return new UserDao().getUserCropIdById(id);
	}
	//判断账号是否绑定QQ
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
	
	//发送邮件
	public void sendEmail(String email, String text) {
		// Get system properties
		Properties props = System.getProperties();
		// Setup mail server
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", "smtp.qq.com");
		props.put("mail.smtp.auth", "true");
		
		//设置端口号
		props.put("mail.smtp.port","25");
		props.put("mail.smtp.starttls.enable","true");

		// Get Session
		Session session = Session.getInstance(props, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() { /* 若服务器需要身份认证，Session会自动调用这个方法 */
				return new PasswordAuthentication("1764056039@qq.com", "xdpafsunbhcbhbfa");
			}
		});

		// Define message
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress("1764056039@qq.com"));
			//设置收件人,to为收件人,cc为抄送,bcc为密送
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));//18395612156@163.com
			message.setSubject("【知识农场】");
			message.setText(text);
			// send message
			message.setHeader("X-Mailer", "smtpsend");
			message.setSentDate(new Date());
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
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
