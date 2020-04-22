package com.knowledge_farm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName UserCrop
 * @Description
 * @Author 张帅华
 * @Date 2020-04-06 14:15
 */
@Entity
@Table(name = "user_crop")
public class UserCrop {
    private Integer id;
    private Crop crop;
    private Integer waterLimit;
    private Integer fertilizerLimit;
    private Integer progress;
    private Integer status;

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
    @JoinColumn(name = "crop_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    @Column(name = "water_limit", insertable = false, columnDefinition = "int default 15")
    public Integer getWaterLimit() {
        return waterLimit;
    }

    public void setWaterLimit(Integer waterLimit) {
        this.waterLimit = waterLimit;
    }

    @Column(name = "fertilizer_limit", insertable = false, columnDefinition = "int default 15")
    public Integer getFertilizerLimit() {
        return fertilizerLimit;
    }

    public void setFertilizerLimit(Integer fertilizerLimit) {
        this.fertilizerLimit = fertilizerLimit;
    }

    @Column(insertable = false, columnDefinition = "int default 0")
    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @Column(insertable = false, columnDefinition = "int default 1")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
