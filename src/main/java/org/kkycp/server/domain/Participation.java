package org.kkycp.server.domain;

public class Participation {
    private Long id;
    private Project participatedProject;

    public Participation(Project participatedProject){
        this.participatedProject=participatedProject;//해당 project에 대해 참가정보 생성
    }
}
