package org.kkycp.server.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.kkycp.server.domain.Participation;
import org.kkycp.server.domain.Project;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.domain.User;
import org.kkycp.server.repo.ProjectRepo;

public class ProjectService {

    private ProjectRepo projectRepo;
    private Map<Long, Project> projects = new HashMap<>(); //새로 추가

    public long createProject(User user, String projectName){
        Project project = new Project(user, projectName);
        return project.getId(); //id는 DB로 할당하신다고 해서 일단 getId로 return했습니다
    }
    
    public Optional<Project> findProject(long projectId) {
        Project project = projects.get(projectId);

        if (project == null) {
            System.out.println("프로젝트가 존재하지 않습니다");
            return Optional.empty(); // 프로젝트가 없는 경우 빈 Optional 반환
        } 
        else {
            return Optional.of(project); // 프로젝트가 있는 경우 해당 프로젝트를 담은 Optional 반환
        }
    }
}
