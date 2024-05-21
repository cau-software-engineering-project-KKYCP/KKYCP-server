package org.kkycp.server.services;

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


    @ResponseStatus(HttpStatus.CONFLICT)
    public static class DuplicatedProjectException extends RuntimeException {
        public DuplicatedProjectException(String message) {
            super(message);
        }

        public DuplicatedProjectException(Throwable cause) {
            super(cause);
        }
    }

    public void addUser(long projectId, String username) {
        Optional<Project> projectOptional = projectRepo.findById(projectId);
        if (projectOptional.isPresent()) { // 해당 id 값을 지닌 프로젝트가 존재
            Project mappingProject = projectOptional.get();

            Optional<User> userOptional = userRepo.findByUsername(username);
            if(userOptional.isPresent()) { // 해당 username을 지닌 User가 존재
                User mappingUser = userOptional.get();

                mappingUser.setParticipationsForUser(mappingUser, mappingProject); // Participation 설정
            }
            else{
                throw new RuntimeException("유저를 찾을 수 없습니다."); // 유저가 존재하지 않으면 예외를 발생
            }
        } else {
            throw new RuntimeException("프로젝트를 찾을 수 없습니다."); // 프로젝트가 존재하지 않으면 예외를 발생
        }

    }
}

