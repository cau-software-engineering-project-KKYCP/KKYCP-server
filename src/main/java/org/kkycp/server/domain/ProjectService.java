package org.kkycp.server.domain;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.repo.ProjectRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepo projectRepo;

    // TODO check admin authorization
    public Long createProject(User user, String projectName) {
        Project newProject = new Project(user, projectName);
        Project savedProject = projectRepo.save(newProject);
        return savedProject.getId();
    }
}
