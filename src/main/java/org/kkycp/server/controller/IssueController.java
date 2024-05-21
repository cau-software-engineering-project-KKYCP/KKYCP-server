package org.kkycp.server.controller;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.controller.dto.SearchConditionDto;
import org.kkycp.server.controller.dto.SimpleIssueDto;
import org.kkycp.server.services.IssueService;
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
}
