package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Entity
public class Issue {
    @Id
    @GeneratedValue
    private Long id;

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

    protected Issue() {
    }

    @Builder
    public Issue(@NonNull User reporter,
                 @NonNull String title,
                 @NonNull String description,
                 @NonNull Issue.Priority priority,
                 @NonNull LocalDate reportedDate) {
        this.title = title;
        this.description = description;
        this.reporter = reporter;
        this.reportedDate = reportedDate;
        this.priority = priority;
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
