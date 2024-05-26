package org.kkycp.server.repo;

import org.kkycp.server.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Project.class, idClass = Long.class)
public interface ProjectRepo extends JpaRepository<Project, Long> {
    boolean existsByProjectName(String projectName);
}
