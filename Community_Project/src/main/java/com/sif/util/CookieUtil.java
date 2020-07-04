package com.sif.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sif.community.dao.BoardDao;
import com.sif.community.model.BoardVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieUtil {
	
	public static int boardCookie(HttpServletRequest request, HttpServletResponse response, BoardVO boardOptionVO, BoardDao boardDao) {
		return boardCookie(request, response, boardOptionVO, boardDao, null);
	}
	
	public static int boardCookie(HttpServletRequest request, HttpServletResponse response, BoardVO boardOptionVO, BoardDao boardDao, String addName) {
		int result = 0;
		
		String username = SpSec.username();
		// 체크할 쿠키 이름 생성
		String cookieName = "cmnt200630_boardNo_" + boardOptionVO.getBoard_no();
		String cookieNameUsername = "cmnt200630_boardNo_" + boardOptionVO.getBoard_no() + "_username_" + username;
		// 매개변수 addName에 값이 있을 경우 뒤에 _addName 추가
		if(addName != null) {
			cookieName += "_" + addName;
			cookieNameUsername += "_" + addName;
		}
		
		boolean isLoggedIn = SpSec.isLoggedIn();
		log.debug("isLoggedIn === " + isLoggedIn);
		boolean isAlreadyChecked = false;
		Cookie[] reqCookies = request.getCookies();
		// 같은 이름의 쿠키가 있으면 조회수/추천수 증가시키지 않음
		for(Cookie cookie : reqCookies) {
			// 1. 로그인 된 상태인지 확인
			if(isLoggedIn) {
				// 1-1. cmnt200630_boardNo_글번호_username_아이디(_recommend) 체크, 쿠키가 있으면 조회수/추천수 증가하지 않음				
				if( cookie.getName().equals(cookieNameUsername) ) {
					isAlreadyChecked = true;
					break;
				}
			} else {
				// 1-2. 로그인 된 상태가 아니라면 cmnt200630_boardNo_글번호(_recommend) 체크, 쿠키가 있으면 조회수/추천수 증가하지 않음
				if( cookie.getName().equals(cookieName) ) {
					isAlreadyChecked = true;
					break;
				}
			}
		}
		
		// 2. cmnt200630_boardNo_글번호(_recommend) 체크, 쿠키가 없으면 DB 데이터에 조회수/추천수 1 증가시키기
		if(isAlreadyChecked == false) {
			// 매개변수 addName 값이 null이면 조회수 증가, 값이 recommend면 추천수 증가
			if(addName == null) {
				result = boardDao.updateBoardCount(boardOptionVO);
			} else if(addName.equals("recommend")) {
				result = boardDao.updateBoardRecommend(boardOptionVO);
			}
			// 2-1. cmnt200630_boardNo_글번호(_recommend) 쿠키 생성 후 response에 쿠키 추가
			Cookie newCookie = new Cookie(cookieName, null);
			newCookie.setMaxAge(60*60*24*7);
			response.addCookie(newCookie);
			
			// 2-2. 로그인 된 상태라면 추가로 cmnt200630_boardNo_글번호_username_아이디(_recommend) 쿠키 생성 후 response에 쿠키 추가
			if(isLoggedIn) {
				newCookie = new Cookie(cookieNameUsername, null);
				newCookie.setMaxAge(60*60*24*7);
				response.addCookie(newCookie);
			}
		}
		
		return result;
	}
	
}
