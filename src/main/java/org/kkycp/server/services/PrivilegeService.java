package org.kkycp.server.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.kkycp.server.domain.Project;
import org.kkycp.server.domain.User;
import org.kkycp.server.domain.authorization.Privilege;
import org.kkycp.server.repo.ProjectRepo;
import org.kkycp.server.repo.UserPrivilegeRecord;
import org.kkycp.server.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PrivilegeService {
    private final UserRepo userRepo;

    public List<UserPrivilegeRecord> getAllUserPrivileges(long projectId) {
        return userRepo.getAllUserPrivileges(projectId);
    }
}
