package com.li.knowledgefarm.entity;

import java.util.Date;

/**
 * UserUtil: li
 * Date: 2020/4/14
 * Time: 15:24
 */
public class Notification {
    private int id;
    private User from;
    private User to;
    private String title;
    private String content;
    private String extra;
    private Date createTime;
    private int haveRead;
    private NotificationType notificationType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int isHaveRead() {
        return haveRead;
    }

    public void setHaveRead(int haveRead) {
        this.haveRead = haveRead;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

}
