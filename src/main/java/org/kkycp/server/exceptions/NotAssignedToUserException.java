package org.kkycp.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class NotAssignedToUserException extends RuntimeException {
    public NotAssignedToUserException() {
        super("The issue is not assigned to the user.");
    }
}
