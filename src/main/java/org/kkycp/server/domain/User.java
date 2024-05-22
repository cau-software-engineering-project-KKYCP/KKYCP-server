package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private ProjectRegistration projectRegistration = new ProjectRegistration();

    public void participate(Project project) {
        projectRegistration.register(this, project);
    }

    public boolean isParticipated(Project project) {
        return projectRegistration.isRegistered(project);
    }

    public User(String username) {
        this.username = username;
        this.projectRegistration = new ProjectRegistration();
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
