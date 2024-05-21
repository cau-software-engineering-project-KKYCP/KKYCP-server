package org.kkycp.server.services;

import org.kkycp.server.controller.dto.SearchConditionDto;
import org.kkycp.server.controller.dto.SimpleIssueDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {
    public List<SimpleIssueDto> getSimplifiedIssues(int offset, int limit, SearchConditionDto searchCondition) {
        return null;
    }
}
