package com.ead.course.domain.exceptions;

import java.util.UUID;

public class LessonIntoModuleNotFoundException extends EntityNotExistsException {
    public LessonIntoModuleNotFoundException(UUID moduleId, UUID lessonId) {
        super(String.format(
                "There is no module registered with UUID %s to lesson whit UUD %s", moduleId, lessonId));
    }
}
