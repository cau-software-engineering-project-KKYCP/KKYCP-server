package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.kkycp.server.exceptions.IllegalIssueStatusTransitionException;
import org.kkycp.server.exceptions.NotAssignedToUserException;
import org.kkycp.server.exceptions.UserNotParticipatingException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Issue {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Getter(AccessLevel.NONE)
    private Project project;

    @Setter
    private String title;

    @Setter
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    private LocalDate reportedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixer_id")
    private User fixer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Enumerated(EnumType.STRING)
    @Setter
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Setter
    private String type;
    @OneToMany(mappedBy = "parentIssue", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Issue(
            @NonNull Project project,
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

    public void assignIssue(User assignee) {
        if (!Status.ASSIGNED.canTransitionFrom(this.status)) {
            throw new IllegalIssueStatusTransitionException();
        }

        if (!assignee.isParticipated(project)) {
            throw new UserNotParticipatingException();
        }

        this.status = Status.ASSIGNED;
        this.assignee = assignee;
    }

    public void fixIssue(User fixer) {
        if (!Status.FIXED.canTransitionFrom(this.status)) {
            throw new IllegalIssueStatusTransitionException();
        }

        if (!assignee.equals(fixer)) {
            throw new NotAssignedToUserException();
        }

        this.status = Status.FIXED;
        this.fixer = fixer;
    }

    public void addComment(Comment comment) {
        comment.setParentIssue(this);
        comments.add(comment);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    public void changeStatus(Status desiredStatus) {
        if (!desiredStatus.canTransitionFrom(this.status)) {
            throw new IllegalIssueStatusTransitionException();
        }
        this.status = desiredStatus;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Issue issue)) {
            return false;
        }

        return id.equals(issue.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public enum Priority {
        BLOCKER,
        CRITICAL,
        MAJOR,
        MINOR,
        TRIVIAL
    }

    @Getter
    public enum Status {
        NEW(null),
        ASSIGNED(NEW),
        FIXED(ASSIGNED),
        RESOLVED(FIXED),
        CLOSED(RESOLVED),
        REOPENED(RESOLVED);

        private final Status possiblePreviousStatus;

        Status(Status possiblePreviousStatus) {
            this.possiblePreviousStatus = possiblePreviousStatus;
        }

        public boolean canTransitionFrom(Status previousStatus) {
            //ASSIGNED: Cannot refer to enum constant 'REOPENED' before its definition
            if (this == ASSIGNED) {
                return previousStatus == REOPENED || previousStatus == possiblePreviousStatus;
            }
            return previousStatus == possiblePreviousStatus;
        }
    }
}
