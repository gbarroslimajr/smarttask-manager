package com.smarttask.dto;

import com.smarttask.domain.enums.TaskPriority;
import com.smarttask.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para criação de tarefa")
public record CreateTaskDTO(
    @Schema(description = "Título da tarefa", example = "Implementar funcionalidade de login", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Título não pode estar vazio")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    String title,

    @Schema(description = "Descrição detalhada da tarefa", example = "Implementar autenticação JWT com refresh token")
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    String description,

    @Schema(description = "Status da tarefa", example = "PENDING", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Status não pode ser nulo")
    TaskStatus status,

    @Schema(description = "Prioridade da tarefa", example = "HIGH", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Prioridade não pode ser nula")
    TaskPriority priority,

    @Schema(description = "Data de vencimento da tarefa", example = "2024-12-31T23:59:59")
    LocalDateTime dueDate,

    @Schema(description = "ID do usuário responsável pela tarefa", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID do usuário não pode ser nulo")
    UUID userId,

    @Schema(description = "ID do projeto ao qual a tarefa pertence", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID do projeto não pode ser nulo")
    UUID projectId
) {}
