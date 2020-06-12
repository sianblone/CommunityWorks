package com.sif.community.service.board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.sif.community.dao.AdminDao;
import com.sif.community.dao.BoardDao;
import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.BoardVO;
import com.sif.community.model.CategoryVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.service.board.itf.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service(value = "boardSvc")
public class BoardServiceImpl implements BoardService {
	
	private final BoardDao boardDao;
	private final AdminDao adminDao;
	
	@Override
	public long countAll(BoardVO boardVO) {
		long totalCount = 0;
		
		// 현재 사용자가 관리자 권한일 때 delete = 1인 게시물도 count하기
		if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			totalCount = adminDao.countAll(boardVO);
		} else {
			// 현재 사용자가 관리자 권한이 아닐 때 delete = 1인 게시물 리스트에서 숨기기
			totalCount = boardDao.countAll(boardVO);
		}
		
		return totalCount;
	}

	@Override
	public List<BoardVO> selectAllByPage(BoardVO boardVO, PaginationVO pageVO) {
		// boardVO에는 게시판이름, search_type, search_txt가 들어있다.
		
		List<BoardVO> boardList = null;
		
		// 현재 사용자가 관리자 권한일 때 delete = 1인 게시물도 리스트에 보여주기
		if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			boardList = adminDao.selectAllByPageAdmin(boardVO, pageVO);
		} else {
			// 현재 사용자가 관리자 권한이 아닐 때 delete = 1인 게시물도 리스트에서 숨기기
			boardList = boardDao.selectAllByPage(boardVO, pageVO);
		}
		
		return boardList;
	}
	
	@Override
	public String saveView(BoardVO boardOptionVO, Model model) {
		String render = "";
		long board_no = boardOptionVO.getBoard_no();
		
		if(board_no != 0) {
			// save 메소드의 쿼리에서 board_no를 받은 경우(=글 수정)
			
			BoardVO boardVO = this.findByNo(board_no);
			// DB에 board_no로 검색한 데이터가 있으면(이미 있는 글이면) 수정하기
			if(boardVO != null) {
				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				// 로그인한 사용자가 게시글 작성자와 같거나 관리자면 글 수정 view 보여주기
				if(auth.getName().equals(boardVO.getBoard_writer()) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
					model.addAttribute("BOARD_VO",boardVO);
					render = "board/save";
				} else {
					// 현재 수정 버튼을 누른 사용자(로그인한 사용자)가 게시글 작성자와 다르고 관리자도 아니면 에러 페이지 보여주기
					render = "board/error";
				}
			} else {
				// DB에 baord_no로 검색한 데이터가 없으면 에러 페이지 보여주기
				render = "board/error";
			}
		} else {
			// 쿼리에서 board_no를 받지 않은 경우(=신규작성 글 또는 답글) 새 글 작성 페이지 보여주기
			// 답글인 경우 save.jsp에서 SpEL 태그를 이용해 URL 쿼리의 board_p_no를 받아와 POST action에 지정해주고 save POST 메소드로 submit
			// 컨트롤러 save POST 메소드의 VO에 board_p_no 값이 매핑되어 자동 세팅
			render = "board/save";
		}
		
		return render;
	}
	
	@Override
	public List<CategoryVO> selectCategoryByBoard(BoardVO boardOptionVO) {
		return boardDao.selectCategoryByBoard(boardOptionVO);
	}

	@Override
	public int save(BoardVO boardVO) {
		
		int ret = 0;
		
		// 카테고리 선택하지 않았을 경우 null로 만들어주기
		if(boardVO.getBoard_category() == 0) {
			boardVO.setBoard_category(null);
		}
		
		if(boardVO.getBoard_no() != 0) {
			// 글 수정인 경우(컨트롤러에서 넘겨준 boardVO에 게시글번호가 있는 경우)
			BoardVO dbBoardVO = this.findByNo(boardVO.getBoard_no());
			dbBoardVO.setBoard_category(boardVO.getBoard_category());
			dbBoardVO.setBoard_subject(boardVO.getBoard_subject());
			dbBoardVO.setBoard_content(boardVO.getBoard_content());
			ret = boardDao.update(dbBoardVO);
		} else {
			// 신규작성 글이나 답글인 경우(컨트롤러에서 넘겨준 boardVO에 게시글번호가 없는 경우)
			// 작성자, 날짜, 시간 세팅 후 INSERT
			// 답글인 경우는 GET 쿼리에 board_p_no가 있기 때문에 자동으로 세팅됨
			saveSetting(boardVO);
			ret = boardDao.insert(boardVO);
		}
		
		return ret;
	}
	
	protected BoardVO saveSetting(BoardVO boardVO) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boardVO.setBoard_writer(username);
		
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("HH:mm:ss");
		boardVO.setBoard_date(ldt.format(df).toString());
		boardVO.setBoard_time(ldt.format(dt).toString());
		
		return boardVO;
	}
	
	@Override
	public String delete(long board_no, Integer currPage) {
		String render = "";
		BoardVO boardVO = this.findByNo(board_no);
		// DB에 게시글번호로 검색한 데이터가 있으면(이미 있는 글이면) 삭제하기
		if(boardVO != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			// 로그인한 사용자와 게시글 작성자가 같거나 로그인한 사용자가 관리자면 글 삭제하기
			if(auth.getName().equals(boardVO.getBoard_writer()) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
				boardVO.setBoard_delete(1);
				boardDao.delete(boardVO);
				
				long board_info = boardVO.getBoard_info();
				render = "redirect:/board/list?board_info=" + board_info;
				if(currPage != null) render += "&currPage=" + currPage;
			} else {
				// 현재 로그인한 사용자와 게시글 작성자가 다르고 관리자가 아니면 삭제 불가, 에러페이지 보여주기
				render = "board/error";
			}
		} else {
			// DB에 baord_no로 검색한 데이터가 없으면 에러페이지 보여주기
			render = "board/error";
		}
		
		return render;
	}
	
	@Override
	public BoardVO findByNo(long board_no) {
		return boardDao.findByNo(board_no);
	}

	@Override
	public BoardInfoVO findByBoardInfo(long board_info) {
		return boardDao.findByBoardInfo(board_info);
	}

	@Override
	public List<BoardInfoVO> selectAllBoardInfo() {
		return boardDao.selectAllBoardInfo();
	}

}
