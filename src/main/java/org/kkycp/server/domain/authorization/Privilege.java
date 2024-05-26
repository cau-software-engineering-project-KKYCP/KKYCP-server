package org.kkycp.server.domain.authorization;

public enum Privilege {
    /**
     * <ul>
     *     <li>Participation in the project: 해당 프로젝트의 이슈 생성과 조회, 통계 조회, 코멘트 달기 가능. 자신에게 할당된 이슈를 고칠 수 있음</li>
     *     <li>Report: 해당 프로젝트의 이슈 생성</li>
     *     <li>Triager: 해당 프로젝트의 구성원에게 이슈 할당</li>
     *     <li>Tester: fixed 상태의 이슈를 resolved 상태로 전환하거나, 해결이 안된 경우 다시 assigned로 전환</li>
     *     <li>Verifier: resolved 상태의 이슈를 closed 로 바꿈</li>
     * </ul>
     */
    PARTICIPANT,
    REPORTER,
    TRIAGER,
    TESTER,
    VERIFIER
}
