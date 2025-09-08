package com.kiran.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiran.taskmanager.model.Task;

public interface TaskRepository extends JpaRepository<Task,Long> {
    
}
