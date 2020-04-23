package com.li.knowledgefarm.entity;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Member;
import java.util.Objects;

public class TaskItem implements Comparable<TaskItem> {
    private String type;
    private String content;
    private String reward;
    private int status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public int compareTo(@NotNull TaskItem taskItem) {
        if (this.getStatus()==0) {
            if (taskItem.getStatus()==1) {
                return 1;
            }else if(taskItem.getStatus()==2){
                return -1;
            }else
                return this.getType().equals("signIn")?-1:0;
        }else if (this.getStatus()==1){
            if (taskItem.getStatus()==1){
                return this.getType().equals("signIn")?-1:0;
            }
            return -1;
        }else{
            if (taskItem.getStatus()==2)
                return this.getType().equals("signIn")?-1:0;
            return 1;
        }
    }
}