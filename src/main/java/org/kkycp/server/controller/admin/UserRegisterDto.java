package org.kkycp.server.controller.admin;

import lombok.Data;

public class UserRegisterDto {
    @Data
    public static class Request {
        private String username;
    }
}
