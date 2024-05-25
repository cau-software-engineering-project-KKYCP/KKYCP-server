package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kkycp.server.domain.authorization.Privilege;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User{
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @Embedded
    private ProjectRegistration projectRegistration;

    public User(String username) {
        this.username = username;
        this.projectRegistration = new ProjectRegistration();
    }

    public void register(Project project) {
        projectRegistration.register(this, project);
    }

    public boolean isParticipated(Project project) {
        return projectRegistration.isRegistered(project);
    }

    public void addPrivilege(Project grantedProject, Privilege privilege) {
        projectRegistration.addPrivilege(grantedProject, privilege);
    }

    public void replacePrivileges(Project grantedProject, Collection<Privilege> privileges) {
        projectRegistration.replacePrivileges(grantedProject, privileges);
    }

    public boolean hasPrivilege(Project project, Privilege privilege) {
        return projectRegistration.hasPrivilege(project, privilege);
    }

    public List<Privilege> getPrivileges(Project project) {
        return new ArrayList<>(projectRegistration.getPrivilege(project));
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
