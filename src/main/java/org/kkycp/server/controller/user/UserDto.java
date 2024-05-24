package org.kkycp.server.controller.user;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

public class UserDto {
    @Data
    public class Response {
        String username;
    }
}