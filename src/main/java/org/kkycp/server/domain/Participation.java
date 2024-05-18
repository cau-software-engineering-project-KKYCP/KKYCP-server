package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Participation {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project participatedProject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}

