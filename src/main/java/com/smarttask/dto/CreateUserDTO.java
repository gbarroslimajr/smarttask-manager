package com.smarttask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criação de usuário")
public record CreateUserDTO(
    @Schema(description = "Nome completo do usuário", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Nome não pode estar vazio")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String name,

    @Schema(description = "Email do usuário (deve ser único)", example = "joao.silva@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email não pode estar vazio")
    @Email(message = "Email deve ser válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    String email
) {}
