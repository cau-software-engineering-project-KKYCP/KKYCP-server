package org.kkycp.server.domain.authorization;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.User;
import org.kkycp.server.exceptions.UserNotParticipatingException;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.repo.UserRepo;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationChecker {
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;

    public boolean checkPrivilege(String username, Long projectId, Privilege privilege) {
        User user = userRepo.findByUsernameFetchParticipation(
                username).orElseThrow(() -> new NoSuchElementException("There is no such user."));
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new NoSuchElementException("There is no such project"));

        if (!user.isParticipated(project)) {
            throw new UserNotParticipatingException();
        }

        return user.hasPrivilege(project, privilege);
    }
}
