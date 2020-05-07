package com.knowledge_farm.notification.service;

import com.google.gson.Gson;
import com.knowledge_farm.entity.*;
import com.knowledge_farm.jpush.service.JpushService;
import com.knowledge_farm.notification.dao.NotificationDao;
import com.knowledge_farm.notification_type.service.NotificationTypeService;
import com.knowledge_farm.user.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName NotificationService
 * @Description
 * @Author 张帅华
 * @Date 2020-04-21 09:45
 */
@Service
@Transactional(readOnly = true)
public class NotificationService {
    @Resource
    private NotificationDao notificationDao;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private NotificationTypeService notificationTypeService;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional(readOnly = false)
    public Page<Notification> findReceivedByNotificationType(Integer userId, Integer typeId, Integer pageNumber, Integer pageSize){
        if(typeId == 1){
            User user = this.userService.findUserById(userId);
            user.setLastReadTime(new Date());
            return this.notificationDao.findReceivedSystemNotificationByNotificationType(userId, 1, PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
        }else if(typeId == 2){
            return this.notificationDao.findReceivedAddFriendNotification(userId, 2, PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
        }
        return this.notificationDao.findReceivedByNotificationType(userId, typeId, PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
    }

    public Page<Notification> findSendByNotificationType(Integer userId, Integer typeId, Integer pageNumber, Integer pageSize){
        return this.notificationDao.findSendByNotificationType(userId, typeId, PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
    }

    public List<Boolean> isHavingNewNotification(Integer userId){
        User user = this.userService.findUserById(userId);
        List<Boolean> isHavingRead = new ArrayList<>();
        for(int i = 0;i < 5;i++){
            isHavingRead.add(false);
        }

        List<Notification> notifications = this.notificationDao.isHavingNewNotification(userId);
        logger.info("新Notification消息个数"  + notifications.size());
        for(Notification notification : notifications){
            switch (notification.getNotificationType().getId()){
                case 1:
                    isHavingRead.set(0, true);
                    break;
                case 2:
                    if(notification.getFrom() == user){
                        isHavingRead.set(2, true);
                    }else{
                        isHavingRead.set(1, true);
                    }
                    break;
                case 3:
                    isHavingRead.set(3, true);
                    break;
            }
        }

        Task task = user.getTask();
        if(task.getSignIn() == 0 || task.getWater() == 1 || task.getFertilize() == 1 || task.getCrop() == 1 || task.getHarvest() == 1 || task.getHelpWater() == 1 || task.getHelpFertilize() == 1){
            isHavingRead.set(4, true);
        }
        return isHavingRead;
    }

    @Transactional(readOnly = false)
    public void addUserFriendNotification(Integer userId, String account){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserByAccount(account);
        Notification find = this.notificationDao.findNotificationByFromAndTo(userId, account);
        if(find != null){
            return;
        }
        String title = "新朋友";
        String content = friendUser.getNickName() + "请求添加你为好友";
        Map extra = new HashMap<>();
        Notification notification = new Notification();
        NotificationType notificationType = this.notificationTypeService.findNotificationTypeById(2);
        notification.setFrom(user);
        notification.setTo(friendUser);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setExtra(new Gson().toJson(extra));
        notification.setCreateTime(new Date());
        notification.setHaveRead(0);
        notification.setNotificationType(notificationType);
        this.notificationDao.save(notification);
    }

    @Transactional(readOnly = false)
    public void addWaterForFriendNotification(Integer userId, Integer friendId){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserById(friendId);
        String title = "新消息";
        String content = friendUser.getNickName() + "给你浇水了";
        Map extra = new HashMap<>();
        Notification notification = new Notification();
        NotificationType notificationType = this.notificationTypeService.findNotificationTypeById(3);
        notification.setFrom(user);
        notification.setTo(friendUser);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setExtra(new Gson().toJson(extra));
        notification.setCreateTime(new Date());
        notification.setHaveRead(0);
        notification.setNotificationType(notificationType);
        this.notificationDao.save(notification);
    }

    @Transactional(readOnly = false)
    public void addFertilizerForFriendNotification(Integer userId, Integer friendId){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserById(friendId);
        String title = "新消息";
        String content = friendUser.getNickName() + "给你施肥了";
        Map extra = new HashMap<>();
        Notification notification = new Notification();
        NotificationType notificationType = this.notificationTypeService.findNotificationTypeById(3);
        notification.setFrom(user);
        notification.setTo(friendUser);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setExtra(new Gson().toJson(extra));
        notification.setCreateTime(new Date());
        notification.setHaveRead(0);
        notification.setNotificationType(notificationType);
        this.notificationDao.save(notification);
    }

    @Transactional(readOnly = false)
    public void addSystemNotification(String title, String content, Map extra, String... alias){
        List<Notification> notificationList = new ArrayList<>();
        if(alias.length != 0){
            for(String arr : alias){
                User user = this.userService.findUserByAccount(arr);
                Notification notification = new Notification();
                NotificationType notificationType = this.notificationTypeService.findNotificationTypeById(1);
                notification.setFrom(null);
                notification.setTo(user);
                notification.setTitle(title);
                notification.setContent(content);
                notification.setExtra(new Gson().toJson(extra));
                notification.setCreateTime(new Date());
                notification.setHaveRead(0);
                notification.setNotificationType(notificationType);
                notificationList.add(notification);
            }
            this.notificationDao.saveAll(notificationList);
        }else{
            Notification notification = new Notification();
            NotificationType notificationType = this.notificationTypeService.findNotificationTypeById(1);
            notification.setFrom(null);
            notification.setTo(null);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setExtra(new Gson().toJson(extra));
            notification.setCreateTime(new Date());
            notification.setHaveRead(0);
            notification.setNotificationType(notificationType);
            this.notificationDao.save(notification);
        }
    }

    @Transactional(readOnly = false)
    public void deleteNotification(List<Integer> idList){
        List<Notification> notifications = this.notificationDao.findAllById(idList);
        this.notificationDao.deleteAll(notifications);
    }

    @Transactional(readOnly = false)
    public void deleteNotificationByType(Integer userId, Integer typeId){
        if(typeId != 2 && typeId != 1){
            this.notificationDao.deleteNotificationByType(userId, typeId);
        }else if(typeId == 2){
            this.notificationDao.deleteNotificationByType2(userId, 2);
        }
    }

    @Transactional(readOnly = false)
    public void editNotificationReadStatus(List<Integer> idList, Integer haveRead){
        List<Notification> notifications = this.notificationDao.findAllById(idList);
        for(Notification notification : notifications){
            notification.setHaveRead(haveRead);
        }
        this.notificationDao.saveAll(notifications);
    }

}
