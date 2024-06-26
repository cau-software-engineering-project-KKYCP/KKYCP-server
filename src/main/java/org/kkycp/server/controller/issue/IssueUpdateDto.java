package org.kkycp.server.controller.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.services.IssueService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see Issue
 */
public class IssueUpdateDto {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class Request {
        private String title;
        private String description;
        private String fixer;
        private String assignee;
        private Issue.Status status;
        private Issue.Priority priority;
        private String type;

        @Transactional
        public void dispatchRequest(long projectId, long issueId, IssueService issueService) {
            if (title != null || description != null || priority != null || type != null) {
                issueService.updateIssueAttribute(projectId, issueId, title, description, priority, type);
            }

            if (status == Issue.Status.ASSIGNED) {
                issueService.assignIssue(projectId,issueId, assignee);
            } else if (status == Issue.Status.FIXED) {
                issueService.markIssueFixed(projectId, issueId, fixer);
            } else if(status != null) {
                issueService.changeIssueState(projectId, issueId, status);
            }
        }
    }
}
