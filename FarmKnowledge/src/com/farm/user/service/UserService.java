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
	 * 	��
	 * @throws
	 */
	//����û���Ϣ����Ȩid���˺š�������ͷ�񡢵�½���ͣ���User��UserAuthority��
	public boolean addUser(String openId, String nickName, String photo,String type, String photoName){
		boolean succeed = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				UserDao userDao = new UserDao();
				
				//user����������ͷ��
				boolean a1 = userDao.addUser(generateAccout(), nickName, photo, photoName);
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
	
	
	
	/**
	 * 	ɾ
	 * @throws
	 */
	//����ɾ��User�����û���Ϣ��User��delete��
	public boolean deleteThoroughUser(int userId) {
		return new UserDao().deleteThoroughUser(userId);
	}
	//����ɾ��UserAuthority������Ȩ��Ϣ��User��delete��
	public boolean deleteThoroughUserAuthority(int userId) {
		return new UserDao().deleteThoroughUserAuthority(userId);
	}
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//�޸��û���Ϣ���˺š�������ͷ�񣩸����޸�ǰ�˺�������
	public boolean updateUser(String oldAccout, String newAccout, String nickName, String photo, String photoName) {
		return new UserDao().updateUser(oldAccout, newAccout, nickName, photo, photoName);
	}
	//��������󣬼��ٽ��
	public boolean decreaseMoney(int id, int money) {
		return new UserDao().decreaseMoney(id, money);
	}
	//��ӽ�ˮ��ʩ�ʣ�User��
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
	//����û����飬���(User��
	public boolean addEandM(int id,int ex,int money) {
		return new UserDao().addExandMoney(id, ex, money);
	}
	//���ٽ�ˮ��User��
	public boolean lessW(int id) {
		return new UserDao().lessWater(id);
	}
	//����ʩ�ʣ�User��
	public boolean lessF(int id) {
		return new UserDao().lessFertilizer(id);
	}
	//�����û�ָ��������ֲ��������
	public boolean updateLandCrop(int userId,String landNumber,int userCropId) {
		return new UserDao().updateLandCrop(userId, landNumber, userCropId);
	}
	//ɾ��User���ڵ����û���Ϣ��User���޸�exist�ֶ�Ϊ0��
	public boolean deleteOneUser(int userId) {
		return new UserDao().deleteOneUser(userId);
	}
	//ɾ��UserAuthority���ڵ�����Ȩ��Ϣ��UserAuthority���޸�exist�ֶ�Ϊ0��
	public boolean deleteOneUserAuthority(int userId) {
		return new UserDao().deleteOneUserAuthority(userId);
	}
	//�ָ�User���ڵ����û���Ϣ��User���޸�exist�ֶ�Ϊ1��
	public boolean recoveryOneUser(int userId) {
		return new UserDao().recoveryOneUser(userId);
	}
	//�ָ�UserAuthority���ڵ�����Ȩ��Ϣ��UserAuthority���޸�exist�ֶ�Ϊ1��
	public boolean recoveryOneUserAuthority(int userId) {
		return new UserDao().recoveryOneUserAuthority(userId);
	}
	
	
	
	/**
	 * 	��
	 * @throws
	 */
	//��ѯUser�����û���Ϣ��User��
	public Page<User> findUserPage(int pageNumber,int everyCount,String accout,int exist) {
		return new UserDao().findUserPage(pageNumber,everyCount,accout,exist);
	}
	//����openId��ѯUser�����û���Ϣ��User��UserAuthority��
	public User findUserByOpenId(String openId){
        return new UserDao().findUserByOpenId(openId);
    }
	//����openId�ж�UserAuthority�����Ƿ���ڸ��û�exist=1��UserAuthority��
	public boolean isExistUserByOpenId(String openId){
        return new UserDao().isExistUserByOpenId(openId);
    }
	//����openId�ж�UserAuthority�����Ƿ���ڸ��û���UserAuthority��
	public boolean isExistUserByOpenIdAll(String openId){
		return new UserDao().isExistUserByOpenIdAll(openId);
	}
	//�����˺��ж�User�����Ƿ���ڸ��û���User��
	public boolean isExistUserByAccout(String accout){
        return new UserDao().isExistUserByAccout(accout);
    }
	//�����˺��ж�User�����Ƿ���ڸ��û�����ָ���˺��⣨User��
	public boolean isExistUserByAccout(String accout1,String accout2){
        return new UserDao().isExistUserByAccout(accout1,accout2);
    }
	//�����û�id��ȡ��Ҫ�޸ĵ��û���Ϣ���˺š�������ͷ��
	public User getUpdateUserInfo(int id) {
		return new UserDao().getUpdateUserInfo(id);
	}
	//����userId��landId��ѯuserCropId
	public int findUcId(int userId,String landNumber) {
		return new UserDao().findUcIdByLand(userId, landNumber);
	}
	//����userId��ѯuserCropId�б�
	public List<Integer> getUserCropIdById(int id) {
		return new UserDao().getUserCropIdById(id);
	}
	//User���ѯʣ�ཱ������
	public int getRewardCount(int id) {
		return new UserDao().getRewardCount(id);
	}
	
	//�����˺�
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
