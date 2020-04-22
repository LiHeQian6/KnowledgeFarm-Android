package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * User: li
 * Date: 2020/4/14
 * Time: 15:24
 */
@Entity
@Table(name = "notification")
public class Notification {
    private int id;
    private User from;
    private User to;
    private String title;
    private String content;
    private String extra;
    private Date createTime;
    private boolean haveRead;
    private NotificationType notificationType;

    @Id
    @GeneratedValue(generator="identity")
    @GenericGenerator(name="identity", strategy = "identity")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_from_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    @ManyToOne
    @JoinColumn(name = "user_to_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
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

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "have_read")
    public boolean isHaveRead() {
        return haveRead;
    }

    public void setHaveRead(boolean haveRead) {
        this.haveRead = haveRead;
    }

    @ManyToOne
    @JoinColumn(name = "notification_type_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

}
