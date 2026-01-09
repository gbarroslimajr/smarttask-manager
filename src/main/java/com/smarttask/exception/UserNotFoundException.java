package com.smarttask.exception;

import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(UUID id) {
        super("Usuário não encontrado com ID: " + id);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
