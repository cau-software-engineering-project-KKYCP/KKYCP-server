package org.kkycp.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UserNotParticipatingException extends RuntimeException {
    public UserNotParticipatingException() {
        super("User is not participating the project");
    }

    public UserNotParticipatingException(String message) {
        super(message);
    }
}
