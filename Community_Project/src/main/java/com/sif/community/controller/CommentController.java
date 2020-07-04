package com.sif.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sif.community.model.CommentVO;
import com.sif.community.model.PaginationDTO;
import com.sif.community.service.board.itf.BoardService;
import com.sif.community.service.board.itf.CommentService;
import com.sif.community.service.board.itf.PaginationService;
import com.sif.util.SpSec;

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
	
	@Qualifier(value = "boardSvc")
	private final BoardService boardSvc;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(
			Model model,
			@RequestParam(value = "cmt_board_no", required = true) long cmt_board_no,
			Integer pageNo) {
		
		CommentVO cmtOptionVO = new CommentVO();
		cmtOptionVO.setCmt_board_no(cmt_board_no);
		
		long cmt_bi_id = boardSvc.findByBoardNo(cmt_board_no).getBoard_info();
		cmtOptionVO.setCmt_bi_id(cmt_bi_id);
		
		// commentVO에는 cmt_board_no, cmt_bi_id가 들어있다
		this.selectAllByPage(model, cmtOptionVO, pageNo);
		
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
	// cmtOptionVO에는 cmt_board_no, cmt_bi_id가 들어있다
	private void selectAllByPage(Model model, CommentVO cmtOptionVO, Integer pageNo) {
		if(pageNo == null) pageNo = 1;
		
		// 1. 페이징 할 곳에서 dataCount(총 데이터 수) 가져오기
		long dataCount = cmtSvc.countAll(cmtOptionVO);
		// 1-2. 전체 댓글 수 view로 보내주기
		model.addAttribute("CMT_TOTAL", dataCount);
		log.debug("카운트 : {}", dataCount);
		
		// 2. 페이지네이션 테이블 댓글 정보 가져오기
		PaginationDTO pageDTO = pageSvc.findByBoardInfo(cmtOptionVO.getCmt_bi_id(), "comment");
		
		// 3. 페이지네이션 정보 만들기
		pageDTO = pageSvc.makePageInfoMiddle(dataCount, pageDTO, pageNo, true);
		log.debug("페이지 : {}", pageDTO.toString());
		
		// 4. 댓글 페이지네이션 정보 view로 보내주기
		model.addAttribute("PAGE_DTO", pageDTO);
		
		// 5. 댓글 페이지네이션 기본 쿼리 view로 보내주기
		String page_default_query = "&cmt_board_no=" + cmtOptionVO.getCmt_board_no();
		model.addAttribute("PAGE_DEFAULT_QUERY", page_default_query);
		
		// 댓글 내용 view로 보내주기
		List<CommentVO> commentList = cmtSvc.selectAllByPage(cmtOptionVO, pageDTO);
		
		for(CommentVO cmtVO : commentList) {
			// 로그인한 사용자가 작성자거나 관리자일 때 댓글 수정, 삭제 가능하도록 설정
			if( cmtVO.getCmt_writer().equals(SpSec.username()) ) cmtVO.setViewerWriter(true);
			if( SpSec.isAdmin() ) cmtVO.setViewerAdmin(true);
		}
		
		model.addAttribute("CMT_LIST", commentList);
	}
	
}