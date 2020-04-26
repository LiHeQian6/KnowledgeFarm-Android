package com.knowledge_farm.task.dao;

import com.knowledge_farm.entity.Task;
import com.knowledge_farm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskDao extends JpaRepository<Task, Integer> {
    Task findTaskByUser(User user);

    @Query("update Task t set t.signIn = 0,t.water = 0,t.fertilize = 0,t.crop = 0,t.harvest = 0,t.helpWater = 0,t.helpFertilize = 0")
    @Modifying
    int updateTaskEveryDay();
}
