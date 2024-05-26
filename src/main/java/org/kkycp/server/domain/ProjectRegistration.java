package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.kkycp.server.domain.authorization.Privilege;
import org.kkycp.server.exceptions.UserAlreadyParticipatingException;
import org.kkycp.server.exceptions.UserNotParticipatingException;

import java.util.*;

@Embeddable
@NoArgsConstructor
public class ProjectRegistration {
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @MapKeyJoinColumn(name = "project_id")
    private Map<Project, Participation> participations = new HashMap<>();

    public boolean isRegistered(Project project) {
        return participations.containsKey(project);
    }

    public void register(User user, Project project) {
        if (participations.containsKey(project)) {
            throw new UserAlreadyParticipatingException();
        }
        Participation newParticipation = new Participation(user, project);
        participations.put(project, newParticipation);
    }

    void addPrivilege(Project grantedProject, Privilege privilege) {
        Participation participation = getParticipation(grantedProject);
        participation.addPrivilege(privilege);
    }

    boolean hasPrivilege(Project project, Privilege privilege) {
        Participation participation = getParticipation(project);
        return participation.hasPrivilege(privilege);
    }

    public void replacePrivileges(Project project, Collection<Privilege> privileges) {
        Participation participation = this.participations.get(project);
        if (participation == null) {
            throw new UserNotParticipatingException();
        }

        participation.replacePrivileges(privileges);
    }

    public Set<Privilege> getPrivilege(Project project) {
        Participation participation = getParticipation(project);
        return Collections.unmodifiableSet(participation.privileges);
    }

    private Participation getParticipation(Project grantedProject) {
        Participation participation = participations.get(grantedProject);
        if (participation == null) {
            throw new UserNotParticipatingException("This user is not participating in the project.");
        }
        return participation;
    }
    //TODO: AUTHORIZATION CHECKING
}
