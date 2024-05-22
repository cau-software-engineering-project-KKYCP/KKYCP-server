package org.kkycp.server.domain.authorization;

public enum Privilege {
    /**
     * Admin: 프로젝트 생성 가능, 프로젝트에 계정 추가. 모든 프로젝트의 모든 권한을 가짐
     * Participation in the project: 해당 프로젝트의 이슈 생성과 조회, 통계 조회, 코멘트 달기 가능. 자신에게 할당된 이슈를 고칠 수 있음
     * Report: 해당 프로젝트의 이슈 생성
     * Triager: 해당 프로젝트의 구성원에게 이슈 할당
     * Tester: fixed 상태의 이슈를 resolved 상태로 전환하거나, 해결이 안된 경우 다시 assigned로 전환
     * Verifier: resolved 상태의 이슈를 closed 로 바꿈
     */
    ADMIN,
    PARTICIPANT,
    REPORTER,
    TRIAGER,
    TESTER,
    VERIFIER
}
