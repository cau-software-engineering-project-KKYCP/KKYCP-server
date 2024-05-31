package org.kkycp.server.controller.issue;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.repo.issue.IssueSearchCondition;

@Data
@NoArgsConstructor
public class SearchConditionDto {
    private String assignee = null;
    private String reporter = null;
    private Issue.Priority priority = null;
    private Issue.Status status = null;
    private String title = null;
    private String type = null;

    public IssueSearchCondition convert() {
        return new IssueSearchCondition(title, assignee, reporter, priority, status,type);
    }
}
