package org.kkycp.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class IllegalIssueStatusTransitionException extends RuntimeException {
    public IllegalIssueStatusTransitionException(String message) {
        super(message);
    }

    public IllegalIssueStatusTransitionException() {
        super("Invalid issue status change.");
    }
}
