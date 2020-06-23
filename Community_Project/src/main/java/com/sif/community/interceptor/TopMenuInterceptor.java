package com.sif.community.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sif.community.dao.TopMenuDao;
import com.sif.community.model.BoardInfoVO;

public class TopMenuInterceptor implements HandlerInterceptor {

	@Autowired
	private TopMenuDao topMenuDao;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		List<BoardInfoVO> topMenuList = topMenuDao.getTopMenuList();
		request.setAttribute("topMenuList", topMenuList);

		return true;
	}
}
