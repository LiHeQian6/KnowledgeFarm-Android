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
	 * 	��
	 * @throws
	 */
	//��̨����ϵͳ������û���û�а�QQ��
	public boolean addUser(String accout, String nickName, String password, String photo, String photoName, String email, int grade){
		return new UserDao().addUser(accout, nickName, password, photo, photoName, email, grade);
	}
	//userauthority�����QQ��Ȩ��Ϣ
	public boolean addUserAuthority(int userId, String openId, String type){
		return new UserDao().addUserAuthority(userId, openId, type);
	}
	
	
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	//��̨����ϵͳ����ɾ�������û���Ϣ
	public boolean deleteThoroughUser(int id) {
		return new UserDao().deleteThoroughUser(id);
	}
	//ɾ��QQ��Ȩ��Ϣ
	public boolean deleteOpenIdByUserId(int userId) {
		return new UserDao().deleteOpenIdByUserId(userId);
	}

	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�޸��û���Ϣ
	public boolean updateUser(String oldAccout, String newAccout, String nickName, String photo, String photoName, String email, int grade, int level, int experience, int money
			, int mathRewardCount, int englishRewardCount, int chineseRewardCount, int water, int fertilizer, int online) {
		return new UserDao().updateUser(oldAccout, newAccout, nickName, photo, photoName, email, grade, level, experience, money, mathRewardCount, englishRewardCount, chineseRewardCount, water, fertilizer, online);
	}
	//�޸��û��ǳ�
	public boolean updateUserNickName(String accout, String nickName) {
		return new UserDao().updateUserNickName(accout, nickName);
	}
	//�޸��û��꼶
	public boolean updateUserGrade(String accout, int grade) {
		return new UserDao().updateUserGrade(accout, grade);
	}
	//�޸��û�����
	public boolean updateUserPassword(String accout,String password) {
		return new UserDao().updateUserPassword(accout, password);
	}
	//�޸��û�ͷ��
	public boolean updateUserPhoto(String accout, String photo, String photoName) {
		return new UserDao().updateUserPhoto(accout, photo, photoName);
	}
	//�޸��û�����
	public boolean updateUserEmail(String accout, String email) {
		return new UserDao().updateUserEmail(accout, email);
	}
	//���ٽ��
	public boolean decreaseMoney(int id, int money) {
		return new UserDao().decreaseMoney(id, money);
	}
	//�����û����飬���
	public boolean addEandM(int id,int ex,int money) {
		return new UserDao().addExandMoney(id, ex, money);
	}
	//�����û����
	public boolean addMoney(int id, int money) {
		return new UserDao().addMoney(id, money);
	}
	//���ٽ�ˮ����
	public boolean lessW(int id) {
		return new UserDao().lessWater(id);
	}
	//����ʩ�ʴ���
	public boolean lessF(int id) {
		return new UserDao().lessFertilizer(id);
	}
	//�����û�ָ��������ֲ��������
	public boolean updateLandCrop(int userId,String landNumber,int userCropId) {
		return new UserDao().updateLandCrop(userId, landNumber, userCropId);
	}
	//��̨����ϵͳ�޸�land1-18
	public boolean updateLand1_18(String accout, int land1, int land2, int land3, int land4, int land5, int land6, int land7, int land8, int land9, int land10
			, int land11, int land12, int land13, int land14, int land15, int land16, int land17, int land18){
		return new UserDao().updateLand1_18(accout, land1, land2, land3, land4, land5, land6, land7, land8, land9, land10, land11, land12, land13, land14, land15, land16, land17, land18);
	}
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//��ҳ��ѯ�û���Ϣ��ָ��accout��pageNumber��pageSize��
	public Page<User> findUserPageAll(int pageNumber,int everyCount,String accout) {
		return new UserDao().findUserPageAll(pageNumber, everyCount, accout);
	}
	//��ҳ��ѯ�û���Ϣ��ָ��accout��pageNumber��pageSize��exist��
	public Page<User> findUserPage(int pageNumber,int everyCount,String accout,int exist) {
		return new UserDao().findUserPage(pageNumber,everyCount,accout,exist);
	}
	//��ѯ�û���Ϣ��ָ��openId��
	public User findUserByOpenId(String openId){
        return new UserDao().findUserByOpenId(openId);
    }
	//��ѯ�û���Ϣ��ָ���˺ţ�
	public User findUserByAccout(String accout){
		return new UserDao().findUserByAccout(accout);
	}
	//�ж�userauthority�����Ƿ���ڸ��û���ָ��openId��
	public boolean isExistUserByOpenIdAll(String openId){
		return new UserDao().isExistUserByOpenIdAll(openId);
	}
	//�ж�UserAuthority�����Ƿ���ڸ��û���ָ��openId��exist=1��
	public boolean isExistUserByOpenId(String openId){
        return new UserDao().isExistUserByOpenId(openId);
    }
	//�ж��Ƿ���ڸ��û���ָ���˺ţ�
	public boolean isExistUserByAccoutAll(String accout){
        return new UserDao().isExistUserByAccoutAll(accout);
    }
	//�ж��Ƿ���ڸ��û���ָ���˺š�exist=1��
	public boolean isExistUserByAccout(String accout){
        return new UserDao().isExistUserByAccout(accout);
    }
	//�ж��Ƿ���ڸ��û�����ָ���˺���
	public boolean isExistUserByAccout(String accout1,String accout2){
        return new UserDao().isExistUserByAccout(accout1,accout2);
    }
	//��ѯ�û���Ϣ��ָ���˺š����룩
	public User findUserByAccountPassword(String account,String pwd){
		return new UserDao().findUserByAccountPassword(account, pwd);
	}
	//��̨����ϵͳ ��ѯҪ�޸ĵ��û���Ϣ��ָ��id��
	public User getUpdateUserInfo0(int id) {
		return new UserDao().getUpdateUserInfo0(id);
	}
	//��ѯҪ�޸ĵ��û���Ϣ��ָ��id��
	public User getUpdateUserInfo(int id) {
		return new UserDao().getUpdateUserInfo(id);
	}
	//��ѯuser���û�id��ָ���˺ţ�
	public int getUserIdByAccout(String accout) {
		return new UserDao().getUserIdByAccout(accout);
	}
	//user���ѯĳ�����ص�userCropId��ָ��id��landNumber��
	public int findUcId(int userId,String landNumber) {
		return new UserDao().findUcIdByLand(userId, landNumber);
	}
	//user���ѯ�û��������ص�userCropId�б�ָ��id��
	public List<Integer> getUserCropIdById(int id) {
		return new UserDao().getUserCropIdById(id);
	}
	//�ж��˺��Ƿ��Ѱ�QQ
	public boolean isBindingQQ(String accout) {
		return new UserDao().isBindingQQ(accout);
	}
	//�ж������Ƿ��ѱ������˺Ű�
	public boolean isBindingEmail(String email) {
		return new UserDao().isBindingEmail(email);
	}
	//��ѯ�Ƿ��Ѵ��ڸ�photoName
	public boolean isExistPhotoName(String photoName) {
		return new UserDao().isExistPhotoName(photoName);
	}
	
	
	/**
	 * 	����
	 * @throws
	 */
	//QQ��һ�ε�¼������û���Ϣ
	public boolean addUser(String accout, String openId, String nickName, String password, String photo, String photoName, String email, int grade, String type){
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				UserDao userDao = new UserDao();
				
				//user����������ͷ��
				boolean a1 = userDao.addUser(accout, nickName, password, photo, photoName, email, grade);
				//���user��ղ������ݵ�userId
				int userId = userDao.getLastUserId();
				//UserAuthority���ڲ���userId��openId��token
				boolean a2 = userDao.addUserAuthority(userId, openId, type);
				
				if(a1 == true && a2 == true) { //��ӳɹ�
					return true;
				}else { //���ʧ�ܣ�����ص�
					return false;
				}
			}
		});
		return succeed;
    }
	//��ӽ�ˮ��ʩ�ʴ��������ٽ�������
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
	//��������
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
	//��̨����ϵͳɾ�������û���Ϣ
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
	//��̨����ϵͳ�ָ������û���Ϣ
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
	//���ɰ�λ���������
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
	//MD5����
	 public String stringMD5(String input) {
		  try {
		     // �õ�һ��MD5ת�����������ҪSHA1�������ɡ�SHA1����
		     MessageDigest messageDigest =MessageDigest.getInstance("MD5");
		     // ������ַ���ת�����ֽ�����
		     byte[] inputByteArray = input.getBytes();
		     // inputByteArray�������ַ���ת���õ����ֽ�����
		     messageDigest.update(inputByteArray);
		     // ת�������ؽ����Ҳ���ֽ����飬����16��Ԫ��
		     byte[] resultByteArray = messageDigest.digest();
		     // �ַ�����ת�����ַ�������
		     return byteArrayToHex(resultByteArray);
		  } catch (NoSuchAlgorithmException e) {
		     return null;
		  }
	 }
	 //���ֽ����黻�ɳ�16���Ƶ��ַ���
	 public String byteArrayToHex(byte[] byteArray) {
	   // ���ȳ�ʼ��һ���ַ����飬�������ÿ��16�����ַ�
	   char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
	   // newһ���ַ����飬�������������ɽ���ַ����ģ�����һ�£�һ��byte�ǰ�λ�����ƣ�Ҳ����2λʮ�������ַ���2��8�η�����16��2�η�����
	   char[] resultCharArray =new char[byteArray.length * 2];
	   // �����ֽ����飬ͨ��λ���㣨λ����Ч�ʸߣ���ת�����ַ��ŵ��ַ�������ȥ
	   int index = 0;
	   for (byte b : byteArray) {
	      resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
	      resultCharArray[index++] = hexDigits[b& 0xf];
	   }
	   // �ַ�������ϳ��ַ�������
	   return new String(resultCharArray);
	 }

}
