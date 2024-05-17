package org.kkycp.server.domain;

import java.util.HashMap;
import java.util.Map;

public class Project {
    private int id;
    private String projectName;
    private Map<User, Participation> participationByUser;

    public Project(User user, String projectName){
        this.projectName=projectName;
        this.participationByUser = new HashMap<>();
        this.participationByUser.put(user, new Participation(this));    //user를 현재 project의 participation으로 추가
    }

    public int getId(){ // 새로 추가
        return this.id;
    }

    public boolean isParticipant(User user){
        Participation participation = participationByUser.get(user);    //user의 particiapation 정보를 가져옴
        if(participation==null) //null일 경우 false
            return false;
        else
            return true;
    }

    public long reportIssue(report Report){
        report Report = new report();
        return Report.id;    
    }
}
