package com.sif.community.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.CategoryVO;
import com.sif.community.model.UserDetailsVO;
import com.sif.community.service.board.itf.BoardService;
import com.sif.community.service.user.AdminService;
import com.sif.community.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value="/admin")
@Controller
public class AdminController {
	
	private final UserService userSvc;
	private final AdminService adminSvc;
	private final BoardService boardSvc;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public String admin() {
		return "admin/admin_main";
	}
	
	@RequestMapping(value="/user_list", method=RequestMethod.GET)
	public String user_list(Model model) {
		List<UserDetailsVO> userList = userSvc.selectAll();
		
		model.addAttribute("USER_LIST", userList);
		return "admin/user_list";
	}
	
	@RequestMapping(value="/user_details/{username}", method=RequestMethod.GET)
	public String user_detail_view(@PathVariable("username")String username, Model model) {
		UserDetailsVO userVO = userSvc.findByUsername(username);
		model.addAttribute("USER_VO", userVO);
		return "admin/user_details";
	}
	
	@RequestMapping(value="/user_details", method=RequestMethod.POST)
	public String mypage(UserDetailsVO userVO, String[] auth, Model model) {
		int ret = adminSvc.update_user_from_admin(userVO, auth);
		
		return "redirect:/admin/user_details/" + userVO.getUsername();
	}
	
	@RequestMapping(value="/board_setting", method=RequestMethod.GET)
	public String board_setting(Model model) {
		model.addAttribute("BOARD_INFO_LIST", boardSvc.selectAllBoardInfo());
		return "admin/board_setting";
	}
	
	@RequestMapping(value="/board_setting_details", method=RequestMethod.GET)
	public String board_setting_details(long board_info, Model model) {
		BoardInfoVO boardInfoVO = boardSvc.findByBoardInfo(board_info);
		model.addAttribute("BOARD_INFO", boardInfoVO);
		
		return "admin/board_setting_details";
	}
	
	@RequestMapping(value="/board_setting_details", method=RequestMethod.POST)
	public String board_setting_details(BoardInfoVO boardInfoOptionVO, CategoryVO categoryOptionVO, Model model) {
		// boardInfoOptionVO에는 게시판 ID(bi_id)와 게시판 이름(bi_name)이 들어있다
		// categoryOptionVO에는 카테고리 목록이 들어있다
		log.debug("카테고리:{}", categoryOptionVO.toString());
		adminSvc.update_tbl_board_info(boardInfoOptionVO, categoryOptionVO);
		
		return "redirect:/admin/board_setting";
	}
	
	@RequestMapping(value="/board_setting_create_board", method=RequestMethod.GET)
	public String create_board() {
		return "admin/board_setting_create_board";
	}
	
	@RequestMapping(value="/board_setting_create_board", method=RequestMethod.POST)
	public String create_board(BoardInfoVO boardInfoVO, Model model) {
		adminSvc.create_board(boardInfoVO);
		
		return "redirect:/admin/board_setting";
	}

}