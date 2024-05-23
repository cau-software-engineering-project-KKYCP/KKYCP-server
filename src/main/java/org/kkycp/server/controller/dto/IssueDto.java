package org.kkycp.server.controller.dto;

import lombok.Data;
import org.kkycp.server.domain.Issue;

import java.time.LocalDate;

public class IssueDto {
    @Data
    public static class Request {
        private String title;
        private String description;
        private Issue.Priority priority;
        private Issue.Status status;
        private String type;
    }

    @Data
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private String reporter;
        private LocalDate reportedDate;
        private String  fixer;
        private String  assignee;
        private Issue.Priority priority;
        private Issue.Status status;
        private String type;
    }

}
