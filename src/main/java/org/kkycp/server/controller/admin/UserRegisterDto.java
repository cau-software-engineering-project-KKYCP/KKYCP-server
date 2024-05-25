package org.kkycp.server.controller.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRegisterDto {
    @Data
    @NoArgsConstructor
    public static class Request {
        private String username;
    }
}
