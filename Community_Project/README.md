# 커뮤니티 프로젝트

## 개발 환경
* Tomcat (개발 환경 버전 : 9.0)
* MySQL (개발 환경 버전 : 8.0.19)

## 사용법(기본 설정)
### 암호화 Key 설정 : 방법 1과 방법 2 중 하나를 선택해서 진행
* 방법 1 : 서버에 환경변수 값 설정하기
* com.sif.util 패키지 안의 DefaultSetting.java를 Java Application으로 실행, 오류 발생 확인
* 프로젝트 우클릭 -> Run As -> Run on Server 클릭, 오류 발생 확인
* 프로젝트 우클릭 -> Run As -> Run Configurations 클릭
* Tomcat v9.0 Server at localhost과 DefaultSetting의 Environment에 각각 Add 클릭 후 NAME은 ENV_PASS, VALUE는 암호화 키로 사용할 문자열 입력(암호화 키는 Tomcat v9.0과 DefaultSetting 똑같이 설정)

* 방법 2 : 서버 컴퓨터에 환경변수 값 설정하기
* 서버 컴퓨터로 사용할 PC에서(Windows 10 기준) 내 컴퓨터 -> 속성 -> 고급 시스템 설정 -> 환경 변수에 사용자 변수나 시스템 변수 만들기
* 변수 이름 : ENV_PASS, 변수 값 : 암호화 키로 사용할 문자열

### DB 설정
* MySQL DBMS에서 Schema(Database), User 생성

### SMTP 발송용 메일 설정(Gmail)
* Gmail에서 SMTP 메일로 사용할 Gmail 계정 로그인 -> 메일 -> 설정 -> 전달 및 POP/IMAP 탭 -> IMAP 사용 클릭 -> 변경사항 저장
* Gmail에서 앱 비밀번호(2단계 인증 사용해야 발급 가능) 발급

### 서버에서 읽을 properties 파일 설정
* com.sif.util 패키지 안의 DefaultSetting을 Java Application으로 실행 후 MySQL, Gmail 설정
* /WEB-INF/spring/properties 폴더 클릭 후 새로고침(F5) (새로고침 하지 않으면 새로 생성된 파일 인식 못함)
* 어플리케이션 실행 후 작동 확인

## pom.xml에서 받은 라이브러리 목록
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

### Utility Dependencies
* lombok
* jackson-databind
* logback

## xml 설정
1. web.xml 설정 : 한글 인코딩 필터 추가, *-context.xml 순서대로 추가
2. appServlet/servlet-context.xml 설정 : component-scan controller와 service 추가, interceptor 추가, 파일 업로드 폴더 열어주기, 파일 경로 전역 변수 bean으로 등록

## Spring Security 설정
1. spring 폴더 아래에 [jasypt-context.xml] 생성 및 설정 : Encryptor, Config, PropertyPlaceholderConfigurer
2. spring 폴더 아래에 [db-context.xml] 생성 및 설정 : BasicDataSource, SqlSessionFactoryBean, trasactionManager
3. spring 폴더 아래에 [security-context.xml] 생성 및 설정 : global-method-security(어노테이션 활성화), BCryptPasswordEncoder, component-scan + Provider, authentication-provider, intercept-url, form-login, form-logout, (옵션)ReloadableResourceBundleMessageSource, (옵션)LoginSuccessHandlerImpl
4. spring 폴더 아래에 properties 폴더 생성 후(ReloadableResourceBundleMessageSource에서 설정한 폴더) [spring-security.message.ko.properties] 파일 복사
5. service 패키지에 [AuthenticationProvider]를 implements 한 클래스를 하나 만들고 security-context.xml에 bean으로 설정
6. 유저 테이블에 저장할 [UserDetailsVO](implements UserDetails), 권한 테이블에 저장할 [AuthorityVO] 하나씩 만들고 설정
7. [AuthenticationProviderImpl](implements AuthenticationProvider) -> [UserDetailsServiceImpl](implements UserDetailsService) -> [UserDao], [AuthoritiesDao] 만들고 설정
8. spring 폴더 아래에 mapper 폴더 생성 후 [auth-mapper.xml], [user-mapper.xml] 생성 및 설정. user-mapper는 mybatis의 resultMap을 이용해 authorities 테이블 조회 결과도 가져오기

### web.xml에 Spring Sescurity 설정
1. filter, filter-mapping에 springSecurityFilterChain 설정
2. contextConfigLocation에 jasypt -> db -> security 순서로 context.xml을 로드하도록 설정. 논리적인 연동 순서대로 로드하지 않으면 오류 발생

## 파일 업로드 설정
1. 파일업로드-context.xml에 CommonsMultipartResolver 설정. id는 반드시 multipartResolver로 설정
2. multipartResolver 안에 maxUploadSize, maxUploadSizePerFile, defaultEncoding 설정

### web.xml에 파일 업로드 설정
1. Spring Security 사용 시 csrf 방지와 form(method="POST" enctype="multipart/form-data")은 충돌이 일어남
2. `<filter>
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
	</filter-mapping>`
    web.xml에 위와 같이 설정해주면 Spring Security와 파일 업로드 충돌을 방지할 수 있음