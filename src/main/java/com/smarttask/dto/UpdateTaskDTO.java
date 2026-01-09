package com.smarttask.dto;

import com.smarttask.domain.enums.TaskPriority;
import com.smarttask.domain.enums.TaskStatus;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateTaskDTO(
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    String title,

    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    String description,

    TaskStatus status,

    TaskPriority priority,

    LocalDateTime dueDate
) {}
