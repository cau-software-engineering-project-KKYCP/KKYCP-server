+  Comment

Comment 관리를 위한 CommentService 클래스 추가

Issue를 가져오기 위해 IssueService클래스에 findIssue 메소드 추가

findIssue내에서 IsssuRepo.findbyId(issueid)를 통해 id 탐색

CommentRepo를 통해 Comment 저장, 삭제, 수정 실행


+  Issue description


IssueService에 getDescription메소드 추가

사실상 findIssue와 같은 Issue를 반환하는 기능이라 필요성이 있는지 잘 모르겠네요 ..

+  Statistics

ProjectService의 ShowReportStatistics 메소드

CountIssueRepo 인터페이스 구현 필요

filter라는 새로운 도메인 모델 만들어서

선택에 따라 날짜, 우선순위, 상태 통계 보여주도록 설정

issueList 반환
