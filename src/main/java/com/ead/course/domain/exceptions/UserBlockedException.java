package com.ead.course.domain.exceptions;

public class UserBlockedException extends BusinessException {
    public UserBlockedException() {
        super("User blocked, unable to register.");
    }
}
