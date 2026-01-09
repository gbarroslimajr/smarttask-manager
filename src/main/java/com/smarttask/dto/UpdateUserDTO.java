package com.smarttask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String name,

    @Email(message = "Email deve ser válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    String email
) {}
