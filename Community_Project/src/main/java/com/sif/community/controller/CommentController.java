package com.sif.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sif.community.model.CommentVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.service.board.itf.CommentService;
import com.sif.community.service.board.itf.PaginationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value="/comment")
@Controller
@RequiredArgsConstructor
public class CommentController {
	
	@Qualifier(value = "cmtSvc")
	private final CommentService cmtSvc;
	
	@Qualifier(value = "pageSvc")
	private final PaginationService pageSvc;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(
			Model model,
			@RequestParam(value = "cmt_board_no", required = true) long cmt_board_no,
			Integer pageNo) {
		
		CommentVO commentVO = new CommentVO();
		commentVO.setCmt_board_no(cmt_board_no);
		
		// commentVO에는 게시글번호(cmt_board_no)만 들어있다
		this.selectAllByPage(model, commentVO, pageNo);
		
		return "comment/comment_list";
	}
	
	// 댓글 저장 시 사용할 메소드
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(CommentVO commentVO, Integer pageNo) {
		String render = "";
		int result = cmtSvc.save(commentVO);
		
		// 댓글 수정인 경우 DB에 cmt_no로 검색한 데이터가 없으면 에러페이지 보여주기
		if(result == -100) {
			render = "comment/cmt_error";
		} else {
			render = "redirect:/comment/list?cmt_board_no=" + commentVO.getCmt_board_no();
			if(pageNo != null) render += "&pageNo=" + pageNo;
		}
		
		return render;
	}
	
	// 댓글 삭제 시 사용할 메소드
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(long cmt_no, Integer pageNo, Model model) {
		String render = "";
		int result = cmtSvc.delete(cmt_no);
		
		if(result == -100 || result == -200) {
			render = "comment/cmt_error";
		} else {
			render = "redirect:/comment/list?cmt_board_no=" + cmt_no;
			if(pageNo != null) render += "&pageNo=" + pageNo;
		}
		return render;
	}
	
	// 페이지네이션
	private void selectAllByPage(Model model, CommentVO commentVO, Integer pageNo) {
		if(pageNo == null) pageNo = 1;
		
		log.debug("commentVO : {}", commentVO.toString());
		
		// 1. 페이징 할 곳에서 totalCount(총 데이터 수) 가져오기
		long totalCount = cmtSvc.countAll(commentVO);
		model.addAttribute("CMT_TOTAL", totalCount);
		log.debug("카운트 : {}", totalCount);
		
		// 2. 페이지네이션 정보 만들기
		PaginationVO pageVO = pageSvc.makePageInfoMiddle(totalCount, pageNo);
		log.debug("페이지 : {}", pageVO.toString());
		
		// 3. 페이지네이션 정보 view로 보내주기
		model.addAttribute("PAGE_DTO", pageVO);
		
		// 4. 페이지네이션 기본 쿼리 view로 보내주기
		String page_default_query = "&cmt_board_no=" + commentVO.getCmt_board_no();
		model.addAttribute("PAGE_DEFAULT_QUERY", page_default_query);
		
		// 게시판 내용
		List<CommentVO> commentList = cmtSvc.selectAllByPage(commentVO, pageVO);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		for(CommentVO cmtVO : commentList) {
			// 현재 로그인한 사용자 아이디와 작성자 아이디가 같거나, 로그인한 사용자 권한이 ADMIN일 때 글 수정,삭제 가능
			if( auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) cmtVO.setViewerAdmin(true);
			if( cmtVO.getCmt_writer().equals(auth.getName()) ) cmtVO.setViewerWriter(true);
			
		}
		model.addAttribute("CMT_LIST", commentList);
	}
	
}
