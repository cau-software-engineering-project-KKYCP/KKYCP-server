package org.kkycp.server.controller.project;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ProjectDto {
    @Data
    @AllArgsConstructor
    public static class Response {
        private long id;
        private String projectName;
    }
}
