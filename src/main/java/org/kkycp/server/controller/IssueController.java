package org.kkycp.server.controller;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.Report;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.repo.issue.IssueRepo;
import org.kkycp.server.services.IssueService;
import org.kkycp.server.services.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class IssueController {
    private final ReportService reportService;
    private final ProjectRepo projectRepo;
    private final IssueRepo issueRepo;
    private final IssueService issueService;

    /*
     * return (...HTTPStatus...)부분들은 맞는지 잘 모르겠어서 확인 부탁드립니다 ㅠ-ㅠ 
     */
    @PostMapping    //project에 Issue를 report
    public ResponseEntity<Issue> reportIssue(Report report, Long projectId) {
        Project project = projectRepo.findById(projectId)   //projectId로 project 찾음
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));//예외
        Issue issue = reportService.reportIssue(project, report);//project에 issue 등록
        return new ResponseEntity<>(issue, HttpStatus.CREATED);
    }

    @PostMapping    //issue를 resolve로 변경
    public ResponseEntity<String> resolveIssue(Long issueId) {
        Issue issue = issueRepo.findById(issueId)   //issueId로 issue 찾음
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue ID"));//예외

        // 이슈를 해결했다고 시스템에 알림
        issue.resolveIssue();
        issue.setFixer(issue.getAssignee());
        return new ResponseEntity<>("Issue resolved.", HttpStatus.OK);
    }
    
    @PostMapping    //resolved된 issue를 close
    public ResponseEntity<String> closeIssue(Long issueId, Long verifierId) {
        try {
            issueService.closeResolvedIssue(issueId);
            return new ResponseEntity<>("Issue status closed.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
    
    @PostMapping    //issue의 상태를 업데이트
    public ResponseEntity<String> updateIssueStatus(Long issueId, Long userId, Issue.Status status) {
        try {
            issueService.updateIssueStatus(issueId, userId, status);
            return new ResponseEntity<>("Issue status updated.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

}