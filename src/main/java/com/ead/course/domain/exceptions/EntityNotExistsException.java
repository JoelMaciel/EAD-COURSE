package com.ead.course.domain.exceptions;

public class EntityNotExistsException extends BusinessException{
    public EntityNotExistsException(String message) {
        super(message);
    }

}
