package org.kkycp.server.services;

import lombok.RequiredArgsConstructor;
import org.kkycp.server.repo.UserPrivilegeRecord;
import org.kkycp.server.repo.UserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
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
