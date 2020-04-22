package com.li.knowledgefarm.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author 张帅华
 * @Description 
 * @Date 19:07 2020/4/20 0020
 * @Param 
 * @return 
 **/
public class NotificationType {
    private int id;
    private String name;
    private Set<Notification> notifications = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }
    
}
