package com.smarttask.exception;

import java.util.UUID;

public final class UserNotFoundException extends DomainException {

    public UserNotFoundException(UUID id) {
        super("Usuário não encontrado com ID: " + id);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
