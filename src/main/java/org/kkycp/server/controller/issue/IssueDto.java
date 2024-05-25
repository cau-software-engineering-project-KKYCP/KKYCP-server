package org.kkycp.server.controller.issue;

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

        public static Response from(Issue issue) {
            Response response = new Response();
            response.setId(issue.getId());
            response.setTitle(issue.getTitle());
            response.setDescription(issue.getDescription());
            response.setReporter(issue.getReporter().getUsername());
            response.setReportedDate(issue.getReportedDate());
            response.setFixer(issue.getFixer().getUsername());
            response.setAssignee(issue.getAssignee().getUsername());
            response.setPriority(issue.getPriority());
            response.setStatus(issue.getStatus());
            response.setType(issue.getType());
            return response;
        }
    }

}
