package org.kkycp.server.services;
import lombok.RequiredArgsConstructor;
import org.kkycp.server.controller.dto.SearchConditionDto;
import org.kkycp.server.controller.dto.SimpleIssueDto;
import org.kkycp.server.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final ProjectService projectservice;

    /**
     * @param projectId Issue가 생성된 project의 주소
     * @param report Issue의 attribution들을 담은 객체
     * @param createdDated Issue 생성일
     */
    public void createIssue(long projectId, Report report, LocalDate createdDated){
        Project project = projectservice.findProject(projectId).orElseThrow(() -> new NoSuchElementException("No such project exists."));
        project.reportIssue(report, createdDated);
    }

    public List<SimpleIssueDto> getSimplifiedIssues(int offset, int limit, SearchConditionDto searchCondition) {
        return null;
    }
}
