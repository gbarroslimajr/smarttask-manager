package com.smarttask.exception;

/**
 * Classe base selada para todas as exceptions de domínio.
 * Apenas as classes explicitamente permitidas podem estender esta classe.
 */
public sealed class DomainException extends RuntimeException
    permits UserNotFoundException, TaskNotFoundException, ProjectNotFoundException,
            EmailAlreadyExistsException, ConcurrentModificationException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
