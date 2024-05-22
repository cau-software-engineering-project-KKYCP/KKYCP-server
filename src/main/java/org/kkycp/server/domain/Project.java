package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "project_name")
})
public class Project {
    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Issue> issues = new ArrayList<>();

    public Project(String projectName){
        this.projectName=projectName;
    }

    public Issue reportIssue(Report report, LocalDate reportedDay){
        Issue newIssue = Issue.builder()
                .project(this)
                .title(report.getTitle())
                .reporter(report.getReporter())
                .description(report.getDescription())
                .priority(report.getPriority())
                .reportedDate(reportedDay)
                .type(report.getType())
                .build();
        issues.add(newIssue);
        return newIssue;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project project)) {
            return false;
        }

        return projectName.equals(project.projectName);
    }

    @Override
    public int hashCode() {
        return projectName.hashCode();
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
