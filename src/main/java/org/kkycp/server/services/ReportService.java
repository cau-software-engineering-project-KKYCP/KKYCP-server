package org.kkycp.server.services;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.Report;
import org.kkycp.server.repo.issue.IssueRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final IssueRepo issueRepo;

    public Issue reportIssue(Project project, Report report) {
        Issue issue = Issue.builder()
                .project(project)
                .reporter(report.getReporter())
                .title(report.getTitle())
                .description(report.getDescription())
                .priority(report.getPriority())
                .reportedDate(LocalDate.now())
                .type(report.getType())
                .build();
        return issueRepo.save(issue);
    }
}
