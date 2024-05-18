package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {
    @Id
    @GeneratedValue
    private Long id;

    private String projectName;

    @OneToMany(mappedBy = "participatedProject", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyJoinColumn(name = "user_id")
    private final Map<User, Participation> participationByUser = new HashMap<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Issue> issues = new ArrayList<>();

    public Project(String projectName){
        this.projectName=projectName;
    }

    public boolean isParticipant(User user){
        Participation participation = participationByUser.get(user);    //user의 particiapation 정보를 가져옴
        if(participation==null) //null일 경우 false
            return false;
        else
            return true;
    }

    public Issue reportIssue(Report report, LocalDate reportedDay){
        Issue newIssue = Issue.builder()
                .title(report.getTitle())
                .reporter(report.getReporter())
                .description(report.getDescription())
                .priority(report.getPriority())
                .reportedDate(reportedDay)
                .build();
        issues.add(newIssue);
        return newIssue;
    }
}
