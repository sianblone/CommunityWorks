package com.sif.community.service.user.itf;

import java.io.UnsupportedEncodingException;

import com.sif.community.model.UserDetailsVO;

public interface SendService {

	boolean sendMail(String to_email, String subject, String content);

	/**
	 * 회원가입한 사용자에게 인증 email 발송
	 * username을 암호화시켜서 인증을 수행할 수 있는 링크를 email 본문에 작성하여 전송
	 * @param userVO
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	boolean send_join_auth_link(UserDetailsVO userVO) throws UnsupportedEncodingException;

	boolean send_new_pw_link(String username) throws UnsupportedEncodingException;

	boolean send_auth_code(String to_email, String email_token);

}