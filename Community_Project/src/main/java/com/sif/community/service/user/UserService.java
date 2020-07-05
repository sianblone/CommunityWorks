package com.sif.community.service.user;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sif.community.dao.AuthoritiesDao;
import com.sif.community.dao.UserDao;
import com.sif.community.model.AuthorityVO;
import com.sif.community.model.UserDetailsVO;
import com.sif.community.service.user.itf.SendService;
import com.sif.util.PbeEncryptor;
import com.sif.util.SpSec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	// bean으로 설정된 PasswordEncoder 가져오기
	private final PasswordEncoder bcryptEncoder;
	private final UserDao userDao;
	private final AuthoritiesDao authDao;
	@Qualifier("mailSvc")
	private final SendService mailSvc;
	
	// selectAll() 메소드는 2개의 테이블을 select하고 있다
	// 이런 경우 1번 테이블의 데이터는 가져왔는데 2번 테이블을 가져오기 전
	// 다른 사용자가 2번 테이블의 데이터를 수정할 수 있다
	// @Transactional로 여기에 트랜잭션을 걸어주면 셀렉트할 때 다른 사용자가 select중인 테이블의 정보를 수정하지 못하도록 한다
	@Transactional
	public List<UserDetailsVO> selectAll() {
		return userDao.selectAll();
	}
	
	@Transactional
	public UserDetailsVO findByUsername(String username) {
		return userDao.findByUsername(username);
	}
	
	@Transactional
	public UserDetailsVO findByUsernameFromAuthorities(String username) {
		return userDao.findByUsernameFromAuthorities(username);
	}
	
	// 닉네임과 일치하는 유저 정보 가져오기
	public List<UserDetailsVO> findByNickname(String nickname) {
		return userDao.findByNickname(nickname);
	}
	
	public int updateUserFromAdmin(UserDetailsVO userVO) {
		boolean isAdmin = SpSec.isAdmin();
		
		if(isAdmin) {
			return userDao.updateUserFromAdmin(userVO);
		} else {
			return -200;
		}
	}
	
	// 로그인한 유저가 자기 정보 수정하기
	@Transactional
	public int updateUser(UserDetailsVO userVO) {
		// SecurityContextHolder는 Spring Security로 로그인 시에만 발급되며 유저 정보가 수정되더라도 자동으로 갱신되지 않는다
		// 따라서 유저 정보가 수정될 때마다 SecurityContextHolder의 토큰을 갱신해주어야 한다
		
		// SecurityContextHolder에서 인증정보 가져오기
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// 가져온 인증정보에서 Principal(유저정보) 가져오기
		UserDetailsVO loginUserVO = (UserDetailsVO) auth.getPrincipal();
		
		// 기존의 유저정보에 form에서 입력받은 정보만 새로 세팅하기
		// 닉네임, 핸드폰, 생년, 생월, 생일
		loginUserVO.setNickname(userVO.getNickname());
		loginUserVO.setPhone(userVO.getPhone());
		
		loginUserVO.setYear(userVO.getYear());
		loginUserVO.setMonth(userVO.getMonth());
		loginUserVO.setDay(userVO.getDay());
		
		int ret = this.validUpdate(loginUserVO);
		// 유효성 검사 통과 실패 시 유효성 검사 결과값 바로 리턴
		if(ret < 0) return ret;
		
		// DB에 업데이트 된 유저정보 저장
		ret = userDao.updateUser(loginUserVO);
		if(ret > 0) {
			// 업데이트 성공시, 전역으로 쓰는 SecurityContextHolder(현재 로그인 된 사용자 정보)에 새로운 Authentication 업데이트 시켜주기
			Authentication newAuth = new UsernamePasswordAuthenticationToken(loginUserVO, auth.getCredentials(), auth.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(newAuth);
		}
		
		return ret;
	}
	
	protected int validUpdate(UserDetailsVO userVO) {
		String birth = String.format("%s-%s-%s", userVO.getYear(), userVO.getMonth(), userVO.getDay());
		int result = 0;
		
		if( !this.dateCheck(birth) ) {
			// 생년월일 유효성 검사
			result = -103;
		} else if(this.findByUsername(userVO.getUsername()) == null) {
			// DB에 존재하는 아이디인지 검사
			result = -200;
		}
		
		return result;
	}
	
	protected boolean dateCheck(String date) {
		// 년도 유효성 검사
		LocalDate localDate = LocalDate.now();
		String inputStrYear = date.split("-")[0];
		int inputYear = 0;
		try {
			inputYear = Integer.parseInt(inputStrYear);
		} catch (Exception e) {
			// 입력받은 생년을 int로 변환할 수 없으면 return false
			return false;
		}
		
		// 입력받은 생년 값이 올해보다 크거나 1900년보다 작으면 return false
		if(inputYear > localDate.getYear() || inputYear < 1900) {
			return false;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Transactional
	public boolean check_pw(String password) {
		String username = SpSec.username();
		String loginPW = userDao.findByUsername(username).getPassword();
		// 현재 로그인한 사용자의 암호화된 DB 비밀번호와 입력된 password와 비교
		boolean ret = bcryptEncoder.matches(password, loginPW);
		
		return ret;
	}
	
	@Transactional
	public int change_pw(String password, String re_password) {
		// 서버에서 유효성 검사
		// 1. 비밀번호, 비밀번호 확인에 아무것도 입력하지 않은 경우
		// 2. 비밀번호와 비밀번호 확인이 서로 다른 경우 아무 것도 하지 않음
		if(password.isEmpty() || re_password.isEmpty() || !password.equals(re_password)) {
			return 0;
		}
		
		// 유효성 검사 통과시 비밀번호 변경
		// 현재 사용자 이름 가져오기
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// DB에서 사용자 이름으로 VO 가져오기
		UserDetailsVO userVO = userDao.findByUsername(username);
		// 새로운 비밀번호 암호화하기
		userVO.setPassword(bcryptEncoder.encode(password));
		// DB에 username 기준으로 암호화 된 비밀번호 update하기
		return userDao.updatePw(userVO);
	}
	
	public List<UserDetailsVO> find_my_ids(UserDetailsVO userVO) {
		List<UserDetailsVO> userList = null;
		userList = userDao.findByEmail(userVO.getEmail());
		
		return userList;
	}
	
	@Transactional
	public byte find_my_pw(UserDetailsVO userVO) {
		UserDetailsVO dbUserVO = userDao.findByUsername(userVO.getUsername());
		byte res = 0;
		if(dbUserVO == null) {
			// DB에 아이디가 없으면 1 리턴
			res = 1;
		} else {
			// DB에 아이디가 있는 경우
			if(dbUserVO.getEmail().equals(userVO.getEmail()) ) {
				// db에 저장된 username의 email과 form에서 입력한 email이 같으면
				// 비밀번호 재설정 메일 보내기
				
				boolean bRes = false;
				try {
					bRes = mailSvc.send_new_pw_link(userVO.getUsername());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// 성공적으로 보냈으면 3 리턴, 메일 보내기 실패시 4 리턴
				res = (byte) (bRes ? 3 : 4);
			} else {
				// db에 저장된 username의 email과 form에서 입력한 email이 다르면 2 리턴
				res = 2;
			}
		}
		
		return res;
	}
	
	public int new_pw(String enc_username, String password, String re_password) {
		// userVO에는 암호화된 username, password만 담겨있다
		// 서버에서 유효성 검사
		// 1. 비밀번호, 비밀번호 확인에 아무것도 입력하지 않은 경우
		// 2. 비밀번호와 비밀번호 확인이 서로 다른 경우 아무 것도 하지 않음
		if(password.isEmpty() || re_password.isEmpty() || !password.equals(re_password)) {
			return 0;
		}
		
		// JASYPT로 username 복호화
		String username = "";
		try {
			username = PbeEncryptor.decrypt(enc_username);
		} catch (Exception e) {
			return -1;
		}
		
		// BCrypt로 password 암호화해서 VO에 저장
		UserDetailsVO userVO = UserDetailsVO.builder()
							.username(username)
							.password(bcryptEncoder.encode(password))
							.build();
		
		// DB에 새로운 비밀번호 저장
		return userDao.updatePw(userVO);
	}
	
	// 이메일 변경 메소드
	public String change_email_step1(String email) {
		// 이메일 유효성 검사
		if (email.isEmpty() || !email.matches("^([a-zA-Z0-9_.+-])+\\@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{1,6})+$")) return "-102";
		
		String ret = "";
		
		// UUID : 989bbfdd-ed54-430c-a20a-c348614e84be 가장 앞부분 대문자로 만들기
		String email_token = UUID.randomUUID().toString().split("-")[0].toUpperCase();
		
		boolean result = mailSvc.send_auth_code(email, email_token);
		// 메일 보내기 실패 시
		if(!result) ret = "fail";
		
		// 메일 보내기 성공 시
		// UUID 암호화해서 Controller -> View로 보내기
		String enc_email_token = PbeEncryptor.encrypt(email_token);
		ret = enc_email_token;
		
		return ret;
	}
	
	// 메일로 발송된 링크 클릭 시 실행할 메소드
	// 페이지의 암호화된 인증코드, 이메일로 발송된 인증코드를 비교하여
	// 정확히 입력했을 경우 SecurityContextHolder에 있는 현재 로그인 된 사용자의 아이디로 DB를 조회
	// 조회 결과로 받은 객체(VO)의 이메일을 세션에 들어있는 이메일로 변경 후 DB에 업데이트
	// 결과를 DB 업데이트 성공, DB 업데이트 실패, 인증코드 불일치 3가지로 나누어 결과 return
	@Transactional
	public byte change_email_step2(String enc_auth_code, String auth_code, String email) {
		byte ret = 0;
		
		// 페이지 인증코드 복호화
		String dec_code = PbeEncryptor.decrypt(enc_auth_code);
		// 현재 로그인한 유저 아이디 가져오고 form에서 받은 이메일 세팅하기
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		UserDetailsVO userVO = userDao.findByUsername(username);
		userVO.setEmail(email);
		
		// 인증코드를 정확히 입력했을 경우
		if(dec_code.equals(auth_code)) {
			int result = userDao.updateEmail(userVO);
			// DB update 성공 = 리턴 1, 실패 = 리턴 7
			if(result > 0) ret = 1; else ret = 7;
			
			// 현재 사용자의 권한이 ROLE_UNAUTH인 경우에만 실행
			// 권한 테이블에서 username으로 검색한 튜플 전부 삭제 후 ROLE_USER 권한 새로 추가하기
			if(ret == 1 && auth.getAuthorities().stream().filter(o -> o.getAuthority().equals("ROLE_UNAUTH")).findFirst().isPresent()) {
				result = authDao.delete(userVO.getUsername());
				if(result > 0) ret++; else ret = 7;
				List<AuthorityVO> authList = new ArrayList<>();
				authList.add(AuthorityVO.builder().username(userVO.getUsername()).authority("ROLE_USER").build());
				result = authDao.insert(authList);
				// 현재 사용자의 권한이 ROLE_UNAUTH인 경우, 권한 테이블까지 DB update 전부 성공 = 리턴 3
				// DB update 하나라도 실패 = 리턴 7
				if(result > 0) ret++; else ret = 7;
			}
		} else {
			// 정확하지 않은 인증코드 입력 = 리턴 4
			ret = 4;
		}
		
		/*
		 * 유저 테이블에 업데이트 성공 시 (현재 사용자의 권한이 ROLE_UNAUTH인 경우 권한 테이블까지 업데이트 성공 시)
		 * 트랜잭션은 COMMIT : DB 업데이트
		 * DB 업데이트 시 SecurityContextHolder의 유저 정보도 업데이트
		 * UsernamePasswordAuthenticationToken에 서버에서 사용할 유저정보(userVO), 비밀번호, 권한을 생성자 매개변수로 넣어 새로 만들고 Authentication으로 만들기
		 * 유저정보, Credential, 권한이 업데이트 된 새로운 Authentication을 SecurityContextHolder.getContext()에 setAuthentication() 메소드로 세팅해주기
		 */
		if(ret == 1 || ret == 3) {
			Authentication newAuth = new UsernamePasswordAuthenticationToken(userVO, auth.getCredentials(), auth.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(newAuth);
		}
		
		return ret;
	}
	
}
