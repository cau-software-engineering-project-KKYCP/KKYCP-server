package org.kkycp.server.controller.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kkycp.server.domain.Issue;

import java.time.LocalDate;

public class IssueDto {
    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class Request {
        private String title;
        private String description;
        private Issue.Priority priority;
        private String type;
    }

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
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

        public static Response from(Issue issue) {
            Response response = new Response();
            response.setId(issue.getId());
            response.setTitle(issue.getTitle());
            response.setDescription(issue.getDescription());
            response.setReporter(issue.getReporter().getUsername());
            response.setReportedDate(issue.getReportedDate());
            if (issue.getFixer() != null) {
                response.setFixer(issue.getFixer().getUsername());
            }
            if (issue.getAssignee() != null) {
                response.setAssignee(issue.getAssignee().getUsername());
            }
            response.setPriority(issue.getPriority());
            response.setStatus(issue.getStatus());
            response.setType(issue.getType());
            return response;
        }
    }

}
