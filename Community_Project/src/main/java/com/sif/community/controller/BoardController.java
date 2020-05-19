package com.sif.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.service.board.BoardService;
import com.sif.community.service.board.PaginationService;

@RequestMapping(value = "/board")
@Controller
public class BoardController {
	
	@Autowired
	@Qualifier(value = "pageSvc")
	PaginationService pageSvc;
	
	@Autowired
	@Qualifier(value = "boardSvc")
	BoardService boardSvc;
	
	// 게시판 컨트롤러
	// 리스트(GET) + 게시판 이름 + 검색 + 페이지 / 상세보기(GET) / 저장(GET: 화면 보여주기, POST: 입력 받기) / 삭제(POST)
	
	// 리스트 + 게시판 이름 + 검색 + 페이지 메소드
	// 검색 값과 현재 페이지로 페이지네이션 select하기
	@RequestMapping(value="", method=RequestMethod.GET)
	public String list(Model model,
			@RequestParam(value = "boardName", required = false, defaultValue = "") String boardName,
			@RequestParam(value = "searchTxt", required = false, defaultValue = "") String searchTxt,
			@RequestParam(value="currPage", required=false, defaultValue="1") int currPage) {
		
		this.selectAllByPage(model, boardName, searchTxt, currPage);
		
		return "board/list";
	}
	
	// 상세보기 메소드
	// id값으로 게시글 보여주기
	@RequestMapping(value="/details", method=RequestMethod.GET)
	public String details(Long id) {
		
		return "board/details";
	}
	
	// 저장 메소드
	// id값을 받지 않았으면, 값이 입력되지 않은 저장화면 보여주기
	// id값을 받았으면, 현재 사용자와 DB 게시글id 작성자를 검색하여, 일치하면 값 채워서 저장화면 보여주기
	// 현재 사용자와 DB의 게시글id 작성자가 다르면 오류 페이지로 보내기
	@RequestMapping(value="/save", method=RequestMethod.GET)
	public String save(Model model) {
		
		return "board/save";
	}
	
	// form에서 저장버튼 클릭 시 사용할 메소드
	// 현재 사용자와 DB의 게시글id 작성자가 같은지 다시 확인 후
	// form에서 입력받은 값으로 DB에 저장하기(INSERT 또는 UPDATE)
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save() {
		
		return "redirect:/board";
	}
	
	// 게시물 삭제버튼 클릭 시 사용할 메소드
	// 게시글 deleted 칼럼 값 1로 바꿔주기
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(Long id) {
		
		return "redirect:/board";
	}
	
	// 페이지네이션
	private void selectAllByPage(Model model, String boardName, String searchTxt, int currPage) {
		long totalCount = boardSvc.countAll();
		PaginationVO pageVO = pageSvc.makePageInfo(totalCount, currPage);
		model.addAttribute("PAGE_DTO", pageVO);
		
		List<BoardVO> boardList = boardSvc.selectAllByPage(boardName, searchTxt, pageVO);
		model.addAttribute("BOARD_LIST", boardList);
	}

}
