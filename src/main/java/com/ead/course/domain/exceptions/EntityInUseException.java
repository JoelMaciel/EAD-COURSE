package com.ead.course.domain.exceptions;

public class EntityInUseException extends BusinessException {
    public EntityInUseException(String message) {
        super(message);
    }
}