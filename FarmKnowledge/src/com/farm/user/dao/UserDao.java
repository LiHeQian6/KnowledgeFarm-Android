package  com.farm.user.dao;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.farm.entity.Strings;
import com.farm.model.User;
import com.farm.model.UserAuthority;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class UserDao {
	
	/**
	 * 	��
	 * @throws
	 */
	//user�����accout��nickName��password��photo��photoName��email��grade
	public boolean addUser(String accout, String nickName, String password, String photo, String photoName, String email, int grade){
		boolean succeed =  new User().set("accout", accout).set("nickName", nickName).set("password", password).set("photo", photo).set("photoName", photoName).set("email", email).set("grade", grade).save();
		return succeed;
	}
	//userauthority���ڲ���userId��openId��type
	public boolean addUserAuthority(int userId, String openId, String type){
		boolean succeed =  new UserAuthority().set("userId", userId).set("openId", openId).set("type", type).save();
		return succeed;
	}
	
	
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	//user��ɾ����������
	public boolean deleteThoroughUser(int id) {
		boolean succeed = User.dao.deleteById(id);
		return succeed;
	}
	//userauthority��ɾ����������
	public boolean deleteOpenIdByUserId(int userId) {
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where userId=?",userId);
		if(list.size() != 0) {
			boolean succeed = UserAuthority.dao.deleteById(list.get(0).getInt("id"));
			return succeed;
		}
		return false;
	}
	
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */                                                                                                                   
	//user���޸�����
	public boolean updateUser(String oldAccout, String newAccout, String nickName, String photo, String photoName, String email, int grade, int level, int experience, int money
			, int mathRewardCount, int englishRewardCount, int chineseRewardCount, int water, int fertilizer, int online) {
		List<User> list = User.dao.find("select * from user where accout=?",oldAccout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0)
					.set("accout", newAccout)
					.set("nickName", nickName)
					.set("photo", photo)
					.set("photoName", photoName)
					.set("email", email)
					.set("grade", grade)
					.set("level", level)
					.set("experience", experience)
					.set("money", money)
					.set("mathRewardCount", mathRewardCount)
					.set("englishRewardCount", englishRewardCount)
					.set("chineseRewardCount", chineseRewardCount)
					.set("water", water)
					.set("fertilizer", fertilizer)
					.set("online", online)
					.update();
		}
		return succeed;
	}
	//user���޸�nickName
	public boolean updateUserNickName(String accout, String nickName) {
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("nickName", nickName).update();
		}
		return succeed;
	}
	//user���޸�grade
	public boolean updateUserGrade(String accout, int grade) {
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("grade", grade).update();
		}
		return succeed;
	}	
	//user���޸�password
	public boolean updateUserPassword(String accout,String password) {
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			User user = list.get(0);
			return user.set("password", password).update();
		}
		return false;
	}
	//user���޸�photo��photoName
	public boolean updateUserPhoto(String accout, String photo, String photoName) {
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			boolean succeed = list.get(0).set("photo", photo).set("photoName", photoName).update();
			return succeed;
		}
		return false;
	}
	//user���޸�email
	public boolean updateUserEmail(String accout, String email) {
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			boolean succeed = list.get(0).set("email", email).update();
			return succeed;
		}
		return false;
	}
	//user������money
	public boolean addMoney(int id, int money) {
		User user = User.dao.findById(id);
		if(user != null) {
			return user.set("money", user.getInt("money")+money).update();
		}
		return false;
	}
	//user�����money
	public boolean decreaseMoney(int id, int money) {
		User user = User.dao.findById(id);
		if(user != null) {
			return user.set("money", user.getInt("money")-money).update();
		}
		return false;
	}
	//user������experience��money
	public boolean addExandMoney(int id,int ex,int money) {
		User user = User.dao.findById(id);
		if(user != null) {
			return user.set("experience", user.getInt("experience")+ex).set("money", user.getInt("money")+money).update();
		}
		return false;
	}
	//user������water��fertilizer
	public boolean addWaterAndFertilizer(int id, int water, int fertilizer) {
		User user = User.dao.findById(id);
		boolean succeed = false;
		if(user != null) {
			succeed = user.set("water", user.getInt("water")+water).set("fertilizer", user.getInt("fertilizer")+fertilizer).update();
		}
		return succeed;
	}
	//user�����water
	public boolean lessWater(int id) {
		User user = User.dao.findById(id);
		if(user != null) {
			if(user.getInt("water") == 0) {
				return false;
			}
			return user.set("water", user.getInt("water")-1).update();
		}
		return false;
	}
	//user�����fertilizer
	public boolean lessFertilizer(int id) {
		User user = User.dao.findById(id);
		if(user != null) {
			if(user.getInt("fertilizer") == 0) {
				return false;
			}
			return user.set("fertilizer", user.getInt("fertilizer")-1).update();
		}
		return false;
	}
	//user���޸�landNumber
	public boolean updateLandCrop(int id,String landNumber,int userCropId) {
		return User.dao.findById(id).set(landNumber, userCropId).update();
	}
	//user���޸�rewardCount
	public boolean lessRewardCount(int id, int rewardCount, String subject) {
		User user = User.dao.findById(id);
		if(user != null) {
			return User.dao.findById(id).set(subject+"RewardCount", rewardCount).update();
		}
		return false;
	}
	//user���޸�landNumberΪ0
	public boolean extensionLand(int id, String landNumber) {
		boolean succeed = User.dao.findById(id).set(landNumber, 0).update();
		return succeed;
	}
	//user���޸�exist
	public boolean updateUserExist(int id, int exist) {
		boolean succeed = User.dao.findById(id).set("exist", exist).update();
		return succeed;
	}
	//userauthority���޸�exist
	public boolean updateUserAuthorityExist(int userId, int exist) {
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where userId=?",userId);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("exist", 0).update();
		}
		return succeed;
	}
	//user���޸�land1-18
	public boolean updateLand1_18(String accout, int land1, int land2, int land3, int land4, int land5, int land6, int land7, int land8, int land9, int land10
			, int land11, int land12, int land13, int land14, int land15, int land16, int land17, int land18){
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			return list.get(0).set("land1", land1).set("land2", land2).set("land3", land3).set("land4", land4).set("land5", land5).set("land6", land6)
				.set("land7", land7).set("land8", land8).set("land9", land9).set("land10", land10).set("land11", land11).set("land12", land12)
				.set("land13", land13).set("land14", land14).set("land15", land15).set("land16", land16).set("land17", land17).set("land18", land18).update();
			
		}
		return false;
	}
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//user���ҳ��ѯ���ݣ�ָ��accout��pageNumber��pageSize��
	public Page<User> findUserPageAll(int pageNumber,int everyCount,String accout) {
		Page<User> userPage;
		if(accout == null || accout.equals("")) {
			userPage = User.dao.paginate(pageNumber, everyCount, "select *","from user");	
		}else {
			userPage = User.dao.paginate(pageNumber, everyCount, "select *","from user where accout like?","%"+accout+"%");
		}
		if(userPage != null) {
			List<User> list = userPage.getList();
			if(list.size() != 0) {
				int i = 0;
				for(User user : list) {
					list.set(i, user.set("photo", Strings.userPhotoUrl + user.get("photo")));
					i++;
				}
				userPage.setList(list);
			}
		}
		return userPage;
	}
	//user���ҳ��ѯ���ݣ�ָ��accout��pageNumber��pageSize��exist��
	public Page<User> findUserPage(int pageNumber,int everyCount,String accout,int exist) {
		Page<User> userPage;
		if(accout == null || accout.equals("")) {
			userPage = User.dao.paginate(pageNumber, everyCount, "select *","from user where exist=?",exist);	
		}else {
			userPage = User.dao.paginate(pageNumber, everyCount, "select *","from user where exist=? and accout like?",exist,"%"+accout+"%");
		}
		if(userPage != null) {
			List<User> list = userPage.getList();
			if(list.size() != 0) {
				int i = 0;
				for(User user : list) {
					list.set(i, user.set("photo", Strings.userPhotoUrl + user.get("photo")));
					i++;
				}
				userPage.setList(list);
			}
		}
		return userPage;
	}
	//user���ѯ�������ݣ�ָ��openId��
	public User findUserByOpenId(String openId){
		List<User> list = User.dao.find("select user.* from user,userauthority where user.id=userauthority.userId and userauthority.openId=?",openId);
		if(list.size() != 0) {
			User user = list.get(0);
			user.set("photo", URLEncoder.encode(Strings.userPhotoUrl + user.get("photo")));
			return user;
		}
		return null;
	}
	//user���ѯ�������ݣ�ָ��accout��
	public User findUserByAccout(String accout){
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			User user = list.get(0);
			user.set("photo", URLEncoder.encode(Strings.userPhotoUrl + user.get("photo")));
			return user;
		}
		return null;
	}
	//user���ѯ�������ݣ�ָ��accout��password��
	public User findUserByAccountPassword(String account,String pwd){
		List<User> list = User.dao.find("select * from user where accout=? and password=?",account,pwd);
		if(list.size() != 0) {
			User user = list.get(0);
			user.set("photo", URLEncoder.encode(Strings.userPhotoUrl + user.getStr("photo")));
			return user;
		}
		return null;
	}
	//user���ѯ�������ݣ�ָ��id��
	public User getUpdateUserInfo(int id) {
		User user = User.dao.findById(id);
		user.set("photo", URLEncoder.encode(Strings.userPhotoUrl + user.getStr("photo")));
		return user;
	}
	//��̨ user���ѯ�������ݣ�ָ��id��
	public User getUpdateUserInfo0(int id) {
		User user = User.dao.findById(id);
		return user;
	}
	//user���ѯ���һ�����ݵ�id
	public int getLastUserId(){
		int id =  Db.queryInt("select id from user order by id desc limit 1");
		return id;
	}
	//user���ѯuserCropId��ָ��id��landNumber��
	public int findUcIdByLand(int userId,String landNumber) {
		User user = User.dao.findById(userId);
		if(user != null) {
			int ucId = user.getInt(landNumber);
			return ucId;
		}
		return 0;
	}
	//user���ѯuserCropId�б�ָ��id��
	public List<Integer> getUserCropIdById(int id) {
		User user = User.dao.findById(id);
		List<Integer> list = new ArrayList<Integer>();
		if(user != null) {
			for(int i = 1;i < 19;i++) {
				int userCropId = user.getInt("land"+i);
				if(userCropId > 0) {
					list.add(userCropId);
				}
			}
			return list;
		}
		return null;		
	}
	//user���ж�ĳ�������Ƿ���ڣ�ָ��accout��
	public boolean isExistUserByAccoutAll(String accout){
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//user���ж��Ƿ����ĳ�����ݣ�ָ��accout��exist=1��
	public boolean isExistUserByAccout(String accout){
		List<User> list = User.dao.find("select * from user where accout=? and exist=1",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//user���ж�ĳ�������Ƿ���ڣ���accout1�⣨ָ��accout2��
	public boolean isExistUserByAccout(String accout1,String accout2){
		List<User> list = User.dao.find("select * from user where accout=? and accout!=?",accout2,accout1);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//user���ж��Ƿ��QQ(ָ��accout)
	public boolean isBindingQQ(String accout) {
		List<UserAuthority> list = UserAuthority.dao.find("select userauthority.* from user,userauthority where user.id=userauthority.userId and user.accout=?",accout);
		if(list.size() != 0) {
			return true;
		}
		return false;
	}
	//user���ж��Ƿ��Ѵ��ڸ�email
	public boolean isBindingEmail(String email) {
		List<User> list = User.dao.find("select * from user where email=?",email);
		if(list.size() != 0) {
			return true;
		}
		return false;
	}
	//user���ж��Ƿ��Ѵ��ڸ�photoName
	public boolean isExistPhotoName(String photoName) {
		List<User> list = User.dao.find("select * from user where photoName=?",photoName);
		if(list.size() != 0) {
			return true;
		}
		return false;
	}
	//userauthority���ж�ĳ�������Ƿ���ڣ�ָ��openId��
	public boolean isExistUserByOpenIdAll(String openId){
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where openId=?",openId);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//userauthority���ж��Ƿ����ĳ�����ݣ�ָ��openId��exist=1��
	public boolean isExistUserByOpenId(String openId){
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where openId=? and exist=1",openId);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//userauthority���ж�ĳ�������Ƿ���ڣ�ָ��userId��
	public boolean isExistUserByUserId(int userId){
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where userId=?",userId);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}

}
