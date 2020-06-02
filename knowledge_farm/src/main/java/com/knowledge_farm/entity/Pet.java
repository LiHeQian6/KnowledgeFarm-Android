package com.knowledge_farm.entity;

import javax.persistence.*;

/**
 * @program: knowledge_farm
 * @description: 宠物
 * @author: 景光赞
 * @create: 2020-04-25 10:26
 **/
@Entity
@Table(name = "pet")
public class Pet {
    private Integer id;
    private String name;
    private String description;
    private Integer life;       //生命值上限
    private Integer intelligence;       //智力值上限
    private Integer physical;         //体力值上限
    private Integer price;
    private PetFunction petFunction;
    private String img1;
    private String img2;
    private String img3;
    private String gif1;
    private String gif2;
    private String gif3;
    private Integer exist;

    public Pet() {
        this.exist = 1;
    }

    public Pet(String name, String description, Integer life, Integer intelligence, Integer physical,Integer price) {
        this.name = name;
        this.description = description;
        this.life = life;
        this.intelligence = intelligence;
        this.physical = physical;
        this.price = price;
        this.exist = 1;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public String getGif1() {
        return gif1;
    }

    public void setGif1(String gif1) {
        this.gif1 = gif1;
    }

    public String getGif2() {
        return gif2;
    }

    public void setGif2(String gif2) {
        this.gif2 = gif2;
    }

    public String getGif3() {
        return gif3;
    }

    public void setGif3(String gif3) {
        this.gif3 = gif3;
    }

    @OneToOne(mappedBy = "pet",cascade = CascadeType.ALL)
    @org.hibernate.annotations.ForeignKey(name = "none")
    public PetFunction getPetFunction() {
        return petFunction;
    }

    public void setPetFunction(PetFunction petFunction) {
        this.petFunction = petFunction;
    }

    @Column(columnDefinition = "1")
    public Integer getExist() {
        return exist;
    }

    public void setExist(Integer exist) {
        this.exist = exist;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", life=" + life +
                ", intelligence=" + intelligence +
                ", physical=" + physical +
                ", price=" + price +
                ", petFunction=" + petFunction +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                ", img3='" + img3 + '\'' +
                ", exist=" + exist +
                '}';
    }

}
