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
import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.service.board.itf.BoardService;

import lombok.RequiredArgsConstructor;

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
			// 현재 사용자가 관리자 권한이 아닐 때 delete = 1인 게시물도 리스트에서 숨기기
			totalCount = boardDao.countAll(boardVO);
		}
		
		return totalCount;
	}

	@Override
	public List<BoardVO> selectAllByPage(BoardVO boardVO, PaginationVO pageVO) {
		// boardVO에는 게시판이름, search_type, search_txt가 들어있다
		
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
			// 쿼리에서 board_no를 받은 경우
			
			BoardVO boardVO = this.findByNo(board_no);
			// DB에 게시글번호로 검색한 데이터가 있으면(이미 있는 글이면) 수정하기
			if(boardVO != null) {
				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				// 로그인한 사용자와 게시글 작성자가 같거나 로그인한 사용자가 관리자면 글 수정 view 보여주기
				if(auth.getName().equals(boardVO.getBoard_writer()) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
					model.addAttribute("BOARD_VO",boardVO);
					render = "board/save";
				} else {
					// 현재 로그인한 사용자와 게시글 작성자가 다르고 관리자가 아니면 수정 불가
					render = "board/error";
				}
			} else {
				// DB에 baord_no로 검색한 데이터가 없으면 에러페이지 보여주기
				render = "board/error";
			}
		} else {
			// 쿼리에서 board_no를 받지 않은 경우(=신규작성 글 또는 답글) 새 글 작성 페이지 보여주기
			// 답글인 경우 GET 쿼리의 board_p_no를 받아와 save.jsp의 POST action에 지정해주고 submit, 컨트롤러의 VO에 board_p_no 값이 매핑되어 자동 세팅
			render = "board/save";
		}
		
		return render;
	}

	@Override
	public int save(BoardVO boardVO) {
		
		int ret = 0;
		
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
			// 답글인 경우는 GET 쿼리에 board_p_no가 있기에 자동으로 세팅
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
				
				String board_name = boardVO.getBoard_name();
				render = "redirect:/board/list?board_name=" + board_name;
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

}
