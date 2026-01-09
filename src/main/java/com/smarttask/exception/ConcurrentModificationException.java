package com.smarttask.exception;

public final class ConcurrentModificationException extends DomainException {

    public ConcurrentModificationException(String message) {
        super(message);
    }

    public ConcurrentModificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
