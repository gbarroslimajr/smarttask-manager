package com.smarttask.dto;

import jakarta.validation.constraints.Size;

public record UpdateProjectDTO(
    @Size(max = 200, message = "Nome do projeto deve ter no máximo 200 caracteres")
    String name,

    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    String description
) {}
