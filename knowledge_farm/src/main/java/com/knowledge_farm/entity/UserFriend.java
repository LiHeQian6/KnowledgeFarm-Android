package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName UserFriend
 * @Description
 * @Author 张帅华
 * @Date 2020-04-10 22:34
 */
@Entity
@Table(name = "user_friend")
public class UserFriend {
    private Integer id;
    private User user;
    private User friendUser;
    private Integer status;

    public UserFriend(){
        this.status = 1;
    }

    @Id
    @GeneratedValue(generator="identity")
    @GenericGenerator(name="identity", strategy="identity")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "friend_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    public User getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(User friendUser) {
        this.friendUser = friendUser;
    }

    @Column( columnDefinition = "int default 1")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
