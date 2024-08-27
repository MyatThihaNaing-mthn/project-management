package com.th.pm.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.th.pm.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    
}
