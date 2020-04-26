package com.knowledge_farm.notification.service;

import com.google.gson.Gson;
import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.entity.NotificationType;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.notification.dao.NotificationDao;
import com.knowledge_farm.notification_type.service.NotificationTypeService;
import com.knowledge_farm.user.service.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Page<Notification> findReceivedByNotificationType(Integer userId, Integer typeId, Integer pageNumber, Integer pageSize){
        if(typeId == 1){
            return this.notificationDao.findReceivedByNotificationType2(userId, typeId, PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
        }
        return this.notificationDao.findReceivedByNotificationType(userId, typeId, PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
    }

    public Page<Notification> findSendByNotificationType(Integer userId, Integer typeId, Integer pageNumber, Integer pageSize){
        return this.notificationDao.findSendByNotificationType(userId, typeId, PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
    }

//    public List<Notification> findReceivedByNotificationType(Integer userId, Integer typeId){
//        if(typeId == 1){
//            return this.notificationDao.findReceivedByNotificationType2(userId, typeId, Sort.by(Sort.Direction.DESC, "createTime"));
//        }
//        return this.notificationDao.findReceivedByNotificationType(userId, typeId, Sort.by(Sort.Direction.DESC, "createTime"));
//    }
//
//    public List<Notification> findSendByNotificationType(Integer userId, Integer typeId){
//        return this.notificationDao.findSendByNotificationType(userId, typeId, Sort.by(Sort.Direction.DESC, "createTime"));
//    }

    @Transactional(readOnly = false)
    public Notification addUserFriendNotification(Integer userId, String account){
        User user = this.userService.findUserById(userId);
        User friendUser = this.userService.findUserByAccount(account);
        String title = "新朋友";
        String content = friendUser.getNickName() + "请求添加你为好友";
        Map<String, String> extra = new HashMap<>();
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
        this.notificationDao.save(notification);
        return notification;
    }

    @Transactional(readOnly = false)
    public void deleteNotification(Integer notificationId){
        this.notificationDao.deleteById(notificationId);
    }

    @Transactional(readOnly = false)
    public void editNotificationReadStatus(List<Integer> idList){
        List<Notification> notifications = this.notificationDao.findAllById(idList);
        for(Notification notification : notifications){
            notification.setHaveRead(true);
        }
        this.notificationDao.saveAll(notifications);
    }

}
