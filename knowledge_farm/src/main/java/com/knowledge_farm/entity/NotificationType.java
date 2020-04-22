package com.knowledge_farm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author 张帅华
 * @Description 
 * @Date 19:07 2020/4/20 0020
 * @Param 
 * @return 
 **/
@Entity
@Table(name = "notification_type")
public class NotificationType {
    private int id;
    private String name;
    private Set<Notification> notifications = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "notificationType")
    @JsonIgnore
    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }
    
}
