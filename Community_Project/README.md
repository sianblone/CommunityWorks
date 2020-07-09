# 커뮤니티 프로젝트

## 사용법(기본 설정)

### 개발 환경
- Java (개발 환경 버전: 1.8)
- Spring Tool Suite (개발 환경 버전: 3.9)
- Tomcat (개발 환경 버전: 9.0)
- MySQL (개발 환경 버전: 8.0.19)
- Windows (개발 환경 버전: 10)

### 암호화 Key 설정 : 방법 1과 방법 2 중 하나를 선택해서 진행
#### 방법 1 : 서버에 환경변수 값 설정하기
1. com.sif.util 패키지 안의 DefaultSetting.java를 Java Application으로 실행, 오류 발생 확인
2. 프로젝트 우클릭 -> Run As -> Run on Server 클릭, 오류 발생 확인
3. 프로젝트 우클릭 -> Run As -> Run Configurations 클릭
4. Tomcat v9.0 Server at localhost와 DefaultSetting의 Environment에 각각 Add 클릭 후 NAME은 ENV_PASS, VALUE는 암호화 키로 사용할 문자열 입력(둘 다 환경변수 이름, 환경변수 값 똑같이 설정)

#### 방법 2 : 서버 컴퓨터에 환경변수 값 설정하기
1. 서버 컴퓨터로 사용할 PC에서(Windows 10 기준) 내 컴퓨터 -> 속성 -> 고급 시스템 설정 -> 환경 변수에 사용자 변수나 시스템 변수 만들기
2. 변수 이름 : ENV_PASS, 변수 값 : 암호화 키로 사용할 문자열 입력

### DB 설정
- MySQL DBMS에서 Schema(Database), User 생성

### SMTP 발송용 메일 설정(Gmail)
1. Gmail에서 SMTP 메일로 사용할 Gmail 계정 로그인 -> 메일 -> 설정 -> 전달 및 POP/IMAP 탭 -> IMAP 사용 클릭 -> 변경사항 저장
2. Gmail에서 앱 비밀번호(2단계 인증 사용해야 발급 가능) 발급

### 서버에서 읽을 properties 파일 설정
1. com.sif.util 패키지 안의 DefaultSetting을 Java Application으로 실행 후 MySQL, Gmail 설정
2. /WEB-INF/spring/properties 폴더 클릭 후 새로고침(F5) (새로고침 하지 않으면 새로 생성된 파일 인식 못함)

## 작동 확인 및 배포
- 설정 완료 후 어플리케이션 실행해서 작동 확인
- 정상적으로 작동하는지 확인 후 배포

## 프로젝트 목적
1. 게시판 CRUD, 게시판 순서 변경, 게시판 별 페이지네이션 설정
2. 게시글 CRUD, 무한 계층 답글, 검색, 페이지네이션
3. 댓글 CRUD, 무한 계층 댓글, 리버스 페이지네이션
4. DB 연동 (JDBC, DBCP, MyBatis, MySQL)
5. JavaMail + SMTP를 이용한 이메일 인증
6. Spring Security를 이용한 회원가입, 로그인, 유저 정보 수정
7. Multipart를 이용한 파일, 이미지 업로드
8. HTML5, CSS, JS, jQuery를 이용한 화면구현
9. Ajax를 이용한 유효성 검사 및 프론트엔드
10. 자체 개발한 컨텍스트 메뉴
11. 동적으로 나타나는 댓글 수정&대댓글 창
12. 메인페이지에 표시되는 게시판 별 최신 글, 최대 글 수 설정
13. 쿠키와 Spring Security를 이용한 조회수, 추천 중복 방지

- 위의 기술들을 결합한 커뮤니티형 게시판 제작

## 개발 기록
### pom.xml에서 받은 라이브러리 목록
#### Spring Security
- Spring Security Core
- Spring Security Web
- Spring Security Config
- Spring Security Taglibs

- jasypt
- jasypt-spring31

#### DB
- spring JDBC
- apache DBCP
- MyBatis
- MyBatis-Spring
- MySQL-J

#### Utility Dependencies
- lombok
- jackson-databind
- logback

### xml 설정
1. web.xml 설정 : 한글 인코딩 필터 추가, *-context.xml 순서대로 추가
2. appServlet/servlet-context.xml 설정 : component-scan controller와 service 추가, interceptor 추가, 파일 업로드 폴더 열어주기, 파일 경로 전역 변수 bean으로 등록

### Spring Security 설정
1. spring 폴더 아래에 [jasypt-context.xml] 생성 및 설정 : Encryptor, Config, PropertyPlaceholderConfigurer
2. spring 폴더 아래에 [db-context.xml] 생성 및 설정 : BasicDataSource, SqlSessionFactoryBean, trasactionManager
3. spring 폴더 아래에 [security-context.xml] 생성 및 설정 : global-method-security(어노테이션 활성화), BCryptPasswordEncoder, component-scan + Provider, authentication-provider, intercept-url, form-login, form-logout, (옵션)ReloadableResourceBundleMessageSource, (옵션)LoginSuccessHandlerImpl
4. spring 폴더 아래에 properties 폴더 생성 후(ReloadableResourceBundleMessageSource에서 설정한 폴더) [spring-security.message.ko.properties] 파일 복사
5. service 패키지에 [AuthenticationProvider]를 implements 한 클래스를 하나 만들고 security-context.xml에 bean으로 설정
6. 유저 테이블에 저장할 [UserDetailsVO](implements UserDetails), 권한 테이블에 저장할 [AuthorityVO] 하나씩 만들고 설정
7. [AuthenticationProviderImpl](implements AuthenticationProvider) -> [UserDetailsServiceImpl](implements UserDetailsService) -> [UserDao], [AuthoritiesDao] 만들고 설정
8. spring 폴더 아래에 mapper 폴더 생성 후 [auth-mapper.xml], [user-mapper.xml] 생성 및 설정. user-mapper는 mybatis의 resultMap을 이용해 authorities 테이블 조회 결과도 가져오기

#### web.xml에 Spring Sescurity 설정
1. filter, filter-mapping에 springSecurityFilterChain 설정
2. contextConfigLocation에 jasypt -> db -> security 순서로 context.xml을 로드하도록 설정. 논리적인 연동 순서대로 로드하지 않으면 오류 발생

### 파일 업로드 설정
1. fileupload-context.xml에 CommonsMultipartResolver 설정. id는 반드시 multipartResolver로 설정
2. multipartResolver 안에 maxUploadSize, maxUploadSizePerFile, defaultEncoding 설정

#### web.xml에 파일 업로드 설정
1. Spring Security 사용 시 csrf 방지와 form(method="POST" enctype="multipart/form-data")은 충돌이 일어남
2. ```
	<filter>
	    <filter-name>MultipartFilter</filter-name>
	    <filter-class>org.springframework.web.multipart.support.MultipartFilter</filter-class>
	    <init-param>
	    	<param-name>multipartResolverBeanName</param-name>
	    	<param-value>multipartResolver</param-value>
	    </init-param>
	</filter>
	<filter-mapping>
	    <filter-name>MultipartFilter</filter-name>
	    <url-pattern>/*</url-pattern>
	</filter-mapping>
	```
    web.xml에 위와 같이 설정해주면 Spring Security와 multipart/form-data 충돌을 방지할 수 있음