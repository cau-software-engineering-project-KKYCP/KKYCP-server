package org.kkycp.server.controller.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        String username;
    }
}