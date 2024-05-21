package org.kkycp.server.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.User;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.repo.UserRepo;
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
    private final UserRepo userRepo;

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


    public void addUser(long projectId, String username) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("No such project."));
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("No such user"));
        user.participate(project);
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
}

