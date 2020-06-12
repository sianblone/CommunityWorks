package com.sif.community.service.user;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sif.community.dao.AuthoritiesDao;
import com.sif.community.dao.UserDao;
import com.sif.community.model.AuthorityVO;
import com.sif.community.model.UserDetailsVO;
import com.sif.util.PbeEncryptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JoinService {
	
	private final UserDao userDao;
	private final AuthoritiesDao authDao;
	private final MailSendService mailSvc;
	private final UserService userSvc;
	private final PasswordEncoder bcryptEncoder;
	
	/**
	 * @since 2020-05-11
	 * @author me
	 * email 인증이 되지 않은 상태로 DB에 저장하기
	 * 1. userVO를 파라미터로 받아서 비밀번호 암호화 후 유저 테이블에 저장
	 * 2. authorities 테이블에 ROLE_UNAUTH로 저장
	 * 이후 이메일 인증 링크를 클릭하면 role 정보를 ROLE_USER로 설정한다
	 * @param userVO
	 * @param day 
	 * @param month 
	 * @param year 
	 * @return
	 */
	// 두 개 이상의 쿼리 => @Transactional
	@Transactional
	public int insert(UserDetailsVO userVO) {
		
		int ret = this.valid_join(userVO);
		// 유효성 검사 통과 실패 시 유효성 검사 결과값 바로 리턴
		if(ret < 0) return ret;
		
		// 비밀번호 암호화하기
		String encPW = bcryptEncoder.encode(userVO.getPassword());
		userVO.setPassword(encPW);
		
		// 닉네임이 비어있다면 아이디와 똑같이 설정하기
		if(userVO.getNickname().isEmpty()) userVO.setNickname(userVO.getUsername());
		
		// DB tbl_users 테이블 INSERT
		ret = userDao.insert(userVO);
		
		// DB authorities 테이블 INSERT
		List<AuthorityVO> authList = new ArrayList<>();
		authList.add(AuthorityVO.builder().username(userVO.getUsername()).authority("ROLE_UNAUTH").build());
		ret += authDao.insert(authList);
		
		// 인증메일 발송
		try {
			mailSvc.send_join_auth_link(userVO);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = -300;
		}
		
		return ret;
	}
	
	protected int valid_join(UserDetailsVO userVO) {
		String password = userVO.getPassword();
		String re_password = userVO.getRe_password();
		String email = userVO.getEmail();
		String birth = String.format("%s-%s-%s", userVO.getYear(), userVO.getMonth(), userVO.getDay());
		int result = 0;
		
		// 4~12자 아이디 유효성 검사
		if( !userVO.getUsername().matches("^[a-zA-Z0-9]{4,12}$") ) {
			result = -100;
		} else if (password.isEmpty() || re_password.isEmpty() || !password.equals(re_password)) {
			// 비밀번호 확인 유효성 검사
			result = -101;
		} else if (email.isEmpty() || !email.matches("^([a-zA-Z0-9_.+-])+\\@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{1,6})+$")) {
			// 이메일 유효성 검사
			result = -102;
		} else if( !this.dateCheck(birth) ) {
			// 생년월일 유효성 검사
			result = -103;
		} else if(userSvc.findByUsername(userVO.getUsername()) != null) {
			// 이미 DB에 있는 아이디인지 검사
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
		
		SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public int test_insert(UserDetailsVO userVO, String authority) {
		int ret = this.valid_join(userVO);
		if(ret < 0) return ret;
		
		String encPW = bcryptEncoder.encode(userVO.getPassword());
		userVO.setPassword(encPW);
		if(userVO.getNickname().isEmpty()) userVO.setNickname(userVO.getUsername());
		ret = userDao.insert(userVO);
		List<AuthorityVO> authList = new ArrayList<>();
		authList.add(AuthorityVO.builder().username(userVO.getUsername()).authority(authority).build());
		ret += authDao.insert(authList);
		return ret;
	}
	
	// 회원가입 이메일 링크 클릭 시 인증 메소드
	@Transactional
	public byte email_link_auth(String username, String email) {
		byte ret = 0;
		// 아이디, 이메일 복호화
		String plainUsername = PbeEncryptor.decrypt(username);
		String plainEmail = PbeEncryptor.decrypt(email);
		
		UserDetailsVO userVO = userDao.findByUsername(plainUsername);
		
		// 1. DB의 유저 테이블에 username이 없으면 아무 일도 하지 않음
		if(userVO == null) {
			ret = 2;
		} else if(authDao.findByUsername(plainUsername).stream()
				.filter(o -> o.getAuthority().equals("ROLE_UNAUTH")).findFirst().isPresent()) {
			// 2. DB의 유저 테이블에 username이 있고, 해당 유저의 권한 테이블에 ROLE_UNAUTH가 있는 경우
			// 2-1. 링크의 이메일 복호화 값 == DB 유저 테이블의 이메일 값이면 권한 업데이트
			if(plainEmail.equalsIgnoreCase(userVO.getEmail())) {
				// 현재 권한 테이블에서 username으로 검색한 튜플 전부 삭제 후 ROLE_USER 권한 새로 추가
				authDao.delete(userVO.getUsername());
				List<AuthorityVO> authList = new ArrayList<>();
				authList.add(AuthorityVO.builder().username(userVO.getUsername()).authority("ROLE_USER").build());
				authDao.insert(authList);
				ret = 1;
			} else {
				// 2-2. 링크의 이메일 복호화 값 != DB 유저 테이블의 이메일 값이면 잘못된 요청 페이지로 보내기
				ret = 3;
			}
		} else {
			// 3. DB의 유저 테이블에 username이 있지만 권한 중 ROLE_UNAUTH가 없는 경우 아무 일도 하지 않음
			ret = 4;
		}
		
		return ret;
	}

}
