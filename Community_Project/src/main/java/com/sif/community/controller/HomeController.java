package com.sif.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.service.board.BoardInfoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	@Autowired
	protected BoardInfoService boardInfoSvc;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(
			@RequestParam(value = "EMAIL_AUTH", required = false) boolean email_auth,
			Model model) {
		
		List<BoardInfoVO> boardInfoList = boardInfoSvc.selectMainPage();
		model.addAttribute("BOARD_INFO_LIST", boardInfoList);
		model.addAttribute("EMAIL_AUTH", email_auth);
		
		return "home";
	}
	
}
