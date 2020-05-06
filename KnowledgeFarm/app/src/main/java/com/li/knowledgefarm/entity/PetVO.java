package com.li.knowledgefarm.entity;

/**
 * @program: knowledge_farm
 * @description: 宠物
 * @author: 景光赞
 * @create: 2020-04-25 10:26
 **/
public class PetVO {
    private Integer id;
    private String name;
    private String description;
    private Integer life;       //生命值上限
    private Integer intelligence;       //智力值上限
    private Integer physical;         //体力值上限
    private Integer price;
    private String img1;
    private String img2;
    private String img3;
    private Integer own;
    private Integer exist;

    public PetVO() {
        this.exist = 1;
        this.own = 0;
    }

    public PetVO(Pet pet) {
        this.id = pet.getId();
        this.img1 = pet.getImg1();
        this.img2 = pet.getImg2();
        this.img3 = pet.getImg3();
        this.name = pet.getName();
        this.description = pet.getDescription();
        this.life = pet.getLife();
        this.intelligence = pet.getIntelligence();
        this.physical = pet.getPhysical();
        this.price = pet.getPrice();
        this.exist = pet.getExist();
        this.own = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getPhysical() {
        return physical;
    }

    public void setPhysical(Integer physical) {
        this.physical = physical;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public Integer getExist() {
        return exist;
    }

    public void setExist(Integer exist) {
        this.exist = exist;
    }

    public Integer getOwn() {
        return own;
    }

    public void setOwn(Integer own) {
        this.own = own;
    }
}
