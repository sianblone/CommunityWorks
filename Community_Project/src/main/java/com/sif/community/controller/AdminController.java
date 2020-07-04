package com.sif.community.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.CategoryVO;
import com.sif.community.model.UserDetailsVO;
import com.sif.community.service.board.BoardInfoService;
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
	private final BoardInfoService boardInfoSvc;
	
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
	public String mypage(UserDetailsVO userVO, String[] auth) {
		int ret = adminSvc.updateUserFromAdmin(userVO, auth);
		
		return "redirect:/admin/user_details/" + userVO.getUsername();
	}
	
	// 게시판 리스트
	@RequestMapping(value="/board_setting", method=RequestMethod.GET)
	public String board_setting(Model model) {
		model.addAttribute("ADMIN_BOARD_INFO_LIST", boardInfoSvc.selectAll(false));
		return "admin/board_setting";
	}
	
	// 게시판 생성 화면
	@RequestMapping(value="/board_setting_create_board", method=RequestMethod.GET)
	public String create_board() {
		return "admin/board_setting_create_board";
	}
	// 게시판 생성 POST 메소드
	// boardInfoVO에는 bi_name만 들어있다
	@RequestMapping(value="/board_setting_create_board", method=RequestMethod.POST)
	public String create_board(BoardInfoVO boardInfoVO) {
		adminSvc.insertBoard(boardInfoVO);
		
		return "redirect:/admin/board_setting";
	}
	// 게시판 수정 화면
	@RequestMapping(value="/board_setting_details", method=RequestMethod.GET)
	public String board_setting_details(long bi_id, Model model) {
		BoardInfoVO boardInfoVO = boardInfoSvc.findByBiId(bi_id);
		model.addAttribute("BOARD_INFO", boardInfoVO);
		
		return "admin/board_setting_details";
	}
	// 게시판 수정 POST 메소드
	@ResponseBody
	@RequestMapping(value="/board_setting_details", method=RequestMethod.POST)
	public int board_setting_details(BoardInfoVO boardInfoOptionVO, CategoryVO categoryOptionVO) {
		// boardInfoOptionVO에는 게시판 ID(bi_id)와 게시판 이름(bi_name)이 들어있다
		// categoryOptionVO에는 카테고리 목록이 들어있다
		log.debug("카테고리:{}", categoryOptionVO.toString());
		int result = adminSvc.updateBoard(boardInfoOptionVO, categoryOptionVO);
		
		return result;
	}
	// 게시판 삭제 POST 메소드
	@RequestMapping(value="/delete_board", method=RequestMethod.POST)
	public String delete_board(long bi_id) {
		adminSvc.deleteBoard(bi_id);
		
		return "redirect:/admin/board_setting";
	}
	
	// 게시판 순서 변경 POST 메소드
	@RequestMapping(value="/board_change_order", method=RequestMethod.POST)
	public String changeOrder(long bi_id, long order) {
		int result = adminSvc.changeOrder(bi_id, order);
		
		return "redirect:/admin/board_setting";
	}
	
	@ResponseBody
	@RequestMapping(value="/delete_category", method=RequestMethod.POST)
	public int delete_category(long cate_id) {
		
		return adminSvc.deleteCategory(cate_id);
	}

}