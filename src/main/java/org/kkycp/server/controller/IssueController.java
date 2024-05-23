package org.kkycp.server.controller;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.controller.dto.IssueDto;
import org.kkycp.server.controller.dto.SearchConditionDto;
import org.kkycp.server.controller.dto.SimpleIssueDto;
import org.kkycp.server.services.IssueService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @GetMapping("/project/{projectId}/issues")
    List<SimpleIssueDto> getSimplifiedIssues(@PathVariable("projectId") String projectId,
                                             @RequestParam(defaultValue = "0") int offset,
                                             @RequestParam(defaultValue = "50") int limit,
                                             @ModelAttribute SearchConditionDto searchCondition) {
        return issueService.getSimplifiedIssues(offset, limit, searchCondition);
    }

    @PostMapping("/project/{projectId}/issues")
    @ResponseStatus(HttpStatus.CREATED)
    public void createIssue(@PathVariable("projectId") String projectId,
                            @RequestBody IssueDto.Request request) {
        //TODO
    }

    @GetMapping("/project/{projectId}/issues/{issueId}")
    public IssueDto.Response getIssue(@PathVariable("projectId") String projectId,
                                      @PathVariable("issueId") String issueId) {
        //TODO
        return null;
    }

    @PatchMapping("/project/{projectId}/issues/{issueId}")
    public void updateIssue(@PathVariable("projectId") String projectId,
                            @PathVariable("issueId") String issueId) {
        //TODO
    }

    @GetMapping("/project/{projectId}/statistics")
    public void getStatistics(@PathVariable("projectId") String projectId) {
        //TODO
    }
}
