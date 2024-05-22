package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

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
        Participation newParticipation = new Participation(user, project);
        participations.put(project, newParticipation);
    }

    //TODO: AUTHORIZATION CHECKING
}
