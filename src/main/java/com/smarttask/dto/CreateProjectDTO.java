package com.smarttask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "DTO para criação de projeto")
public record CreateProjectDTO(
    @Schema(description = "Nome do projeto", example = "Sistema de Gestão de Tarefas", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Nome do projeto não pode estar vazio")
    @Size(max = 200, message = "Nome do projeto deve ter no máximo 200 caracteres")
    String name,

    @Schema(description = "Descrição do projeto", example = "Sistema completo para gerenciamento de tarefas e projetos")
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    String description,

    @Schema(description = "ID do usuário criador do projeto", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID do usuário não pode ser nulo")
    UUID userId
) {}
