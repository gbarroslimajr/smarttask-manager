package com.smarttask.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta com dados do usuário")
public record UserResponseDTO(
    @Schema(description = "ID único do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,
    @Schema(description = "Nome completo do usuário", example = "João Silva")
    String name,
    @Schema(description = "Email do usuário", example = "joao.silva@example.com")
    String email,
    @Schema(description = "Data de criação do usuário", example = "2024-01-01T10:00:00")
    LocalDateTime createdAt
) {}
