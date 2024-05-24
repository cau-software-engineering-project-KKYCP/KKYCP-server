package org.kkycp.server.services;
import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.*;
import org.kkycp.server.repo.issue.IssueRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final ProjectService projectservice;
    private final IssueRepo issueRepo;
    /*
    Report report; // Issue의 attribution들을 담은 객체
    long projectId; // Issue가 생성된 project의 주소
    */

    public void createIssue(long projectId, Report report, LocalDate createdDated){
        Project project = projectservice.findProject(projectId).orElseThrow(() -> new NoSuchElementException("No such project exists."));
        project.reportIssue(report, createdDated);
    }

    //권한이 있는 유저가 issue의 상태 업데이트
    public void updateIssueStatus(Long issueId, Long userId, Issue.Status status){
        Issue issue = issueRepo.findById(issueId)//findById 구현 필요
                              .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));
        issue.setStatus(status);
    }

    //resolved된 issue를 verifier가 close
    public void closeResolvedIssue(Long issueId){
        Issue issue = issueRepo.findById(issueId)//findById 구현 필요
                                .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));
        if(issue.getStatus()==Issue.Status.RESOLVED){//RESOLVED일 경우 close로 변경
            issue.closeIssue();
            return;
        }
        else    
            return;
    }

    //issue가 해결되었는지 검토
    public void verifyIssueResolved(Long issueId, Long testerId, boolean resolved) {
        try {
            // Get the issue
            Issue issue = issueRepo.findById(issueId)
                                    .orElseThrow(() -> new IllegalArgumentException("Invalid issue ID"));
            // tester가 reporter인지 확인
            if (!issue.getReporter().getId().equals(testerId)) {
                return;
            }
            //status가 FIXED인지 확인
            if (issue.getStatus() != Issue.Status.FIXED) {
                return;
            }
            // issue가 해결된 상태
            if (resolved) {
                updateIssueStatus(issueId, testerId, Issue.Status.RESOLVED);//resolved로 변경
                return;
            } else {//issue가 해결되지 않음
                //코멘트 추가
                updateIssueStatus(issueId, testerId, Issue.Status.ASSIGNED);//assigned로 변경
                return;
            }
        } catch (IllegalArgumentException e) {
            return;
        }
    }
}

