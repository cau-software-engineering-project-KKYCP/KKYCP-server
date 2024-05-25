package org.kkycp.server.controller.issue;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.auth.jpa.AuthUserDetails;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Report;
import org.kkycp.server.repo.issue.IssueStatistics;
import org.kkycp.server.repo.issue.TimeUnit;
import org.kkycp.server.services.IssueService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @GetMapping("/project/{projectId}/issues")
    List<SimpleIssueDto> getSimplifiedIssues(@PathVariable("projectId") long projectId,
                                             @RequestParam(defaultValue = "0") int offset,
                                             @RequestParam(defaultValue = "50") int limit,
                                             @ModelAttribute SearchConditionDto searchCondition) {
        return issueService.getSimplifiedIssues(
                projectId, searchCondition.convert(), offset, limit);
    }

    @PostMapping("/project/{projectId}/issues")
    @ResponseStatus(HttpStatus.CREATED)
    public void createIssue(@PathVariable("projectId") long projectId,
                            @RequestBody IssueDto.Request request,
                            @AuthenticationPrincipal AuthUserDetails authUser) {
        String reporter = authUser.getUsername();
        Report report = Report.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .type(request.getType()).build();
        issueService.createIssue(projectId, reporter, report, LocalDate.now());
    }

    @GetMapping("/project/{projectId}/issues/{issueId}")
    public IssueDto.Response getIssue(@PathVariable("projectId") long projectId,
                                      @PathVariable("issueId") long issueId) {
        Issue issue = issueService.getIssue(issueId);
        return IssueDto.Response.from(issue);
    }

    /**
     * 이 컨트롤러는 http body의 op attribute에 기반해, 여러가지의 오퍼레이션을 처리한다.
     * <ol>
     *     <li>Assignee: 자신에게 할당된 이슈 해결</li>
     *     <li>Tester: 이슈 해결 확인</li>
     *     <li>Verifier: 이슈를 close 하거나 reopen 함</li>
     *     <li>Triager: 이슈를 유저에게 할당</li>
     *     <li>Assignee, Triager, Verifier: 이슈의 title, description, priority, status, type의 변경</li>
     * </ol>
     */
    @PatchMapping("/project/{projectId}/issues/{issueId}")
    public void updateIssue(@PathVariable("projectId") long projectId,
                            @PathVariable("issueId") long issueId,
                            @RequestBody IssueUpdateDto.Request updateRequest) {
        updateRequest.dispatchRequest(issueId, issueService);
    }

    @GetMapping("/project/{projectId}/statistics/time")
    public List<IssueStatistics.Time> getStatisticsByTime(@PathVariable("projectId") long projectId,
                                                          @RequestParam(name = "time_unit", defaultValue = "DAY")
                                                          TimeUnit timeUnit) {
        return issueService.getStatisticsByTime(timeUnit);
    }
}
