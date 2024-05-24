package org.kkycp.server.services;
import lombok.RequiredArgsConstructor;
import org.kkycp.server.controller.issue.SearchConditionDto;
import org.kkycp.server.controller.issue.SimpleIssueDto;
import org.kkycp.server.domain.*;
import org.kkycp.server.repo.issue.IssueRepo;
import org.kkycp.server.repo.issue.IssueSearchCondition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class IssueService {
    private final ProjectService projectservice;
    private final IssueRepo issueRepo;

    /*
    Report report; // Issue의 attribution들을 담은 객체
    long projectId; // Issue가 생성된 project의 주소
    */

    public void createIssue(long projectId, Report report, LocalDate createdDated) {
        Project project = projectservice.findProject(projectId).orElseThrow(() -> new NoSuchElementException("No such project exists."));
        project.reportIssue(report, createdDated);
    }

    public Issue findIssue(long issueid){
        return issueRepo.findById(issueid).get();
    }


    public List<SimpleIssueDto> getSimplifiedIssues(long projectId, IssueSearchCondition searchCondition,
                                                    int offset, int limit) {
        List<Issue> foundIssues = issueRepo.search(projectId, searchCondition, offset, limit);
        return foundIssues.stream()
                .map(i -> new SimpleIssueDto(i.getId(), i.getTitle(), i.getReportedDate(), i.getPriority(), i.getStatus(), i.getType()))
                .toList();
    }
}
