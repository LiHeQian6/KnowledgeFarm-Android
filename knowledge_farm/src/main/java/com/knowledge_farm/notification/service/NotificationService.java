package com.knowledge_farm.notification.service;

import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.notification.dao.NotificationDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    @Transactional(readOnly = false)
    public Notification save(Notification notification){
        return this.notificationDao.save(notification);
    }

    public Page<Notification> findReceivedByNotificationType(Integer userId, Integer typeId, Integer pageNumber, Integer pageSize){
        if(typeId == 1){
            return this.notificationDao.findReceivedByNotificationType2(userId, typeId, PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
        }
        return this.notificationDao.findReceivedByNotificationType(userId, typeId, PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
    }

}
