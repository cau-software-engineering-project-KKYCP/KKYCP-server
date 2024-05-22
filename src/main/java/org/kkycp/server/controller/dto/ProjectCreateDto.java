package org.kkycp.server.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
