package com.knowledge_farm.notification_type.service;

import com.knowledge_farm.entity.NotificationType;
import com.knowledge_farm.notification_type.dao.NotificationTypeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName NotificationService
 * @Description
 * @Author 张帅华
 * @Date 2020-04-21 10:04
 */
@Service
@Transactional(readOnly = true)
public class NotificationTypeService {
    @Resource
    private NotificationTypeDao notificationTypeDao;

    public NotificationType findNotificationTypeById(Integer id){
        return this.notificationTypeDao.findNotificationTypeById(id);
    }

    public NotificationType findNotificationTypeByName(String name){
        return this.notificationTypeDao.findNotificationTypeByName(name);
    }

}
