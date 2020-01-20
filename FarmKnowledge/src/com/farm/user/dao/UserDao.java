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
	//User������˺š�������ͷ��accout��nickName��photo��
	public boolean addUser(String accout, String nickName, String password, String photo, String photoName, String email, int grade){
		boolean succeed =  new User().set("accout", accout).set("nickName", nickName).set("password", password).set("photo", photo).set("photoName", photoName).set("email", email).set("grade", grade).save();
		return succeed;
	}
	//UserAuthority���ڲ���userId��openId��type
	public boolean addUserAuthority(int userId, String openId, String type){
		boolean succeed =  new UserAuthority().set("userId", userId).set("openId", openId).set("type", type).save();
		return succeed;
	}
	
	
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	//ɾ��openId��Ϣ
	public boolean deleteOpenIdByUserId(int userId) {
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where userId=?",userId);
		if(list.size() != 0) {
			boolean succeed = UserAuthority.dao.deleteById(list.get(0).getInt("id"));
			return succeed;
		}
		return false;
	}
	//����ɾ��User�����û���Ϣ��User��delete��
	public boolean deleteThoroughUser(int userId) {
		boolean succeed = User.dao.deleteById(userId);
		return succeed;
	}
	//����ɾ��UserAuthority������Ȩ��Ϣ��User��delete��
	public boolean deleteThoroughUserAuthority(int userId) {
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where userId=?",userId);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = UserAuthority.dao.deleteById(list.get(0).get("id"));
		}
		return succeed;
	}
	
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�޸��û���Ϣ�������޸�ǰ�˺�������
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
	//�޸��û��ǳƣ������˺Ų�ѯ��
	public boolean updateUserNickName(String accout, String nickName) {
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("nickName", nickName).update();
		}
		return succeed;
	}
	//�޸��û��꼶�������˺Ų�ѯ��
	public boolean updateUserGrade(String accout, int grade) {
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("grade", grade).update();
		}
		return succeed;
	}	
	//�������룬�����˺Ų�ѯ��
	public boolean updateUserPassword(String accout,String password) {
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			User user = list.get(0);
			return user.set("password", password).update();
		}
		return false;
	}
	//�޸��û���ͷ�񣬸����˺Ų�ѯ��
	public boolean updateUserPhoto(String accout, String photo, String photoName) {
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			boolean succeed = list.get(0).set("photo", photo).set("photoName", photoName).update();
			return succeed;
		}
		return false;
	}
	//����email�������˺Ų�ѯ��
	public boolean updateUserEmail(String accout, String email) {
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			boolean succeed = list.get(0).set("email", email).update();
			return succeed;
		}
		return false;
	}
	//��������󣬼��ٽ��
	public boolean decreaseMoney(int id, int money) {
		User user = User.dao.findById(id);
		if(user != null) {
			return user.set("money", user.getInt("money")-money).update();
		}
		return false;
	}
	//User�����ӽ�ˮ��ʩ�ʴ�����water��fertilizer��
	public boolean addWaterAndFertilizer(int id, int water, int fertilizer) {
		User user = User.dao.findById(id);
		boolean succeed = false;
		if(user != null) {
			succeed = user.set("water", user.getInt("water")+water).set("fertilizer", user.getInt("fertilizer")+fertilizer).update();
		}
		return succeed;
	}
	//User�����Ӿ��飬��ң�experience��money��
	public boolean addExandMoney(int id,int ex,int money) {
		User user = User.dao.findById(id);
		if(user != null) {
			return user.set("experience", user.getInt("experience")+ex).set("money", user.getInt("money")+money).update();
		}
		return false;
	}
	//User�����ӽ�ң�money��
	public boolean addMoney(int id, int money) {
		User user = User.dao.findById(id);
		if(user != null) {
			return user.set("money", user.getInt("money")+money).update();
		}
		return false;
	}
	//User����ٽ�ˮ��water��
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
	//User�����ʩ�ʣ�fertilizer��
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
	//�����û�ָ��������ֲ��������
	public boolean updateLandCrop(int userId,String landNumber,int userCropId) {
		return User.dao.findById(userId).set(landNumber, userCropId).update();
	}
	//���ý�������
	public boolean lessRewardCount(int id, int rewardCount, String subject) {
		User user = User.dao.findById(id);
		if(user != null) {
			return User.dao.findById(id).set(subject+"RewardCount", rewardCount).update();
		}
		return false;
	}
	//��������
	public boolean extensionLand(int userId, String landNumber) {
		boolean succeed = User.dao.findById(userId).set(landNumber, 0).update();
		return succeed;
	}
	//ɾ��User���ڵ����û���Ϣ��User���޸�exist�ֶ�Ϊ0��
	public boolean deleteOneUser(int userId) {
		boolean succeed = User.dao.findById(userId).set("exist", 0).update();
		return succeed;
	}
	//ɾ��UserAuthority���ڵ�����Ȩ��Ϣ��UserAuthority���޸�exist�ֶ�Ϊ0��
	public boolean deleteOneUserAuthority(int userId) {
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where userId=?",userId);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("exist", 0).update();
		}
		return succeed;
	}
	//�ָ�User���ڵ����û���Ϣ��User���޸�exist�ֶ�Ϊ1��
	public boolean recoveryOneUser(int userId) {
		boolean succeed = User.dao.findById(userId).set("exist", 1).update();
		return succeed;
	}
	//�ָ�UserAuthority���ڵ�����Ȩ��Ϣ��UserAuthority���޸�exist�ֶ�Ϊ1��
	public boolean recoveryOneUserAuthority(int userId) {
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where userId=?",userId);
		boolean succeed = false;
		if(list.size() != 0) {
			succeed = list.get(0).set("exist", 1).update();
		}
		return succeed;
	}
	//�޸�land1-18
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
	//����openId��ѯUser�����û���Ϣ
	public User findUserByOpenId(String openId){
		List<User> list = User.dao.find("select user.* from user,userauthority where user.id=userauthority.userId and userauthority.openId=?",openId);
		if(list.size() != 0) {
			User user = list.get(0);
			user.set("photo", URLEncoder.encode(Strings.userPhotoUrl + user.get("photo")));
			return user;
		}
		return null;
	}
	//�����˺Ų�ѯUser�����û���Ϣ
	public User findUserByAccout(String accout){
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			User user = list.get(0);
			user.set("photo", URLEncoder.encode(Strings.userPhotoUrl + user.get("photo")));
			return user;
		}
		return null;
	}
	//����openId�ж�UserAuthority�����Ƿ���ڸ��û�exist=1
	public boolean isExistUserByOpenId(String openId){
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where openId=? and exist=1",openId);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//����openId�ж�UserAuthority�����Ƿ���ڸ��û�
	public boolean isExistUserByOpenIdAll(String openId){
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where openId=?",openId);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//�����˺��ж�User�����Ƿ���ڸ��û���User��
	public boolean isExistUserByAccout(String accout){
		List<User> list = User.dao.find("select * from user where accout=? and exist=1",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//�����˺��ж�User�����Ƿ���ڸ��û���User��
	public boolean isExistUserByAccoutAll(String accout){
		List<User> list = User.dao.find("select * from user where accout=?",accout);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//�����˺��ж�User�����Ƿ���ڸ��û�����ָ���˺��⣨User��
	public boolean isExistUserByAccout(String accout1,String accout2){
		List<User> list = User.dao.find("select * from user where accout=? and accout!=?",accout2,accout1);
		if(list.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	//�˺������ж�����
	public User findUserByAccountPassword(String account,String pwd){
		List<User> list = User.dao.find("select * from user where accout=? and password=?",account,pwd);
		if(list.size() != 0) {
			User user = list.get(0);
			user.set("photo", URLEncoder.encode(Strings.userPhotoUrl + user.getStr("photo")));
			return user;
		}
		return null;
	}

	//�����˺Ų�ѯ�û�id
	public int getUserIdByAccout(String accout) {
		if(!(""+Db.queryInt("select id from user where accout=?",accout)).equals("null") ) {
			return Db.queryInt("select id from user where accout=?",accout);
		}
		return 0;
	}
	//���User�����һ�����ݵ�userId
	public int getLastUserId(){
		int id =  Db.queryInt("select id from user order by id desc limit 1");
		return id;
	}
	//��ѯUser�����û���Ϣ��User��
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
	//��ѯUser�����û���Ϣ��User��
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
	//��̨ �����û�id��ȡ��Ҫ�޸ĵ��û���Ϣ
	public User getUpdateUserInfo0(int id) {
		User user = User.dao.findById(id);
		return user;
	}
	//�����û�id��ȡ��Ҫ�޸ĵ��û���Ϣ
	public User getUpdateUserInfo(int id) {
		User user = User.dao.findById(id);
		user.set("photo", Strings.userPhotoUrl + user.getStr("photo"));
		return user;
	}
	//����userId��ѯuserCropId�б�
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
	//����userId��landId��ѯuserCropId
	public int findUcIdByLand(int userId,String landNumber) {
		User user = User.dao.findById(userId);
		if(user != null) {
			int ucId = user.getInt(landNumber);
			return ucId;
		}
		return 0;
	}	
	//�ж��˺��Ƿ��QQ
	public boolean isBindingQQ(String accout) {
		int userId = getUserIdByAccout(accout);
		List<UserAuthority> list = UserAuthority.dao.find("select * from userauthority where userId=?",userId);
		if(list.size() != 0) {
			return true;
		}
		return false;
	}
	//�ж������Ƿ��ѱ������˺Ű�
	public boolean isBindingEmail(String email) {
		List<User> list = User.dao.find("select * from user where email=?",email);
		if(list.size() != 0) {
			return true;
		}
		return false;
	}
	//��ѯ�Ƿ��Ѵ��ڸ�photoName
	public boolean isExistPhotoName(String photoName) {
		List<User> list = User.dao.find("select * from user where photoName=?",photoName);
		if(list.size() != 0) {
			return true;
		}
		return false;
	}

}
