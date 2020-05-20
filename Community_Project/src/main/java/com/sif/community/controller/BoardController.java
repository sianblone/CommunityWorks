package com.sif.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.service.board.BoardService;
import com.sif.community.service.board.PaginationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value = "/board")
@Controller
public class BoardController {
	
	@Autowired
	@Qualifier(value = "pageSvc")
	private PaginationService pageSvc;
	
	@Autowired
	@Qualifier(value = "boardSvc")
	private BoardService boardSvc;
	
	// 게시판 컨트롤러
	// 리스트(GET) + 게시판 이름 + 검색 + 페이지 / 상세보기(GET) / 저장(GET: 화면 보여주기, POST: 입력 받기) / 삭제(POST)
	
	// 리스트 + 게시판 이름 + 검색 + 페이지 메소드
	// 검색 값과 현재 페이지로 페이지네이션 select하기
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(Model model,
					BoardVO boardVO,
					@RequestParam(value="currPage", required=false, defaultValue="1") int currPage) {
		
		if(boardVO.getSearch_type() == null) boardVO.setSearch_type("");
		if(boardVO.getSearch_txt() == null) boardVO.setSearch_txt("");
		
		// boardVO에는 게시판이름, search_type, search_txt가 들어있다
		this.selectAllByPage(model, boardVO, currPage);
		
		model.addAttribute("BOARD_NAME", boardVO.getBoard_name());
		
		return "board/list";
	}
	
	// 상세보기 메소드
	// id값으로 게시글 보여주기
	@RequestMapping(value="/details", method=RequestMethod.GET)
	public String details(BoardVO boardOptionVO, Model model) {
		BoardVO boardVO = boardSvc.findByNo(boardOptionVO.getBoard_no());
		model.addAttribute("BOARD_VO",boardVO);
		return "board/details";
	}
	
	// 저장 메소드
	// id값을 받지 않았으면, 값이 입력되지 않은 저장화면 보여주기
	// id값을 받았으면, 현재 사용자와 DB 게시글id 작성자를 검색하여, 일치하면 값 채워서 저장화면 보여주기
	// 현재 사용자와 DB의 게시글id 작성자가 다르면 오류 페이지로 보내기
	@RequestMapping(value="/save", method=RequestMethod.GET)
	public String save(Long no, Model model) {
		String render = "";
		if(no != null) {
			// 게시글번호를 쿼리에서 받은 경우
			
			BoardVO boardVO = boardSvc.findByNo(no);
			
			// 게시글번호로 찾은 데이터가 DB에 있으면(이미 있는 글이면) 수정하기
			if(boardVO != null) {
				// 현재 로그인한 사용자와 게시글 작성자가 다르면 수정 불가
				String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
				if(!loginName.equals(boardVO.getBoard_writer())) {
					render = "board/error";
				} else {
					// 로그인한 사용자와 게시글 작성자가 같으면 수정정보 view로 보내기
					model.addAttribute("BOARD_VO",boardVO);
					render = "board/save";
				}
			} else {
				// 게시글번호로 찾은 데이터가 DB에 없으면 에러페이지로
				render = "board/error";
			}
		} else {
			// 게시글 번호를 쿼리로 받지 않은 경우(신규작성 글)
			render = "board/save";
		}
		
		return render;
	}
	
	// form에서 저장버튼 클릭 시 사용할 메소드
	// 현재 사용자와 DB의 게시글id 작성자가 같은지 다시 확인 후
	// form에서 입력받은 값으로 DB에 저장하기(INSERT 또는 UPDATE)
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(BoardVO boardVO) {
		boardSvc.save(boardVO);
		return "redirect:/board/list?board_name=" + boardVO.getBoard_name();
	}
	
	// 게시물 삭제버튼 클릭 시 사용할 메소드
	// 게시글 deleted 칼럼 값 1로 바꿔주기
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(BoardVO boardVO) {
		long board_no = boardVO.getBoard_no();
		boardSvc.delete(board_no);
		String board_name = boardSvc.findByNo(board_no).getBoard_name();
		return "redirect:/board/list?board_name=" + board_name;
	}
	
	// 페이지네이션
	private void selectAllByPage(Model model, BoardVO boardVO, int currPage) {
		long totalCount = boardSvc.countAll(boardVO);
		PaginationVO pageVO = pageSvc.makePageInfo(totalCount, currPage);
		model.addAttribute("PAGE_DTO", pageVO);
		
		List<BoardVO> boardList = boardSvc.selectAllByPage(boardVO, pageVO);
		log.debug(boardList.toString());
		model.addAttribute("BOARD_LIST", boardList);
	}

}
