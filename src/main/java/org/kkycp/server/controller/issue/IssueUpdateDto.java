package org.kkycp.server.controller.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @NoArgsConstructor
    public static class Request {
        private String title;
        private String description;
        private String fixer;
        private String assignee;
        private Issue.Status status;
        private Issue.Priority priority;
        private String type;

        @Transactional
        public void dispatchRequest(long issueId, IssueService issueService) {
            if (assignee != null) {
                issueService.assignIssue(issueId, assignee);
            }

            if (status == Issue.Status.FIXED) {
                issueService.markIssueFixed(issueId, fixer);
            } else if(status != null) {
                issueService.changeIssueState(issueId, status);
            }

            if (title != null || description != null || priority != null || type != null) {
                issueService.updateIssueAttribute(issueId, title, description, priority, type);
            }
        }
    }
}
