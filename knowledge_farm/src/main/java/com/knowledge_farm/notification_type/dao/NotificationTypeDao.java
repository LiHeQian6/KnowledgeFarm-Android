package com.knowledge_farm.notification_type.dao;

import com.knowledge_farm.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName NotificationType
 * @Description
 * @Author 张帅华
 * @Date 2020-04-21 10:03
 */
public interface NotificationTypeDao extends JpaRepository<NotificationType, Integer> {
    public NotificationType findNotificationTypeById(Integer id);
    public NotificationType findNotificationTypeByName(String name);
}
