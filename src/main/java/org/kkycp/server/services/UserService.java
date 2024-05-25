package org.kkycp.server.services;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.User;
import org.kkycp.server.domain.authorization.Privilege;
import org.kkycp.server.exceptions.UserNotParticipatingException;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;

    public void registerUserTo(long projectId, String username) {
        Project project = projectRepo.findById(projectId).get();
        User user = userRepo.findByUsername(username).get();
        user.register(project);
    }

    public void replaceUserPrivileges(String username, long projectId, List<Privilege> privileges) {
        User user = userRepo.findByUsernameFetchParticipation(username).get();
        Project project = projectRepo.findById(projectId).get();
        user.replacePrivileges(project, privileges);
    }

    public List<User> findAllUserByProjectId(long projectId) {
        return userRepo.findAllByProjectId(projectId);
    }

    public User findByUsernameAndProjectId(String username, long projectId) {
        User user = userRepo.findByUsername(username).get();
        Project project = projectRepo.findById(projectId).get();
        if (!user.isParticipated(project)) {
            throw new UserNotParticipatingException();
        }

        return user;
    }
}
