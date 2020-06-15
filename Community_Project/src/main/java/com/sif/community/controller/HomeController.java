package com.sif.community.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.service.board.itf.BoardService;

@Controller
public class HomeController {
	@Autowired
	protected BoardService boardSvc;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(@RequestParam(value = "EMAIL_AUTH", required = false) boolean email_auth, Model model) {
		model.addAttribute("EMAIL_AUTH", email_auth);
		
		List<Long> boardIdList = new ArrayList<>();
		boardIdList.add((long) 1);
		boardIdList.add((long) 2);
		boardIdList.add((long) 3);
		
		return "home";
	}
	
}
