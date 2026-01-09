package com.smarttask.exception;

import java.util.UUID;

public final class TaskNotFoundException extends DomainException {

    public TaskNotFoundException(UUID id) {
        super("Tarefa não encontrada com ID: " + id);
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
