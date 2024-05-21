package org.kkycp.server.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

public class Issue {
    private Long id;
    private String title;
    private String description;
    private User reporter;
    private LocalDate reportedDate;
    private User fixer;
    private User assignee;
    private Priority priority;
    private Status status;
    private String type;

    protected Issue() {
    }

    @Builder
    public Issue(@NonNull User reporter,
                 @NonNull String title,
                 @NonNull String description,
                 @NonNull Issue.Priority priority,
                 @NonNull LocalDate reportedDate,
                 @NonNull String type) {
        this.title = title;
        this.description = description;
        this.reporter = reporter;
        this.reportedDate = reportedDate;
        this.priority = priority;
        this.type = type;
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
