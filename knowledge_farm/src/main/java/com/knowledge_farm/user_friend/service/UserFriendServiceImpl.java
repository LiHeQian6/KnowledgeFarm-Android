package com.knowledge_farm.user_friend.service;

import com.google.gson.Gson;
import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.entity.NotificationType;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.entity.UserFriend;
import com.knowledge_farm.notification.service.NotificationService;
import com.knowledge_farm.notification_type.service.NotificationTypeService;
import com.knowledge_farm.user.service.UserServiceImpl;
import com.knowledge_farm.user_friend.dao.UserFriendDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserFriendServiceImpl
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 22:44
 */
@Service
@Transactional(readOnly = true)
public class UserFriendServiceImpl {
    @Resource
    private UserFriendDao userFriendDao;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private NotificationService notificationService;
    @Resource
    private NotificationTypeService notificationTypeService;

    public Page<User> findUserFriendPageByAccount(Integer userId, String account, Integer pageNumber, Integer pageSize){
        if(account != null && !account.equals("")){
            return this.userFriendDao.findUserFriendPageByAccount(userId, account, PageRequest.of(pageNumber - 1, pageSize));
        }
        return this.userFriendDao.findUserFriendPage(userId, PageRequest.of(pageNumber - 1, pageSize));
    }

    public Page<User> findAllUserByAccount(String account, Integer pageNumber, Integer pageSize){
        return this.userService.findAllUserByAccount(account, pageNumber, pageSize);
    }

    public Page<Notification> findReceivedNotificationByType(Integer userId, Integer typeId, Integer pageNumber, Integer pageSize){
        return this.notificationService.findReceivedByNotificationType(userId, typeId, pageNumber, pageSize);
    }

    @Transactional(readOnly = false)
    public String addUserFriendNotification(Integer userId, String account){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserByAccount(account);
        String title = "新朋友";
        String content = friendUser.getNickName() + "请求添加你为好友";
        Map<String, String> extra = new HashMap<>();
        try {
            Notification notification = new Notification();
            NotificationType notificationType = this.notificationTypeService.findNotificationTypeById(2);
            notification.setFrom(user);
            notification.setTo(friendUser);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setExtra(new Gson().toJson(extra));
            notification.setCreateTime(new Date());
            notification.setHaveRead(false);
            notification.setNotificationType(notificationType);
            this.notificationService.save(notification);
            return "succeed";
        }catch (NullPointerException e){
            return null;
        }catch (Exception e){
            return "fail";
        }
    }

    @Transactional(readOnly = false)
    public String addUserFriend(Integer userId, String account){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserByAccount(account);
        UserFriend userFriend = new UserFriend();
        try {
            userFriend.setUser(user);
            userFriend.setFriendUser(friendUser);
            this.userFriendDao.save(userFriend);
            return "true";
        }catch (Exception e){
            return "false";
        }
    }

    @Transactional(readOnly = false)
    public String deleteUserFriend(Integer userId, String account){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserByAccount(account);
        try {
            UserFriend userFriend = this.userFriendDao.findUserFriendByUserAndFriendUser(user.getId(), friendUser.getId());
            this.userFriendDao.delete(userFriend);
            return "true";
        }catch (NullPointerException e){
            return null;
        }catch (Exception e){
            return "false";
        }
    }

}
