package com.li.knowledgefarm.entity;

import java.io.Serializable;
import java.util.Random;


public class UserPkPet implements Comparable<UserPkPet>, Serializable {
    private Integer id;
    private User user;
    private Pet pet;
    private Integer ifUsing;            //是否正在使用
    private Integer growPeriod;
    private Integer life;       //生命值
    private Integer intelligence;       //智力值
    private Integer physical;         //体力值
    private Integer nowLife;
    private boolean right;  //是否答对当前题目
    private long useTime;   //答对题目的用时 单位ms

    public UserPkPet() {
    }

    public UserPkPet(UserPetHouse pet) {
        this.user=pet.getUser();
        this.id=pet.getId();
        this.pet=pet.getPet();
        this.ifUsing=pet.getIfUsing();
        this.growPeriod=pet.getGrowPeriod();
        this.life=pet.getLife();
        this.intelligence=pet.getIntelligence();
        this.physical=pet.getPhysical();
        this.nowLife=pet.getLife();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Integer getIfUsing() {
        return ifUsing;
    }

    public void setIfUsing(Integer ifUsing) {
        this.ifUsing = ifUsing;
    }

    public Integer getGrowPeriod() {
        return growPeriod;
    }

    public void setGrowPeriod(Integer growPeriod) {
        this.growPeriod = growPeriod;
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

    public Integer getNowLife() {
        return nowLife;
    }

    public void setNowLife(Integer nowLife) {
        this.nowLife = nowLife;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    @Override
    public int compareTo(UserPkPet userPetHouse) {
        if (ifUsing==1)
            return -1;
        else if(userPetHouse.getIfUsing()==1)
            return 1;
        if (id>userPetHouse.getId())
            return 1;
        else if (id<userPetHouse.getId())
            return -1;
        return 0;
    }

    /**
     * @Author li
     * @param
     * @return void
     * @Description 机器人自动答题
     * @Date 10:11 2020/5/18
     **/
    public void autoAnswer(){
        double rightRate=0;
        double r = new Random().nextDouble();
        switch (growPeriod){
            case 0:
                rightRate=0.65;
                break;
            case 1:
                rightRate=0.80;
                break;
            case 2:
                rightRate=0.95;
                break;
        }
        if (r<=rightRate){
            right=true;
        }
        double i = (100000 - intelligence) / 100000.0;
        useTime= (long) (new Random().nextDouble() * 5000+ i*10000);
    }

    /**
     * @Author li
     * @param pet
     * @return int 正代表赢了,值代表我打了多少伤害，负代表输了,值代表我受到多少伤害 0代表平局
     * @Description 宠物进行一轮pk
     * @Date 10:11 2020/5/18
     **/
    public int pk(UserPkPet pet){
        pet.autoAnswer();
        int nowLife = pet.getNowLife() - intelligence;
        if (right&&!pet.isRight()){
            if (nowLife<0){
                nowLife=0;
            }
            pet.setNowLife(nowLife); 
            return intelligence;
        }else if(!right&&pet.isRight()){
            this.nowLife = this.nowLife -pet.getIntelligence();
            if (this.nowLife <0)
                this.nowLife =0;
            return -pet.getIntelligence();
        }else if(right&&pet.isRight()){
            if (useTime<pet.getUseTime()){
                if (nowLife<0){
                    nowLife=0;
                }
                pet.setNowLife(nowLife);
                return intelligence;
            }else {
                this.nowLife = this.nowLife -pet.getIntelligence();
                if (this.nowLife <0)
                    this.nowLife =0;
                return -pet.getIntelligence();
            }
        }
        return 0;
    }

}
