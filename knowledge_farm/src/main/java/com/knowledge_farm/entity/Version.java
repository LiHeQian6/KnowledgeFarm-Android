package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName Version
 * @Description
 * @Author 张帅华
 * @Date 2020-05-30 21:09
 */
@Entity
@Table(name = "version")
public class Version {
    private Integer id;
    private String description;
    private String versionCode;
    private String versionName;
    private String packageName;
    private String fileName;
    private Date uploadTime;

    @Id
    @GeneratedValue(generator="identity")
    @GenericGenerator(name="identity", strategy="identity")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "version_code")
    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    @Column(name = "version_name")
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Column(name = "package_name")
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "upload_time")
    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

}
