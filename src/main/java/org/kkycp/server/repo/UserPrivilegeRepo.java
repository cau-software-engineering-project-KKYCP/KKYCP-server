package org.kkycp.server.repo;

import java.util.List;

public interface UserPrivilegeRepo {
    List<UserPrivilegeRecord> getAllUserPrivileges(Long projectId);
}
