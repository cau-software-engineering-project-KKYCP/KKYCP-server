package org.kkycp.server.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.domain.Project;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.repo.issue.IssueRepo;
import org.kkycp.server.repo.issue.IssueSearchCondition;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepo projectRepo;

    private final IssueRepo issueRepo;

    public long createProject(String projectName) {
        Project project = new Project(projectName);

        try {
            return projectRepo.save(project).getId();
        } catch (DuplicateKeyException e) {
            throw new DuplicatedProjectException(e);
        }
    }
    
    public Optional<Project> findProject(long projectId) {
        return projectRepo.findById(projectId);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    public static class DuplicatedProjectException extends RuntimeException {
        public DuplicatedProjectException(String message) {
            super(message);
        }

        public DuplicatedProjectException(Throwable cause) {
            super(cause);
        }
    }

    public List<Issue> findIssues(long projectId, IssueSearchCondition condition, int page, int quantity){
        int offset = (page - 1) * quantity;
        return issueRepo.search(projectId, condition, offset, quantity);
    }
}

