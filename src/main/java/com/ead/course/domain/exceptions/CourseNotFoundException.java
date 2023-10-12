package com.ead.course.domain.exceptions;

import java.util.UUID;

public class CourseNotFoundException extends EntityNotExistsException{
    public CourseNotFoundException(UUID courseId) {
        super(String.format("There is no course registered with UUID %s", courseId));
    }
}
