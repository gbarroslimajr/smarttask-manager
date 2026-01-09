package com.smarttask.repository;

import com.smarttask.domain.entity.Task;
import com.smarttask.domain.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    Page<Task> findByUserId(UUID userId, Pageable pageable);

    Page<Task> findByProjectId(UUID projectId, Pageable pageable);

    Page<Task> findByUserIdAndStatus(UUID userId, TaskStatus status, Pageable pageable);
}
