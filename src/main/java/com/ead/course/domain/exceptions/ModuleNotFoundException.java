package com.ead.course.domain.exceptions;

import java.util.UUID;

public class ModuleNotFoundException extends EntityNotExistsException{
    public ModuleNotFoundException(UUID moduleId) {
        super(String.format("There is no module registered with UUID %s", moduleId));
    }
}
