package org.kkycp.server.domain;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Embeddable
public class ProjectRegistration {
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @MapKeyJoinColumn(referencedColumnName = "project_id", unique = true)
    private Map<Project, Participation> participations = new HashMap<>();

    public boolean isRegistered(Project project) {
        return participations.containsKey(project);
    }

    //TODO: AUTHORIZATION CHECKING
}
