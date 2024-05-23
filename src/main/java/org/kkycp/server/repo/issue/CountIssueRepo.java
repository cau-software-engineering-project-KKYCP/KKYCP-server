package org.kkycp.server.repo.issue;

import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Project;

import java.time.LocalDate;
import java.util.List;

public interface CountIssueRepo {
    List<Issue> countIssuebyDate(Project project, LocalDate from, LocalDate to);

    List<Issue> countIssuebyStatus(Project project, Issue.Status status);

    List<Issue> countIssuebyPriority(Project project, Issue.Priority priority);

}
