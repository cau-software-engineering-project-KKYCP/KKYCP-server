package org.kkycp.server.controller.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

public class ProjectCreateDto {
    @Data
    public static class Request {
        @NotBlank
        private String projectName;
    }

    @Data
    @AllArgsConstructor
    public static class Response {
        private long projectId;
    }
}
