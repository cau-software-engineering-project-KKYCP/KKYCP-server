package org.kkycp.server.repo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.kkycp.server.domain.User;
import org.kkycp.server.domain.authorization.Privilege;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserPrivilegeRecord {
    private User user;
    private Set<Privilege> privileges;

    public UserPrivilegeRecord(User user, Collection<Privilege> privileges) {
        this.user = user;
        this.privileges = new HashSet<>(privileges);
    }
}
