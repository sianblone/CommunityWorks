package com.sif.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.service.board.BoardService;
import com.sif.community.service.board.FileService;
import com.sif.community.service.board.PaginationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value = "/board")
@Controller
public class BoardController {
		
	@Autowired
	private FileService fileService;
	
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
					Integer currPage) {
		
		if(boardVO.getSearch_type() == null) boardVO.setSearch_type("");
		if(boardVO.getSearch_txt() == null) boardVO.setSearch_txt("");
		
		// boardVO에는 게시판이름, search_type, search_txt가 들어있다
		this.selectAllByPage(model, boardVO, currPage);
		
		return "board/list";
	}
	
	// 상세보기 메소드
	// id값으로 게시글 보여주기
	@RequestMapping(value="/details", method=RequestMethod.GET)
	public String details(BoardVO boardOptionVO, Model model) {
		BoardVO boardVO = boardSvc.findByNo(boardOptionVO.getBoard_no());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		// 현재 로그인한 사용자가 관리자가 아닐 때 delete 값이 1인 게시물 열람 불가
		if(!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) && boardVO.getBoard_delete() == 1) {
			return "board/error";
		}
		
		boolean isWriter = false;
		// 현재 로그인한 사용자 아이디와 작성자 아이디가 같거나, 로그인한 사용자 권한이 ADMIN일 때 글 수정,삭제 가능
		if(boardVO.getBoard_writer().equals(auth.getName()) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			isWriter = true;
		}
		
		model.addAttribute("BOARD_VO",boardVO);
		model.addAttribute("IS_WRITER", isWriter);
		return "board/details";
	}
	
	// 저장 메소드
	// id값을 받지 않았으면, 값이 입력되지 않은 저장화면 보여주기
	// id값을 받았으면, 현재 사용자와 DB 게시글id 작성자를 검색하여, 일치하면 값 채워서 저장화면 보여주기
	// 현재 사용자와 DB의 게시글id 작성자가 다르면 오류 페이지로 보내기
	@RequestMapping(value="/save", method=RequestMethod.GET)
	public String save(BoardVO boardOptionVO, Model model) {
		String render = boardSvc.saveView(boardOptionVO, model);
		
		return render;
	}
	
	// form에서 저장버튼 클릭 시 사용할 메소드
	// 현재 사용자와 DB의 게시글id 작성자가 같은지 다시 확인 후
	// form에서 입력받은 값으로 DB에 저장하기(INSERT 또는 UPDATE)
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(BoardVO boardVO, Integer currPage) {
		boardSvc.save(boardVO);
		String redirect = "redirect:/board/list?board_name=" + boardVO.getBoard_name();
		if(currPage != null) redirect += "&currPage=" + currPage;
		return redirect;
	}
	
	// 게시물 삭제버튼 클릭 시 사용할 메소드
	// 게시글 deleted 칼럼 값 1로 바꿔주기
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public String delete(long board_no, Integer currPage) {
		String render = boardSvc.delete(board_no, currPage);
		return render;
	}
	
	// 페이지네이션
	private void selectAllByPage(Model model, BoardVO boardVO, Integer currPage) {
		if(currPage == null) currPage = 1;
		long totalCount = boardSvc.countAll(boardVO);
		PaginationVO pageVO = pageSvc.makePageInfo(totalCount, currPage);
		model.addAttribute("PAGE_DTO", pageVO);
		
		String defaultQuery = "board_name=" + boardVO.getBoard_name();
		model.addAttribute("DEFAULT_QUERY", defaultQuery);
		
		List<BoardVO> boardList = boardSvc.selectAllByPage(boardVO, pageVO);
		log.debug("페이지 데이터 : {}", boardList.toString());
		model.addAttribute("BOARD_LIST", boardList);
	}
	
	@ResponseBody
	@RequestMapping(value="/image_up", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String fileUp(MultipartFile upFile) {
		
		log.debug("파일업:" + upFile.getOriginalFilename());
		
		String saveName = fileService.fileUp(upFile);
		if(saveName == null) {
			return "FAIL";
		}
		
		return saveName;

	}

}
