package com.sif.community.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.service.board.BoardInfoService;

public class BoardInfoInterceptor implements HandlerInterceptor {

	@Autowired
	private BoardInfoService boardInfoSvc;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		List<BoardInfoVO> boardInfoList = boardInfoSvc.selectAll(true);
		request.setAttribute("BOARD_INFO_LIST", boardInfoList);

		return true;
	}
}
