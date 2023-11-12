package com.ead.course.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends EntityNotExistsException{
    public UserNotFoundException(UUID userInstructor) {
        super(String.format("There is no user registered with UUID %s", userInstructor));
    }
}
