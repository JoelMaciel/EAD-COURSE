package com.ead.course.domain.exceptions;

import java.util.UUID;

public class ExistsCourseAndUserException extends EntityInUseException {
    public ExistsCourseAndUserException(UUID courseId) {
        super(String.format("There is already a registration for this user for course %s : ", courseId));
    }
}
