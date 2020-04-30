package com.li.knowledgefarm.entity;

import java.io.Serializable;

/**
 * @ClassName UserAuthority
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 14:11
 */
public class UserAuthority implements Serializable {
    private Integer id;
    private String openId;
    private String type;
    private Integer exist;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getExist() {
        return exist;
    }

    public void setExist(Integer exist) {
        this.exist = exist;
    }

}
