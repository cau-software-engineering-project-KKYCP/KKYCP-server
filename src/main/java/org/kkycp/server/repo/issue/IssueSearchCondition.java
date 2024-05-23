package org.kkycp.server.repo.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kkycp.server.domain.Issue;

/**
 * Search conditions for issues.
 * <p>
 * If all condition is null, fetch all issues.
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class IssueSearchCondition {
    private String title;
    private String assigneeName;
    private String reporterName;
    private Issue.Priority priority;
    private Issue.Status status;
    private String type;
}
