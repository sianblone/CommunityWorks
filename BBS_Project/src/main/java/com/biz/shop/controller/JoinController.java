package com.biz.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biz.shop.model.UserDetailsVO;
import com.biz.shop.service.JoinService;
import com.biz.shop.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 기반 회원가입 및 Email 인증 프로젝트 메인 컨트롤러
 * @since 2020-05-11
 * @author sianblone
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value="/join")
@Controller
public class JoinController {
	
	private final UserService userSvc;
	private final JoinService joinSvc;
	
	/**
	 * 회원가입 화면 보여주기
	 * @since 2020-05-11
	 * @param userVO
	 * @param model
	 * @return
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String join(UserDetailsVO userVO, Model model) {
		return "user/join";
	}
	
	/**
	 * 회원가입 화면에서 username, password 및 정보 입력 후 회원가입 버튼 클릭 시
	 * userVO에 데이터를 받아서(username, password) DB에 인증되지 않은 유저 상태로 저장하고
	 * 메일로 인증 링크 발송하기
	 * @since 2020-05-11
	 * @param userVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public int join_next(UserDetailsVO userVO) {
		int ret = joinSvc.insert(userVO);
		
		return ret;
	}
	
	/**
	 * @since 2020-05-11
	 * 이메일 링크 클릭 시 인증
	 * @param userVO
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/email-auth", method=RequestMethod.GET)
	public String email_valid(UserDetailsVO userVO, Model model) {
		byte ret = joinSvc.email_link_auth(userVO.getUsername(), userVO.getEmail());
		
		if (ret == 1) {
			model.addAttribute("EMAIL_AUTH", true);
			return "redirect:/";
		} else {
			return "user/email_auth_fail";
		}
	}
	
	// 회원가입 시 아이디 중복검사
	@ResponseBody
	@RequestMapping(value="/idcheck", method=RequestMethod.GET)
	public boolean idcheck(String username) {
		boolean ret = false;
		UserDetailsVO userDetailsVO = userSvc.findByUsername(username);
		
		// 이미 DB에 회원정보가 저장되어있는 경우 체크하기
		// null이 아니면 && 뒤의 if문 검사
		// 먼저 null 체크를 해서 NullPointerException 방지
		if(userDetailsVO != null && userDetailsVO.getUsername().equalsIgnoreCase(username)) {
			ret = true;
		}
		return ret;
	}
	
}
