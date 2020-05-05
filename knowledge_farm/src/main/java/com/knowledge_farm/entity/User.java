package com.knowledge_farm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName User
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 13:35
 */
@Entity
@Table(name = "user")
//@NamedEntityGraph(name = "Category.Graph", attributeNodes = {@NamedAttributeNode("land"), @NamedAttributeNode("userAuthority"), @NamedAttributeNode("userBags"), @NamedAttributeNode("task")})
public class User {
    private Integer id;
    private String account;
    private String password;
    private String nickName;
    private String photo;
    private String email;
    private Integer level;
    private Integer experience;
    private Integer grade;
    private Integer money;
    private Integer mathRewardCount;
    private Integer englishRewardCount;
    private Integer chineseRewardCount;
    private Integer water;
    private Integer fertilizer;
    private Integer online;
    private Integer exist;
    private Date lastReadTime;
    private Land land;
    private UserAuthority userAuthority;
    private Set<UserCropBag> userCropBags = new HashSet<>();
    private Task task;
    private Set<UserFriend> userFriends = new HashSet<>();
    private Set<Notification> sendNotifications = new HashSet<>();
    private Set<Notification> ReceiveNotifications = new HashSet<>();
    private Set<UserPetHouse> petHouses = new HashSet<>();
    private Set<UserPetUtilBag> userPetUtilBags = new HashSet<>();

    public User(){
        this.level = 1;
        this.experience = 0;
        this.money = 1000;
        this.mathRewardCount = 3;
        this.chineseRewardCount = 3;
        this.englishRewardCount = 3;
        this.water = 0;
        this.fertilizer = 0;
        this.online = 1;
        this.exist = 1;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "nick_name")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(columnDefinition = "int default 1")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Column(columnDefinition = "int default 0")
    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    @Column
    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Column(columnDefinition = "int default 1000")
    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    @Column(name = "math_reward_count", columnDefinition = "int default 3")
    public Integer getMathRewardCount() {
        return mathRewardCount;
    }

    public void setMathRewardCount(Integer mathRewardCount) {
        this.mathRewardCount = mathRewardCount;
    }

    @Column(name = "english_reward_count", columnDefinition = "int default 3")
    public Integer getEnglishRewardCount() {
        return englishRewardCount;
    }

    public void setEnglishRewardCount(Integer englishRewardCount) {
        this.englishRewardCount = englishRewardCount;
    }

    @Column(name = "chinese_reward_count", columnDefinition = "int default 3")
    public Integer getChineseRewardCount() {
        return chineseRewardCount;
    }

    public void setChineseRewardCount(Integer chineseRewardCount) {
        this.chineseRewardCount = chineseRewardCount;
    }

    @Column(columnDefinition = "int default 0")
    public Integer getWater() {
        return water;
    }

    public void setWater(Integer water) {
        this.water = water;
    }

    @Column(columnDefinition = "int default 0")
    public Integer getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(Integer fertilizer) {
        this.fertilizer = fertilizer;
    }

    @Column(columnDefinition = "int default 1")
    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    @Column(columnDefinition = "int default 1")
    public Integer getExist() {
        return exist;
    }

    public void setExist(Integer exist) {
        this.exist = exist;
    }

    @Column(name = "last_read_time")
    public Date getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Date lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @org.hibernate.annotations.ForeignKey(name = "none")
    @JsonIgnore
    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @org.hibernate.annotations.ForeignKey(name = "none")
    public UserAuthority getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(UserAuthority userAuthority) {
        this.userAuthority = userAuthority;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @org.hibernate.annotations.ForeignKey(name = "none")
    @JsonIgnore
    public Set<UserCropBag> getUserCropBags() {
        return userCropBags;
    }

    public void setUserCropBags(Set<UserCropBag> userCropBags) {
        this.userCropBags = userCropBags;
    }

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "task_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @org.hibernate.annotations.ForeignKey(name = "none")
    @JsonIgnore
    public Set<UserFriend> getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(Set<UserFriend> userFriends) {
        this.userFriends = userFriends;
    }

    @OneToMany(mappedBy = "from")
    @org.hibernate.annotations.ForeignKey(name = "none")
    @JsonIgnore
    public Set<Notification> getSendNotifications() {
        return sendNotifications;
    }

    public void setSendNotifications(Set<Notification> sendNotifications) {
        this.sendNotifications = sendNotifications;
    }

    @OneToMany(mappedBy = "to")
    @org.hibernate.annotations.ForeignKey(name = "none")
    @JsonIgnore
    public Set<Notification> getReceiveNotifications() {
        return ReceiveNotifications;
    }

    public void setReceiveNotifications(Set<Notification> receiveNotifications) {
        ReceiveNotifications = receiveNotifications;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @org.hibernate.annotations.ForeignKey(name = "none")
    public Set<UserPetHouse> getPetHouses() {
        return petHouses;
    }

    public void setPetHouses(Set<UserPetHouse> petHouses) {
        this.petHouses = petHouses;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @org.hibernate.annotations.ForeignKey(name = "none")
    @JsonIgnore
    public Set<UserPetUtilBag> getUserPetUtilBags() {
        return userPetUtilBags;
    }

    public void setUserPetUtilBags(Set<UserPetUtilBag> userPetUtilBags) {
        this.userPetUtilBags = userPetUtilBags;
    }

}
