## 쇼핑몰의 로그인/회원가입 + Spring Security

### 기본 환경 설치
* Tomcat 서버 설치(개발환경 : Tomcat 9.0)
* MySQL DB 설치(개발환경 : 8.0.19)

### 사용법
* 방법 1.
* com.sif.util 패키지 안의 DefaultSetting을 Java Application으로 실행하고 오류 발생 확인하기
* 프로젝트 우클릭 -> Run As -> Run on Server 클릭하고 오류 발생 확인하기
* 프로젝트 우클릭 -> Run As -> Run Configurations 클릭
* Tomcat v9.0 Server at localhost, DefaultSetting에 각각 Environment -> Add 클릭
* NAME은 ENV_PASS, VALUE는 암호화 키로 사용할 문자열 입력(암호화 키는 Tomcat v9.0과 DefaultSetting 둘 다 똑같이 설정)

* 방법 2.
* 서버 컴퓨터로 사용할 PC에서(Windows 10 기준) 내 컴퓨터 -> 속성 -> 고급 시스템 설정 -> 환경 변수에 사용자 변수 또는 시스템 변수 새로 만들기
* 변수 이름 : ENV_PASS, 변수 값 : 암호화 키로 사용할 문자열 입력

* 방법 1 또는 방법 2를 진행했다면
* MySQL에 사용할 Schema(Database), User 생성
* Gmail에서 SMTP 메일로 사용할 계정 로그인 -> 메일 -> 설정 -> 전달 및 POP/IMAP 탭 -> IMAP 사용 클릭 -> 변경사항 저장
* Gmail에서 앱 비밀번호(2단계 인증 사용해야 발급 가능) 발급 
* com.sif.util 패키지 안의 DefaultSetting을 Java Application으로 실행 후 MySQL, Gmail 설정
* /WEB-INF/spring/properties 폴더 클릭 후 새로고침(F5) (새로고침 하지 않으면 새로 생성된 파일 인식 불가)

* 어플리케이션 실행 후 작동 확인

### Spring Security
* Spring Security Core
* Spring Security Web
* Spring Security Config
* Spring Security Taglibs

* jasypt
* jasypt-spring31

### DB
* spring JDBC
* apache DBCP
* MyBatis
* MyBatis-Spring
* MySQL-J

### Basic Dependency
* lombok
* jackson-databind
* logback

### 프로젝트 설정
1. web.xml 설정 : 한글 인코딩 필터 추가, *-context.xml
2. servlet-context.xml 설정 : component-scan controller, service 추가

### Spring Security 설정
1. spring 폴더 아래에 [jasypt-context.xml] 생성 후 설정
2. spring 폴더 아래에 [db-context.xml] 생성 후 설정
3. spring 폴더 아래에 [security-context.xml] 생성 후 설정
4. spring 폴더 아래에 properties 폴더 생성 후(security-context.xml에서 설정한 폴더) [spring-security.message.ko.properties] 파일 복사
5. service 패키지에 [AuthenticationProvider]를 implements 한 클래스를 하나 만들고 security-context.xml에 설정
6. 유저정보 테이블에 저장할 [UserDetailsVO](implements UserDetails), 권한 테이블에 저장할 [AuthorityVO] 하나씩 만들고 설정
7. [AuthenticationProviderImpl](implements AuthenticationProvider) -> [UserDetailsService] -> [UserDao], [AuthoritiesDao] 만들고 설정
8. spring 폴더 아래에 mapper 폴더 생성 후 [auth-mapper.xml], [user-mapper.xml] 생성 및 설정. user-mapper는 resultMap을 이용해 authorities 테이블 조회 결과도 가져오기

#### web.xml 설정
* 반드시 springSecurityFilterChain, springSecurityFilterChain 설정
* 반드시 contextConfigLocation에 jasypt -> db -> security 순서로 context를 로드하도록 설정한다. 논리적인 연동 순서대로 로드하지 않으면 오류 발생