package org.kkycp.server.services;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.controller.issue.SimpleIssueDto;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.Report;
import org.kkycp.server.domain.User;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.repo.UserRepo;
import org.kkycp.server.repo.issue.IssueRepo;
import org.kkycp.server.repo.issue.IssueSearchCondition;
import org.kkycp.server.repo.issue.IssueStatistics;
import org.kkycp.server.repo.issue.TimeUnit;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional
@RequiredArgsConstructor
public class IssueService {
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;
    private final IssueRepo issueRepo;

    /**
     * @param projectId Issue가 생성된 project의 주소
     * @param report Issue의 attribution들을 담은 객체
     */
    @PreAuthorize("@authz.hasPrivilege(#root, #projectId, T(org.kkycp.server.domain.authorization.Privilege).REPORTER)")
    public void createIssue(long projectId, String reporterName, Report report,
                            LocalDate createdDated) {
        User reporter = userRepo.findByUsername(reporterName).get();
        report.setReporter(reporter);
        Project project = projectRepo.findById(projectId).get();
        project.reportIssue(report, createdDated);
    }

    public Issue getIssue(long issueid) {
        return issueRepo.findByIdFetchAll(issueid).get();
    }

    public List<SimpleIssueDto> getSimplifiedIssues(long projectId,
                                                    IssueSearchCondition searchCondition,
                                                    int offset, int limit) {
        List<Issue> foundIssues = issueRepo.search(projectId, searchCondition, offset, limit);
        return foundIssues.stream()
                .map(i -> new SimpleIssueDto(i.getId(), i.getTitle(), i.getReporter().getUsername(), i.getReportedDate(),
                        i.getPriority(), i.getStatus(), i.getType()))
                .toList();
    }


    @PreAuthorize("@authz.canChangeIssueStatus(#root, #projectId, #desiredStatus)")
    public void changeIssueState(long projectId, long issueId, Issue.Status desiredStatus) {
        Issue issue = issueRepo.findById(issueId).get();
        issue.changeStatus(desiredStatus);
    }

    @PreAuthorize("@authz.hasPrivilege(#root, #projectId, T(org.kkycp.server.domain.authorization.Privilege).TRIAGER)")
    public void assignIssue(long projectId, long issueId, String assigneeName) {
        User assignee = userRepo.findByUsername(assigneeName).get();
        Issue issue = issueRepo.findById(issueId).get();
        issue.assignIssue(assignee);
    }

    @PreAuthorize("@authz.isAssignedToPrincipal(#root, #issueId)")
    public void markIssueFixed(long projectId, long issueId, String fixerName) {
        User fixer = userRepo.findByUsername(fixerName).get();
        Issue issue = issueRepo.findById(issueId).get();
        issue.fixIssue(fixer);
    }

    public List<IssueStatistics.Time> getStatisticsByTime(TimeUnit timeUnit) {
        return issueRepo.countIssueGroupByTime(timeUnit);
    }

    @PreAuthorize("@authz.isAssignedToPrincipal(#root, #issueId) " +
            "|| @authz.hasPrivilege(#root, #projectId, T(org.kkycp.server.domain.authorization.Privilege).TRIAGER) " +
            "|| @authz.hasPrivilege(#root, #projectId, T(org.kkycp.server.domain.authorization.Privilege).VERIFIER)")
    public void updateIssueAttribute(long projectId, long issueId,
                                     String title, String description,
                                     Issue.Priority priority, String type) {
        Issue issue = issueRepo.findById(issueId).get();
        if (hasText(title)) {
            issue.setTitle(title);
        }
        if (hasText(description)) {
            issue.setDescription(description);
        }
        if (priority != null) {
            issue.setPriority(priority);
        }
        if (hasText(type)) {
            issue.setType(type);
        }
    }
}
