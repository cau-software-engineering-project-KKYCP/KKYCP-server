package org.kkycp.server.repo.issue;

import lombok.Data;
import org.kkycp.server.domain.Issue;

@Data
public class IssueSearchCondition {
    private String title;
    private String assigneeName;
    private String reporterName;
    private Issue.Priority priority;
    private Issue.Status status;
    private String type;
}
