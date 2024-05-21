package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
