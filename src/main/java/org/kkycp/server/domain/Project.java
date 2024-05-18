package org.kkycp.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Project {
    @Id
    @GeneratedValue
    private Long id;

    private String projectName;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issue> issues = new ArrayList<>();
    private Map<User, Participation> participationByUser;
    private List<Issue> issues;

    public Project(User user, String projectName){
        this.projectName=projectName;
        this.participationByUser = new HashMap<>();
        this.participationByUser.put(user, new Participation(this));    //user를 현재 project의 participation으로 추가
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
