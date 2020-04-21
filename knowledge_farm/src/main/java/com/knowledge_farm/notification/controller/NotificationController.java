package com.knowledge_farm.notification.controller;

import com.knowledge_farm.entity.Notification;
import com.knowledge_farm.entity.User;
import com.knowledge_farm.notification.service.NotificationService;
import com.knowledge_farm.util.PageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Controller
 * @Description
 * @Author 张帅华
 * @Date 2020-04-21 16:46
 */
@RestController
@RequestMapping("/notification")
@PropertySource(value = {"classpath:photo.properties"})
public class NotificationController {
    @Resource
    private NotificationService notificationService;
    @Value("${file.photoUrl}")
    private String photoUrl;

//    @RequestMapping("/findReceivedNotificationByType")
//    public PageUtil<Notification> findReceivedNotificationByType(@RequestParam("userId") Integer userId,
//                                                                 @RequestParam("typeId") Integer typeId,
//                                                                 @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
//                                                                 @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
//        Page<Notification> page = this.notificationService.findReceivedByNotificationType(userId, typeId, pageNumber, pageSize);
//        PageUtil<Notification> pageUtil = new PageUtil<>(pageNumber, pageSize);
//        pageUtil.setTotalCount((int) page.getTotalElements());
//        for(Notification notification : page.getContent()){
//            User from = notification.getFrom();
//            User to = notification.getTo();
//            from.setPhoto(this.photoUrl + from.getPhoto());
//            if(!to.getPhoto().contains("http://")){
//                to.setPhoto(this.photoUrl + to.getPhoto());
//            }
//        }
//        pageUtil.setList(page.getContent());
//        return pageUtil;
//    }
//
//    @RequestMapping("/findSendNotificationByType")
//    public PageUtil<Notification> findSendNotificationByType(@RequestParam("userId") Integer userId,
//                                                             @RequestParam("typeId") Integer typeId,
//                                                             @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
//                                                             @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
//        Page<Notification> page = this.notificationService.findSendByNotificationType(userId, typeId, pageNumber, pageSize);
//        PageUtil<Notification> pageUtil = new PageUtil<>(pageNumber, pageSize);
//        pageUtil.setTotalCount((int) page.getTotalElements());
//        for(Notification notification : page.getContent()){
//            User from = notification.getFrom();
//            User to = notification.getTo();
//            from.setPhoto(this.photoUrl + from.getPhoto());
//            if(!to.getPhoto().contains("http://")){
//                to.setPhoto(this.photoUrl + to.getPhoto());
//            }
//        }
//        pageUtil.setList(page.getContent());
//        return pageUtil;
//    }

    @RequestMapping("/findReceivedNotificationByType")
    public List<Notification> findReceivedNotificationByType(@RequestParam("userId") Integer userId, @RequestParam("typeId") Integer typeId){
        List<Notification> notifications = this.notificationService.findReceivedByNotificationType(userId, typeId);
        for(Notification notification : notifications){
            User from = notification.getFrom();
            User to = notification.getTo();
            from.setPhoto(this.photoUrl + from.getPhoto());
            if(!to.getPhoto().contains("http://")){
                to.setPhoto(this.photoUrl + to.getPhoto());
            }
        }
        return notifications;
    }

    @RequestMapping("/findSendNotificationByType")
    public List<Notification> findSendNotificationByType(@RequestParam("userId") Integer userId, @RequestParam("typeId") Integer typeId){
        List<Notification> notifications = this.notificationService.findSendByNotificationType(userId, typeId);
        for(Notification notification : notifications){
            User from = notification.getFrom();
            User to = notification.getTo();
            from.setPhoto(this.photoUrl + from.getPhoto());
            if(!to.getPhoto().contains("http://")){
                to.setPhoto(this.photoUrl + to.getPhoto());
            }
        }
        return notifications;
    }

    @RequestMapping("/addUserFriendNotification")
    public String addUserFriendNotification(@RequestParam("userId") Integer userId, @RequestParam("account") String account){
        return this.notificationService.addUserFriendNotification(userId, account);
    }

    @RequestMapping("/deleteNotification")
    public String deleteNotification(@RequestParam("id") Integer notificationId){
        return this.notificationService.deleteNotification(notificationId);
    }

    @RequestMapping("/editNotificationReadStatus")
    public String editNotificationReadStatus(@RequestParam("ids") String notificationIds){
        String ids[] = notificationIds.split(",");
        List<Integer> idList = new ArrayList<>();
        for(String id : ids){
            idList.add(Integer.parseInt(id));
        }
        return this.notificationService.editNotificationReadStatus(idList);
    }

}
