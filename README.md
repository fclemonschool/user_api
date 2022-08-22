# 사용자 API(user)
- 회원 가입 및 사용자 기능 일부를 구현합니다.

### Requirements
- Java >= 1.8
- Gradle >= 7.3.3

### 기본 구성
- Java 1.8
- Spring Boot 2.7.3
- Spring Security
- jjwt
- H2 Database
- lombok
- MapStruct
- CheckStyle
- Gradle
- Swagger

### 빌드
```
gradle build
```

### 서버 실행
```
java -jar .\build\libs\user-0.0.1-SNAPSHOT.jar
```

### 구현 기능
- 회원 가입
  - 전화번호 인증 후 회원 가입합니다.
  - 회원 가입 이전에 `인증 번호 전송 - 인증 번호 조회 - 인증 번호 인증 처리` 를 완료해야 회원 가입을 진행할 수 있습니다.
  - `인증 번호 조회` 기능은 휴대폰으로 인증번호를 수신하는 과정을 대체하는 API입니다.
- 로그인
  - `아이디, 전화번호 혹은 이메일 + 비밀번호`를 입력하면 로그인이 가능합니다.
  - 로그인 하면 Bearer Token을 결과값으로 받게 됩니다. 내 정보 보기 기능을 사용할 때 이를 이용합니다.
- 내 정보 조회
  - 로그인 후 가능합니다.
  - Bearer Token을 전송해야 합니다.
- 비밀번호 재설정
  - 로그인 하지 않은 상태에서 전화번호 인증 후 비밀번호 재설정이 가능합니다.
  - 비밀번호 재설정 이전에 `인증 번호 전송 - 인증 번호 조회 - 인증 번호 인증 처리` 를 완료해야 비밀번호 재설정을 진행할 수 있습니다.

### 엔드포인트 목록
- http://localhost:8080/api/v1/users (HTTP:POST)
- http://localhost:8080/api/v1/users/my (HTTP:GET)
- http://localhost:8080/api/v1/users/{phone}/password (HTTP:PUT)
- http://localhost:8080/api/v1/auths (HTTP:POST)
- http://localhost:8080/api/v1/auths/codes (HTTP:POST)
- http://localhost:8080/api/v1/auths/codes/{phone} (HTTP:GET)
- http://localhost:8080/api/v1/auths/codes/{phone} (HTTP:POST)

### 추가로 해야 할 것
- AccessToken 처리

### Swagger
- http://localhost:8080/swagger-ui/index.html
