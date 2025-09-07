package com.kiran.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.taskmanager.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    
}
