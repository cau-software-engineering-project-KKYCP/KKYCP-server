package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User{
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @OneToMany(mappedBy = "participatedProject", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyJoinColumn(name = "user_id")
    private final Map<User, Participation> participationsForUser = new HashMap<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Participation> participations = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void setParticipationsForUser(User user, Project project){
        Participation participation = new Participation();

        participation.setUser(user);
        participation.setParticipatedProject(project);

    }
}
