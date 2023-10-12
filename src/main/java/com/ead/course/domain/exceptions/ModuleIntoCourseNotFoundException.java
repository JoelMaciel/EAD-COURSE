package com.ead.course.domain.exceptions;

import java.util.UUID;

public class ModuleIntoCourseNotFoundException extends EntityNotExistsException{
    public ModuleIntoCourseNotFoundException(UUID courseId, UUID moduleId) {
        super(String.format(
                "There is no course registered with UUID %s to module whit UUD %s", courseId, moduleId));
    }
}
