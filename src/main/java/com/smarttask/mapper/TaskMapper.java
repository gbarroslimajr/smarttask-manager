package com.smarttask.mapper;

import com.smarttask.domain.entity.Task;
import com.smarttask.dto.CreateTaskDTO;
import com.smarttask.dto.TaskResponseDTO;
import com.smarttask.dto.UpdateTaskDTO;

public class TaskMapper {

    public static TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            task.getCreatedAt(),
            task.getUpdatedAt(),
            task.getDueDate(),
            task.getUser() != null ? task.getUser().getId() : null,
            task.getProject() != null ? task.getProject().getId() : null,
            task.getVersion()
        );
    }

    public static Task toEntity(CreateTaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setPriority(dto.priority());
        task.setDueDate(dto.dueDate());
        return task;
    }

    public static void updateEntityFromDTO(Task task, UpdateTaskDTO dto) {
        if (dto.title() != null) {
            task.setTitle(dto.title());
        }
        if (dto.description() != null) {
            task.setDescription(dto.description());
        }
        if (dto.status() != null) {
            task.setStatus(dto.status());
        }
        if (dto.priority() != null) {
            task.setPriority(dto.priority());
        }
        if (dto.dueDate() != null) {
            task.setDueDate(dto.dueDate());
        }
    }
}
