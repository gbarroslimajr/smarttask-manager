package com.smarttask.exception;

import java.util.UUID;

public class ProjectNotFoundException extends EntityNotFoundException {

    public ProjectNotFoundException(UUID id) {
        super("Projeto não encontrado com ID: " + id);
    }

    public ProjectNotFoundException(String message) {
        super(message);
    }
}
