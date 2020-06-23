package com.sif.community.service.secure;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class LoginSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {
	
	public LoginSuccessHandlerImpl(String defaultTargetUrl) {
		setDefaultTargetUrl(defaultTargetUrl);
	}
	
	// 로그인 성공 시 실행할 메소드
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		/*
		RequestCache requestCache = new HttpSessionRequestCache();

		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String targetUrl = savedRequest.getRedirectUrl();

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
		*/
		// 로그인 이전 페이지 가져오기
		HttpSession session = request.getSession(false);
		// 로그인 이전 페이지가 있는 경우
		if(session != null) {
			String redirectURL = (String) session.getAttribute("url_prev_page");
			// 로그인 이전 페이지의 URL이 있는 경우
			if(redirectURL != null) {
				// request에서 임시로 만들어놓은 url_prev_page 객체 삭제 후 페이지 이동
				session.removeAttribute("url_prev_page");
				getRedirectStrategy().sendRedirect(request, response, redirectURL);
			} else {
				// 로그인 이전 페이지의 URL이 없는 경우 defaultTargetUrl 페이지로 이동
				super.onAuthenticationSuccess(request, response, authentication);
			}
		} else {
			// 로그인 이전 페이지가 없는 경우 defaultTargetUrl 페이지로 이동
			super.onAuthenticationSuccess(request, response, authentication);
		}
		
	}
	
	
	
}
