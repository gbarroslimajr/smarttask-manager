package com.smarttask.dto;

import com.smarttask.domain.enums.TaskPriority;
import com.smarttask.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta com dados da tarefa")
public record TaskResponseDTO(
    @Schema(description = "ID único da tarefa", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,
    @Schema(description = "Título da tarefa", example = "Implementar funcionalidade de login")
    String title,
    @Schema(description = "Descrição detalhada da tarefa", example = "Implementar autenticação JWT")
    String description,
    @Schema(description = "Status da tarefa", example = "IN_PROGRESS")
    TaskStatus status,
    @Schema(description = "Prioridade da tarefa", example = "HIGH")
    TaskPriority priority,
    @Schema(description = "Data de criação da tarefa", example = "2024-01-01T10:00:00")
    LocalDateTime createdAt,
    @Schema(description = "Data da última atualização", example = "2024-01-02T15:30:00")
    LocalDateTime updatedAt,
    @Schema(description = "Data de vencimento da tarefa", example = "2024-12-31T23:59:59")
    LocalDateTime dueDate,
    @Schema(description = "ID do usuário responsável", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID userId,
    @Schema(description = "ID do projeto", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID projectId,
    @Schema(description = "Versão para optimistic locking", example = "1")
    Long version
) {}
