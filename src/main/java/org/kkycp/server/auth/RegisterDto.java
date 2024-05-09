package org.kkycp.server.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class RegisterDto {
    @Getter
    @AllArgsConstructor
    public static class Request {
        private String username;
        private String password;
    }
}
