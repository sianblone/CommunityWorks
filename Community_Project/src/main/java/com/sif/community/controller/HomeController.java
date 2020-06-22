package com.sif.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.service.board.itf.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	@Autowired
	protected BoardService boardSvc;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(
			@RequestParam(value = "limit", required = false, defaultValue = "5")int limit,
			@RequestParam(value = "EMAIL_AUTH", required = false) boolean email_auth,
			Model model) {
		
		List<BoardInfoVO> boardInfoList = boardSvc.selectMainPage(limit);
		log.debug("Limit : {}", limit);
		log.debug("메인페이지 : {}", boardInfoList.toString());
		log.debug("메인페이지 게시글 : {}", boardInfoList.get(0).getBi_board_list().toString());
		log.debug("메인페이지 게시글 수 : {}", boardInfoList.get(0).getBi_board_list().size());
		model.addAttribute("BOARD_INFO_LIST", boardInfoList);
		
		model.addAttribute("EMAIL_AUTH", email_auth);
		
		return "home";
	}
	
}
