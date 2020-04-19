package com.knowledge_farm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    private Land land;
    private UserAuthority userAuthority;
    private Set<UserBag> userBags = new HashSet<>();

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

    @Column(insertable = false)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Column(insertable = false)
    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Column(insertable = false)
    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    @Column(name = "math_reward_count", insertable = false)
    public Integer getMathRewardCount() {
        return mathRewardCount;
    }

    public void setMathRewardCount(Integer mathRewardCount) {
        this.mathRewardCount = mathRewardCount;
    }

    @Column(name = "english_reward_count", insertable = false)
    public Integer getEnglishRewardCount() {
        return englishRewardCount;
    }

    public void setEnglishRewardCount(Integer englishRewardCount) {
        this.englishRewardCount = englishRewardCount;
    }

    @Column(name = "chinese_reward_count", insertable = false)
    public Integer getChineseRewardCount() {
        return chineseRewardCount;
    }

    public void setChineseRewardCount(Integer chineseRewardCount) {
        this.chineseRewardCount = chineseRewardCount;
    }

    @Column(insertable = false)
    public Integer getWater() {
        return water;
    }

    public void setWater(Integer water) {
        this.water = water;
    }

    @Column(insertable = false)
    public Integer getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(Integer fertilizer) {
        this.fertilizer = fertilizer;
    }

    @Column(insertable = false)
    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    @Column(insertable = false)
    public Integer getExist() {
        return exist;
    }

    public void setExist(Integer exist) {
        this.exist = exist;
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    public UserAuthority getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(UserAuthority userAuthority) {
        this.userAuthority = userAuthority;
    }

    @OneToMany(targetEntity=UserBag.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public Set<UserBag> getUserBags() {
        return userBags;
    }

    public void setUserBags(Set<UserBag> userBags) {
        this.userBags = userBags;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", photo='" + photo + '\'' +
                ", email='" + email + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                ", grade=" + grade +
                ", money=" + money +
                ", mathRewardCount=" + mathRewardCount +
                ", englishRewardCount=" + englishRewardCount +
                ", chineseRewardCount=" + chineseRewardCount +
                ", water=" + water +
                ", fertilizer=" + fertilizer +
                ", online=" + online +
                ", exist=" + exist +
                ", land=" + land +
                ", userAuthority=" + userAuthority +
                ", userBags=" + userBags +
                '}';
    }

}
