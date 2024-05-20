package org.kkycp.server.services;
import org.kkycp.server.domain.*;
import org.kkycp.server.services.*;
import org.kkycp.server.repo.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IssueService {
    ProjectService projectservice;
    Project currentproject;

    /*
    User user; //처음 issue를 생성하는 user
    Report report; // Issue의 attribution들을 담은 객체
    long projectId; // Issue가 생성된 project의 주소
    */


    /*
    처음 이슈 서비스를 생성시 프로젝트 서비스를 생성자로 초기화
     */
    public IssueService(ProjectService projectservice){
        this.projectservice = projectservice;
    }


    //user로 부터 createIssue 호출
    public void createIssue(User user, long projectId, Report report){

        currentproject = projectservice.findProject(projectId); // usecase 1.1, 1.2
        boolean isparticipant = currentproject.isParticipant(user); // 1.3

        if (isparticipant==true) {
            currentproject.reportIssue(report, LocalDate.now()); //1.4
            //create 성공 알림
            System.out.println("Issue creation succeed.\n");
        }
        else {
            // create 실패 경고문
            System.out.println("Issue creation fail.\n");
        }
    }
}

