package com.smarttask.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta com dados do projeto")
public record ProjectResponseDTO(
    @Schema(description = "ID único do projeto", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,
    @Schema(description = "Nome do projeto", example = "Sistema de Gestão de Tarefas")
    String name,
    @Schema(description = "Descrição do projeto", example = "Sistema completo para gerenciamento de tarefas")
    String description,
    @Schema(description = "Data de criação do projeto", example = "2024-01-01T10:00:00")
    LocalDateTime createdAt,
    @Schema(description = "Data da última atualização", example = "2024-01-02T15:30:00")
    LocalDateTime updatedAt,
    @Schema(description = "ID do usuário criador", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID userId,
    @Schema(description = "Versão para optimistic locking", example = "1")
    Long version
) {}
