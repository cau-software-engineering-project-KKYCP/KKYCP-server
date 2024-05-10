#   Functional Requirement

##  -유저가 프로젝트를 개설

+   Primary Actor: User

+   Postcondition: 프로젝트를 개설한 유저는 해당 프로젝트의 Admin이 됨

+   Basic Flow:

        1. User가 프로젝트 개설 버튼을 누름

        2. User가 프로젝트 이름을 작성

        3. 시스템은 프로젝트를 개설하고, 프로젝트에 할당된 ID를 유저에게 반환

+   Alternative Flow:

        2a. User가 잘못된 프로젝트 이름을 입력한 경우, 유저에게 알려 올바르게 프로젝트 이름을 입력하게 함.

+   ETC: Admin이 된 User에게 반환된 프로젝트 ID를 이용해 다른 사용자가 프로젝트에 참여 가능

##  -이슈 검색

+   Primary Actor: User

+   Basic flow

        1. User가 필터 대상을 선택한다.

        2. User가 검색 키워드를 작성한다.

        3. User가 검색 버튼을 누른다.

        4. 시스템은 검색 결과를 유저에게 제공한다.

+   Alternative flow

        4a. 검색 결과가 없다면 검색 결과가 없다고 알린다.

+   필터 대상
    + Assignee
    + Reporter
    + Priority
    + Status
    + Type
    + 검색 키워드 대상
    + Title

##  -프로젝트의 유저가 이슈의 상세 정보를 확인

+   Primary Actor: Project’s User
 
+   Precondition: user가 시스템에 접속한 상태(로그인) 및 이슈가 등록된 상태
 
+   Postcondition: 이슈의 상세정보 UI가 제공됨
 
+   Basic flow

        1. Project의 User(PL1, Reporter, Fixer, etc)가 Issue list에서 issue를 선택한다.

        2. 선택된 issue의 상세정보(Title, Description, Priority, Type)를 제공한다. 
 
+   Alternative flow

        2a. 선택된 이슈의 상세정보를 불러오는데 실패한다면, 유저에게 알린다.

##  -유저가 이슈에 코멘트를 달고, 자신이 단 코멘트를 관리함

+   Primary Actor: User

+   Precondition: 유저는 시스템에 로그인되어 있어야 함, 해당 이슈가 존재해야 함

+   Postcondition: 이슈에 코멘트가 추가됨

+   Basic Flow:

        1.    유저가 시스템에 로그인

        2.    유저가 이슈를 검색

        3.    이슈의 상세 페이지에 코멘트를 입력하고 제출 버튼 누름

        4.    시스템이 코멘트를 DB에 추가하고 이슈와 같이 표시함

+   Alternative Flow:

        3a. 유저가 코멘트를 입력하지 않고 "제출" 버튼을 클릭하는 경우: 시스템은 코멘트 입력을 요구하는 오류 메시지 표시

        4a. 유저가 코멘트를 수정, 삭제하는 경우:
    시스템은 수정 또는 삭제된 코멘트를 처리하고, 이슈 페이지에 코멘트를 업데이트

##  -이슈등록

+   Primary Actor: Reporter

+   Precondition : User가 시스템에 접속한 상태(로그인)

+   Postcondition: Report된 이슈가 시스템 상에 영구히 저장된다.

+   Basic flow

        1. Reporter가 인터페이스를 통해 시스템에서 “새 Issue 등록” 을 시도한다.

        2. Reporter가 Issue를 작성한다.

            -   Title

            -   Describtion

            -   Priority

            -   Type

        3. 시스템에 작성된 이슈를 저장한다.

+   Alternative flow

        3a. 만약 시스템이 이슈 저장을 실패한다면 유저에게 알린다.

##  -Triager가 이슈를 특정 유저에게 할당

+   Primary Actor : User(Triager)

+   Precondition : User가 시스템에 접속한 상태, 시스템에서 검색기능 사용가능, Triagger Use인 경우만 해당함

+   Post condition : 할당된 이슈를 할당 받은 User가 조회할 수 있다.

+   Basic Flow

        1. PA(Primary Actor)가 인터페이스를 통해 할당되지 않은 이슈를 검색한다.

        2. PA가 할당되지 않은 이슈에 대한 정보를 확인한다.

  	        - 이슈의 상태로 필터링

        3. PA가 이슈를 유저에게 할당한다.

         - 추천 기능 활용 가능
 
+   Alternative flow

        2a. 할당되지 않은 이슈가 없다면 할당되지 않은 이슈가 없다고 알린다.

        3a. 이슈를 할당하는 것을 실패했다고 유저에게 알린다.



##  -권한 있는 유저의 이슈 상태 변경

+   Primary Actor: user

+   Precondition: user는 상태를 수정할 권리를 가지고 있다. 프로젝트 내의 Issue가 최소 1개 이상 생성 되어있다.
        
+   Postcondition: 이슈의 상태가 변경되었다.

+   Basic Flow:

        1.  User가 issue 상태를 선택한다.

        2.  Apply 버튼을 누른다.

        3.  System은 User가 권한을 가지고 있는지 확인한다.

        4.  Issue의 상태가 변경되어 표시된다.

+   Alternative Flow

 	    4a. user의 권한이 없다고 표시한다.
  
+   Statements: new/assigned/resolved/closed/reopened


#   Non-Functional Requirement

1.  JAVA 사용

2.  Http Api 사용  
3.  Frontend: Swing, HTML+JS
4.  Http Basic Login
5.  JPA, MySql, Swing
6.  Document: docx, md
7.  Junit5
8.  검색은 최대한 DB에서 진행


#   Domain Model


#   Design
##  -Project
+   create

##  -Issue
+   create

##  -Comment
+   JPA의 도움으로, Project에 List<Comment> comments 필드를 넣고, comments.add()를 하면 자동으로 DB에 comment가 저장된다.


#   ERD
+   각 Role이 어떤 Privileges를 가지는 지는 어플리케이션 내부에서 결정된다.
