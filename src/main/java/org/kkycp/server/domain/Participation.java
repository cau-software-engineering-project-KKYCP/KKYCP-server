package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kkycp.server.domain.authorization.Privilege;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
public class Participation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project participatedProject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection(targetClass = Privilege.class)
    @JoinTable(name = "privilege", joinColumns = @JoinColumn(name = "participation_id"))
    @Column(name = "grant", nullable = false)
    @Enumerated(EnumType.STRING)
    Set<Privilege> privileges = new HashSet<>();

    public Participation(User user, Project participatedProject) {
        this.participatedProject = participatedProject;
        this.user = user;
        privileges.add(Privilege.PARTICIPANT);
    }

    public void addPrivilege(Privilege privilege) {
        privileges.add(privilege);
    }

    public void replacePrivileges(Collection<Privilege> newPrivileges) {
        privileges = new HashSet<>(newPrivileges);
    }

    public boolean hasPrivilege(Privilege privilege) {
        return privileges.contains(privilege);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Participation that)) {
            return false;
        }

        return participatedProject.equals(that.participatedProject) && user.equals(that.user);
    }

    @Override
    public int hashCode() {
        int result = participatedProject.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}
