package org.kkycp.server.services;

public class Issue{
    long id;
    String title;
    String description;
    User reporter;
    LocalDate reportedDate;
    User fixer;
    User asignee;
    prioriy Priority;
    Status status;
}

public class IssueService {
    ProjectService projectservice;
    Project currentproject;

    User user; //처음 issue를 생성하는 user
    Report report; // Issue의 attribution들을 담은 객체
    long projectId; // Issue가 생성된 project의 주소


    List<Issue> Issuelist = new ArrayList<>();

    /*
    처음 이슈 서비스를 생성시 프로젝트 서비스를 생성자로 초기화
     */
    public IssueService(ProjectService projectservice) {
        this.projectservice = projectservice;
    }



    //user로 부터 createIssue 호출
    public void createIssue(User user, long projectId, Report report){
        this.user=user;
        this.projectId=projectId;
        this.report=report;
        currentproject = projectservice.getProject(projectId); // usecase 1.1, 1.2
        boolean isparticipant = isParticipant(user, currentproject); // 1.3

        if (isparticipant==true) {
            reportIssue(report);
            Issuelist.add(realCreate());

            //create 성공 알림
        }
        else {
            // create 실패 경고문
        }

    }

    public boolean isParticipant(User user, Project project){
        boolean isparticipant;
        /*
        if (user가 project에 속해있으면){
            isparticipant=true;
        }
        else{
            isparticipant=false;
        }
         */
        return isparticipant;
    }

    public void reportIssue(Report report){

    }

    //user가 검증되면 실제로 Issue를 create
    public Issue realCreate(){
        Issue issue = new Issue();
        /*
        현재 class에 존재하는 user, report 객체로 issue 초기화
         */
        return issue;
    }


}
