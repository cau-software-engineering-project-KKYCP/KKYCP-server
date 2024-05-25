package org.kkycp.server.repo;

import lombok.Data;
import org.kkycp.server.domain.User;
import org.kkycp.server.domain.authorization.Privilege;

import java.util.Set;

@Data
public class UserPrivilegeRecord {
    private User user;
    private Set<Privilege> privileges;

    public UserPrivilegeRecord(User user, Set<Privilege> privileges) {
        this.user = user;
        this.privileges = privileges;
    }
}
