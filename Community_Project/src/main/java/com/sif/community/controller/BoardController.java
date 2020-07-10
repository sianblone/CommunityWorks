package com.sif.community.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationDTO;
import com.sif.community.service.board.BoardInfoService;
import com.sif.community.service.board.CategoryService;
import com.sif.community.service.board.FileService;
import com.sif.community.service.board.itf.BoardService;
import com.sif.community.service.board.itf.PaginationService;
import com.sif.util.ProjectUtil;
import com.sif.util.SpSec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/board")
@Controller
public class BoardController {
		
	private final FileService fileService;
	@Qualifier(value = "pageSvc")
	private final PaginationService pageSvc;
	@Qualifier(value = "boardSvc")
	private final BoardService boardSvc;
	@Qualifier(value = "cateSvc")
	private final CategoryService cateSvc;
	@Qualifier(value = "boardInfoSvc")
	private final BoardInfoService boardInfoSvc;
	
	// 게시판 컨트롤러
	// 리스트(GET) + 게시판 이름 + 검색 + 페이지 / 상세보기(GET) / 저장(GET: 화면 보여주기, POST: 입력 받기) / 삭제(POST)
	
	// 리스트 + 게시판 이름 + 검색 + 페이지 메소드
	// 검색 값과 현재 페이지로 페이지네이션 select하기
	// boardOptionVO에는 board_info, search_type, search_txt가 들어있다
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(
					Model model,
					BoardVO boardOptionVO,
					Integer pageNo) {
		// 없는 게시판(0)을 입력받으면 메인 페이지로
		if(boardOptionVO.getBoard_info() == 0) return "redirect:/";
		
		if(boardOptionVO.getSearch_type() == null) boardOptionVO.setSearch_type("");
		if(boardOptionVO.getSearch_txt() == null) boardOptionVO.setSearch_txt("");
		
		// boardVO에는 게시판번호(board_info), 검색옵션(search_type), 검색어(search_txt)가 들어있다
		this.selectAllByPage(model, boardOptionVO, pageNo);
		
		return "board/list";
	}
	
	// 상세보기 메소드
	// id값으로 게시글 보여주기
	// boardOptionVO에는 board_info, board_no가 들어있다
	@RequestMapping(value="/details", method=RequestMethod.GET)
	public String details(BoardVO boardOptionVO, Model model, HttpServletRequest request, HttpServletResponse response) {
		BoardVO boardVO = boardSvc.findByBoardNo(boardOptionVO.getBoard_no());
		
		// 현재 로그인한 사용자가 관리자가 아닐 때 delete 값이 1인 게시물 열람 불가
		if(!SpSec.isAdmin() && boardVO.getBoard_delete() == 1) {
			return "board/error";
		}
		
		// 현재 로그인한 사용자가 작성자거나 로그인한 사용자 권한이 ADMIN일 때 글 수정, 삭제 가능
		if( SpSec.isAdmin() ) boardVO.setViewerAdmin(true);
		if( boardVO.getBoard_writer().equals(SpSec.username()) ) boardVO.setViewerWriter(true);
		
		// 조회수 증가 (쿠키를 이용한 중복체크)
		int result = boardSvc.updateBoardCount(boardOptionVO, request, response);
		if(result > 0) {
			// 조회수가 증가되었다면 이미 DB에서 가져온 데이터의 조회수를 1 증가시켜 view에 보여주기
			boardVO.setBoard_count(boardVO.getBoard_count() + 1);
		}
		
		model.addAttribute("BOARD_VO",boardVO);
		
		return "board/details";
	}
	
	// 저장 메소드
	// boardOptionVO에는 board_no, board_info, board_p_no가 들어간다
	// board_no 값을 받지 않았으면 새 저장화면 보여주기(새 글 작성)
	// board_no 값을 받았으면 현재 사용자와 DB 게시글 작성자를 대조하여 일치하면 값 채워서 저장화면 보여주기(글 수정)
	// 현재 사용자와 DB의 게시글 작성자가 다르면 오류 페이지로 Redirect
	@RequestMapping(value="/save", method=RequestMethod.GET)
	public String save(BoardVO boardOptionVO, Model model) {
		
		// 없는 게시판(0)을 입력받으면 메인 페이지로
		if(boardOptionVO.getBoard_info() == 0) {
			return "redirect:/";
		}
		
		long board_no = boardOptionVO.getBoard_no();
		
		if(board_no == 0) {
			// 쿼리에서 board_no를 받지 않은 경우(=신규작성 글 또는 답글) 새 글 작성 페이지 보여주기
			// 답글인 경우 save.jsp에서 SpEL 태그를 이용해 URL 쿼리의 board_p_no를 받아와 POST action에 지정해주고 save POST 메소드로 submit
			// 컨트롤러 save POST 메소드의 VO에 board_p_no 값이 매핑되어 자동 세팅
			model.addAttribute("CATEGORY_LIST", cateSvc.findByBiId( boardOptionVO.getBoard_info() ) );
			model.addAttribute("BOARD_INFO", boardInfoSvc.findByBiId( boardOptionVO.getBoard_info() ) );
			return "board/save";
		}
		
		// URL 쿼리에서 board_no를 받은 경우(=> 글 수정)
		BoardVO boardVO = boardSvc.findByBoardNo(board_no);
		// DB에 baord_no로 검색한 데이터가 없으면 에러 페이지 보여주기
		if(boardVO == null) {
			return "board/error";
		}
		
		// DB에 board_no로 검색한 데이터가 있으면(이미 있는 글이면) 수정하기	
		// 현재 수정 버튼을 누른 사용자(로그인한 사용자)가 게시글 작성자가 아니고 관리자도 아니면 에러 페이지 보여주기
		if(!SpSec.username().equals(boardVO.getBoard_writer()) && !SpSec.isAdmin()) {
			return "board/error";
		}
		
		// 로그인한 사용자가 게시글 작성자거나 관리자면 글 수정 view 보여주기
		model.addAttribute("BOARD_VO",boardVO);
		model.addAttribute("CATEGORY_LIST", cateSvc.findByBiId(boardOptionVO.getBoard_info() ));
		model.addAttribute("BOARD_INFO", boardInfoSvc.findByBiId(boardOptionVO.getBoard_info() ));
		
		return "board/save";
	}
	
	// form에서 저장버튼 클릭 시 사용할 메소드
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(BoardVO boardVO, Integer pageNo) {
		
		// 없는 게시판(0)을 입력받으면 메인 페이지로
		if(boardVO.getBoard_info() == 0) {
			return "redirect:/";
		}
		
		String render = "";
		int result = boardSvc.save(boardVO);
		// 글 수정인 경우 DB에 board_no로 검색한 데이터가 없으면 에러페이지 보여주기
		if(result == -100) {
			render = "board/error";
		} else {
			render = "redirect:/board/list?board_info=" + boardVO.getBoard_info();
			if(pageNo != null) render += "&pageNo=" + pageNo;
		}
				
		return render;
	}
	
	// 게시물 삭제버튼 클릭 시 사용할 메소드(게시글 deleted 칼럼 값 1로 바꿔주기)
	// boardOptionVO에는 board_no, board_info가 들어있다
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public String delete(BoardVO boardOptionVO, Integer pageNo) {
		String render = "";
		int result = boardSvc.updateDeleteFlag(boardOptionVO.getBoard_no());
		
		if(result == -100 || result == -200) {
			render = "board/error";
		} else {
			long board_info = boardOptionVO.getBoard_info();
			render = "redirect:/board/list?board_info=" + board_info;
			if(pageNo != null) render += "&pageNo=" + pageNo;
		}
		
		return render;
	}
	
	// 추천버튼 클릭 시 사용할 메소드
	// 게시글 recommend 칼럼 값 +1
	// boardOptionVO에는 board_no가 들어있다
	@ResponseBody
	@RequestMapping(value="/recommend", method=RequestMethod.GET)
	public int recommend(BoardVO boardOptionVO, HttpServletRequest request, HttpServletResponse response) {
		int result = boardSvc.updateBoardRecommend(boardOptionVO, request, response);
		return result;
	}
	
	// 완전삭제, 글 복구 시 사용할 메소드
	// boardOptionVO에는 board_info, board_no가 들어있다
	@RequestMapping(value = "/admin", method=RequestMethod.GET)
	public String admin(BoardVO boardOptionVO, Integer pageNo, String command) {
		String render = "";
		int result = boardSvc.admin(boardOptionVO.getBoard_no(), command);
		
		if(result == -100 || result == -200) {
			render = "board/error";
		} else {
			long board_info = boardOptionVO.getBoard_info();
			render = "redirect:/board/list?board_info=" + board_info;
			if(pageNo != null) render += "&pageNo=" + pageNo;
		}
		
		return render;
	}
	
	// 페이지네이션
	// boardOptionVO에는 board_info, search_type, search_txt가 들어있다
	private void selectAllByPage(Model model, BoardVO boardOptionVO, Integer pageNo) {
		if(pageNo == null) pageNo = 1;
		
		// 1. 페이징 할 곳에서 dataCount(총 데이터 수) 가져오기
		long dataCount = boardSvc.countAll(boardOptionVO);
		log.debug("카운트 : {}", dataCount);
		
		// 2. 페이지네이션 테이블 게시판 정보 가져오기
		// 가져온 pageDTO는 page_id, page_bi_id, page_location, page_data_cnt, page_range가 들어있거나 null이다
		PaginationDTO pageDTO = pageSvc.findByBiId(boardOptionVO.getBoard_info(), ProjectUtil.PAGE_LOCATION_BOARD);
		
		// 3. 페이지네이션 정보 만들기
		pageDTO = pageSvc.makePageInfoMiddle(dataCount, pageDTO, pageNo, false);
		log.debug("페이지 : {}", pageDTO.toString());
		
		// 4. 게시판 페이지네이션 정보 view로 보내주기
		model.addAttribute("PAGE_DTO", pageDTO);
		
		// 5. 게시판 페이지네이션 기본 쿼리 view로 보내주기
		String page_default_query = "&board_info=" + boardOptionVO.getBoard_info();
		model.addAttribute("PAGE_DEFAULT_QUERY", page_default_query);
		
		// 게시판 이름 표시(헤더)
		BoardInfoVO boardInfoVO = boardInfoSvc.findByBiId(boardOptionVO.getBoard_info());		
		model.addAttribute("BOARD_INFO", boardInfoVO);
		
		// 게시판 내용 view로 보내주기
		List<BoardVO> boardList = boardSvc.selectAllByPage(boardOptionVO, pageDTO);
		model.addAttribute("BOARD_LIST", boardList);
	}
	
	// 이미지 업로드 시 사용할 메소드
	@ResponseBody
	@RequestMapping(value="/image_up", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String fileUp(MultipartFile uploadFile) {
		
		log.debug("업로드한 파일명 : " + uploadFile.getOriginalFilename());
		
		String saveName = fileService.fileUp(uploadFile);
		if(saveName == null) {
			return "FAIL";
		}
		
		return saveName;
	}

}
