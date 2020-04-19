package com.knowledge_farm.task.dao;

import com.knowledge_farm.entity.Task;
import com.knowledge_farm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDao extends JpaRepository<Task, Integer> {
    Task findTaskByUser(User user);
}
