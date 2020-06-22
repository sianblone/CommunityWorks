package com.sif.community.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sif.community.model.CommentVO;
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
	private PaginationService pageSvc;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(
			Model model,
			@RequestParam(value = "board_no", required = true) long cmt_board_no,
			Integer currPage) {
		
		CommentVO commentVO = new CommentVO();
		commentVO.setCmt_board_no(cmt_board_no);
		
		// commentVO에는 게시글번호(cmt_board_no)가 들어있다
		this.selectAllByPage(model, commentVO, currPage);
		return "comment/comment_list";
	}
	
	// 댓글 저장 시 사용할 메소드
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(CommentVO commentVO, Integer currPage) {
				
		cmtSvc.save(commentVO);
		String redirect = "redirect:/comment/list?board_no=" + commentVO.getCmt_board_no();
		if(currPage != null) redirect += "&currPage=" + currPage;
		return redirect;
	}
	/*
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String cmt_no, String board_no, Model model) {
		int ret = cmtSvc.delete(Long.valueOf(cmt_no));
		model.addAttribute("board_no",board_no);
		return "redirect:/comment/list";
	}
	
	@RequestMapping(value="/repl",method=RequestMethod.POST)
	public String repl(CommentVO cmtVO, Model model) {
		model.addAttribute("CMT",cmtVO);
		return "comment/comment_write";
	}
	
	// 페이지네이션
		private void selectAllByPage(Model model, CommentVO commentVO, Integer currPage) {
		if(currPage == null) currPage = 1;
		
		log.debug("commentVO : {}", commentVO.toString());
		// 1. 페이징 할 곳에서 totalCount(총 데이터 수) 가져오기
		long totalCount = cmtSvc.countAll(commentVO);
		log.debug("카운트 : {}", totalCount);
		// 2. 페이지네이션 정보 만들기
		PaginationVO pageVO = pageSvc.makePageInfoMiddle(totalCount, currPage);
		log.debug("페이지 : {}", pageVO.toString());
		// 3. 페이지네이션 정보 view로 보내주기
		model.addAttribute("PAGE_DTO", pageVO);
		// 4. 페이지네이션 기본 쿼리 view로 보내주기
		model.addAttribute("PAGE_DEFAULT_QUERY", null);
		
		// 게시판 내용
		List<CommentVO> commentList = cmtSvc.selectAllByPage(commentVO, pageVO);
		model.addAttribute("BOARD_LIST", commentList);
	}
	*/
}
