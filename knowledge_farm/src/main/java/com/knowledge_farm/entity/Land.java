package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName Land
 * @Description
 * @Author 张帅华
 * @Date 2020-04-07 15:33
 */
@Entity
@Table(name = "land")
public class Land {
    private Integer id;
    private User user;
    private UserCrop userCrop1;
    private UserCrop userCrop2;
    private UserCrop userCrop3;
    private UserCrop userCrop4;
    private UserCrop userCrop5;
    private UserCrop userCrop6;
    private UserCrop userCrop7;
    private UserCrop userCrop8;
    private UserCrop userCrop9;
    private UserCrop userCrop10;
    private UserCrop userCrop11;
    private UserCrop userCrop12;
    private UserCrop userCrop13;
    private UserCrop userCrop14;
    private UserCrop userCrop15;
    private UserCrop userCrop16;
    private UserCrop userCrop17;
    private UserCrop userCrop18;

    @Id
    @GeneratedValue(generator="identity")
    @GenericGenerator(name="identity", strategy="identity")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id1")
    public UserCrop getUserCrop1() {
        return userCrop1;
    }

    public void setUserCrop1(UserCrop userCrop1) {
        this.userCrop1 = userCrop1;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id2")
    public UserCrop getUserCrop2() {
        return userCrop2;
    }

    public void setUserCrop2(UserCrop userCrop2) {
        this.userCrop2 = userCrop2;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id3")
    public UserCrop getUserCrop3() {
        return userCrop3;
    }

    public void setUserCrop3(UserCrop userCrop3) {
        this.userCrop3 = userCrop3;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id4")
    public UserCrop getUserCrop4() {
        return userCrop4;
    }

    public void setUserCrop4(UserCrop userCrop4) {
        this.userCrop4 = userCrop4;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id5")
    public UserCrop getUserCrop5() {
        return userCrop5;
    }

    public void setUserCrop5(UserCrop userCrop5) {
        this.userCrop5 = userCrop5;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id6")
    public UserCrop getUserCrop6() {
        return userCrop6;
    }

    public void setUserCrop6(UserCrop userCrop6) {
        this.userCrop6 = userCrop6;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id7")
    public UserCrop getUserCrop7() {
        return userCrop7;
    }

    public void setUserCrop7(UserCrop userCrop7) {
        this.userCrop7 = userCrop7;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id8")
    public UserCrop getUserCrop8() {
        return userCrop8;
    }

    public void setUserCrop8(UserCrop userCrop8) {
        this.userCrop8 = userCrop8;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id9")
    public UserCrop getUserCrop9() {
        return userCrop9;
    }

    public void setUserCrop9(UserCrop userCrop9) {
        this.userCrop9 = userCrop9;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id10")
    public UserCrop getUserCrop10() {
        return userCrop10;
    }

    public void setUserCrop10(UserCrop userCrop10) {
        this.userCrop10 = userCrop10;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id11")
    public UserCrop getUserCrop11() {
        return userCrop11;
    }

    public void setUserCrop11(UserCrop userCrop11) {
        this.userCrop11 = userCrop11;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id12")
    public UserCrop getUserCrop12() {
        return userCrop12;
    }

    public void setUserCrop12(UserCrop userCrop12) {
        this.userCrop12 = userCrop12;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id13")
    public UserCrop getUserCrop13() {
        return userCrop13;
    }

    public void setUserCrop13(UserCrop userCrop13) {
        this.userCrop13 = userCrop13;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id14")
    public UserCrop getUserCrop14() {
        return userCrop14;
    }

    public void setUserCrop14(UserCrop userCrop14) {
        this.userCrop14 = userCrop14;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id15")
    public UserCrop getUserCrop15() {
        return userCrop15;
    }

    public void setUserCrop15(UserCrop userCrop15) {
        this.userCrop15 = userCrop15;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id16")
    public UserCrop getUserCrop16() {
        return userCrop16;
    }

    public void setUserCrop16(UserCrop userCrop16) {
        this.userCrop16 = userCrop16;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id17")
    public UserCrop getUserCrop17() {
        return userCrop17;
    }

    public void setUserCrop17(UserCrop userCrop17) {
        this.userCrop17 = userCrop17;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_crop_id18")
    public UserCrop getUserCrop18() {
        return userCrop18;
    }

    public void setUserCrop18(UserCrop userCrop18) {
        this.userCrop18 = userCrop18;
    }

    @Override
    public String toString() {
        return "Land{" +
                "id=" + id +
                ", user=" + user +
                ", userCrop1=" + userCrop1 +
                ", userCrop2=" + userCrop2 +
                ", userCrop3=" + userCrop3 +
                ", userCrop4=" + userCrop4 +
                ", userCrop5=" + userCrop5 +
                ", userCrop6=" + userCrop6 +
                ", userCrop7=" + userCrop7 +
                ", userCrop8=" + userCrop8 +
                ", userCrop9=" + userCrop9 +
                ", userCrop10=" + userCrop10 +
                ", userCrop11=" + userCrop11 +
                ", userCrop12=" + userCrop12 +
                ", userCrop13=" + userCrop13 +
                ", userCrop14=" + userCrop14 +
                ", userCrop15=" + userCrop15 +
                ", userCrop16=" + userCrop16 +
                ", userCrop17=" + userCrop17 +
                ", userCrop18=" + userCrop18 +
                '}';
    }

}
