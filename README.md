# KKYCP 백엔드 서버

본 프로젝트는 중앙대 2024학년 봄학기 소프트웨어공학 수업 팀 프로젝트의 백엔드 서버입니다.

## 사용 프레임워크

- Spring boot for Spring web
- Spring security
- JPA
- QueryDsl

## 실행 방법 (윈도우 기준)

1. jdk/bin을 path에 추가하고, JAVA_HOME을 환경변수에 추가한다.
2. 터미널 실행
3. 프로젝트 폴더(KKYCP-server) 에서`./gradlew build`입력
4. `./gradlew bootJar`입력
5. `java -jar ./build/libs/server-**.jar` 명령어를 통해 jar파일 실행

> java 명령어는 jdk 설치 후, {설치한 jdk 폴더}/bin 경로를 PATH에 추가하면 사용 가능

> 만약 8080포트가 이미 사용중이라고 뜨면, [PowerShell에서 해당 프로세스를 kill](https://stackoverflow.com/a/39633428)한 뒤 다시 실행