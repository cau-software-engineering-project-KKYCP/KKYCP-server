package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Issue {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String title;

    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    private LocalDate reportedDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixer_id")
    private User fixer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String type;

    @Builder
    public Issue(@NonNull Project project,
                 @NonNull User reporter,
                 @NonNull String title,
                 @NonNull String description,
                 @NonNull Issue.Priority priority,
                 @NonNull LocalDate reportedDate,
                 @NonNull String type) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.reporter = reporter;
        this.reportedDate = reportedDate;
        this.priority = priority;
        this.type = type;
        this.status = Status.NEW;
    }

    public enum Priority {
        BLOCKER,
        CRITICAL,
        MAJOR,
        MINOR,
        TRIVIAL
    }

    public enum Status {
        NEW,
        ASSIGNED,
        RESOLVED,
        CLOSED,
        REOPENED
    }
}
