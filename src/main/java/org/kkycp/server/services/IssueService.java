package org.kkycp.server.services;
import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.*;
import org.kkycp.server.services.*;
import org.kkycp.server.repo.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final ProjectService projectservice;

    /*
    Report report; // Issue의 attribution들을 담은 객체
    long projectId; // Issue가 생성된 project의 주소
    */

    public void createIssue(long projectId, Report report, LocalDate createdDated){
        Project project = projectservice.findProject(projectId).orElseThrow(() -> new NoSuchElementException("No such project exists."));
        project.reportIssue(report, createdDated);
    }
}

