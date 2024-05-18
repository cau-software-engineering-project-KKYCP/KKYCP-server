package org.kkycp.server.services;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.Project;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepo projectRepo;

    public long createProject(String projectName) {
        if(projectRepo.existsByProjectName(projectName)) {
            throw new DuplicatedProjectException("Project name " + projectName + " is duplicated.");
        }
        Project project = new Project(projectName);
        return projectRepo.save(project).getId();
    }
    
    public Optional<Project> findProject(long projectId) {
        return projectRepo.findById(projectId);
    }
}

@ResponseStatus(HttpStatus.CONFLICT)
class DuplicatedProjectException extends RuntimeException {
    public DuplicatedProjectException(String message) {
        super(message);
    }
}
