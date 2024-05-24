package org.kkycp.server.controller.issue;

import lombok.Getter;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.repo.issue.IssueSearchCondition;
import org.springframework.web.bind.annotation.BindParam;

@Getter
public class SearchConditionDto {
    private String assigneeName = null;
    private String reporterName = null;
    private Issue.Priority priority = null;
    private Issue.Status status = null;
    private String title = null;
    private String type = null;

    public SearchConditionDto(@BindParam("assignee") String assigneeName,
                              @BindParam("reporter") String reporterName,
                              Issue.Priority priority,
                              Issue.Status status,
                              String title,
                              String type) {
            this.assigneeName = assigneeName;
        this.reporterName = reporterName;
        this.priority = priority;
        this.status = status;
        this.title = title;
        this.type = type;
    }

    public IssueSearchCondition convert() {
        return new IssueSearchCondition(title, assigneeName, reporterName, priority, status,type);
    }
}
