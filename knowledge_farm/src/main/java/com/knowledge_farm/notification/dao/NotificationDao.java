package com.knowledge_farm.notification.dao;

import com.knowledge_farm.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @ClassName NotificationDao
 * @Description
 * @Author 张帅华
 * @Date 2020-04-21 09:44
 */
public interface NotificationDao extends JpaRepository<Notification, Integer> {
    @Query("select n from Notification n where n.from.id = ?1 and n.notificationType.id = ?2")
    public Page<Notification> findSendByNotificationType(Integer userId, Integer typeId, Pageable pageable);

    @Query("select n from Notification n where n.to.id = ?1 and n.notificationType.id = ?2")
    public Page<Notification> findReceivedByNotificationType(Integer userId, Integer typeId, Pageable pageable);

    @Query("select n from Notification n where n.to.id = ?1 and n.notificationType.id = ?2 and n.haveRead = 0")
    public Page<Notification> findReceivedAddFriendNotification(Integer userId, Integer typeId, Pageable pageable);

    @Query("select n " +
            "from Notification n " +
            "where (n.to.id = ?1 or n.to.id is null) and n.createTime >= (select u.registerTime from User u where u.id = ?1) and n.notificationType.id = ?2")
    public Page<Notification> findReceivedSystemNotificationByNotificationType(Integer userId, Integer typeId, Pageable pageable);

    @Query("select n " +
            "from Notification n " +
            "where (n.to.id = ?1 and n.haveRead = 0) " +
            "or (n.from.id = ?1 and n.haveRead in (-2,2)) " +
            "or (n.to is null and n.createTime >= (select u.lastReadTime from User u where u.id = ?1))")
    public List<Notification> isHavingNewNotification(Integer userId);

    @Query("select n from Notification n where n.from.id = ?1 and n.to.id = ?2 and n.notificationType.id = ?3")
    Notification findNotificationByUser(Integer userFromId, Integer userToId, Integer typeId);

    @Query("select n from Notification n where (n.from.id = ?1 and n.to.id = ?2) or (n.from.id = ?2 and n.to.id = ?1) and n.notificationType.id = ?3")
    Notification findNotificationByUser2(Integer userFromId, Integer userToId, Integer typeId);

    @Modifying
    @Query("delete from Notification n where n.to.id = ?1 and n.notificationType.id = ?2 and n.haveRead = 1")
    public int deleteNotificationByType(Integer userId, Integer typeId);

    @Modifying
    @Query("delete from Notification n where n.from.id = ?1 and n.notificationType.id = ?2 and n.haveRead in (-1,1)")
    public int deleteNotificationByType2(Integer userId, Integer typeId);

    public Notification findNotificationById(Integer id);

    @Query("select n from Notification n where n.from.id = ?1 and n.to.account = ?2 and n.haveRead = 0 and n.notificationType.id = 2")
    public Notification findNotificationByUserAndHaveRead(Integer userFromId, String account);

    @Query("select n from Notification n where n.from.id in ?1 and n.to.id = ?2 and n.notificationType.id = ?3")
    public List<Notification> findNotificationListByFromAndTo(List<Integer> idList, Integer id, Integer typeId);

    @Query("select n from Notification n where n.from.id = ?1 and n.to.id in ?2 and n.notificationType.id = ?3")
    public List<Notification> findNotificationListByFromAndTo2(Integer id, List<Integer> idList, Integer typeId);
}
