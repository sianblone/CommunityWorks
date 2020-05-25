package com.sif.community.service.board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.sif.community.dao.BoardDao;
import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service(value = "boardSvc")
public class BoardServiceImpl implements BoardService {
	
	private final BoardDao boardDao;
	
	@Override
	public long countAll(BoardVO boardVO) {
		// TODO Auto-generated method stub
		return boardDao.countAll(boardVO);
	}

	@Override
	public List<BoardVO> selectAllByPage(BoardVO boardVO, PaginationVO pageVO) {
		// boardVO에는 게시판이름, search_type, search_txt가 들어있다
		return boardDao.selectAllByPage(boardVO, pageVO);
	}
	
	@Override
	public String saveView(long board_no, Model model) {
		String render = "";
		
		if(board_no != 0) {
			// 쿼리에서 board_no를 받은 경우
			
			BoardVO boardVO = this.findByNo(board_no);
			// DB에 게시글번호로 검색한 데이터가 있으면(이미 있는 글이면) 수정하기
			if(boardVO != null) {
				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				// 로그인한 사용자와 게시글 작성자가 같거나 로그인한 사용자가 관리자면 글 수정 view 보여주기
				if(auth.getName().equals(boardVO.getBoard_writer()) || auth.getAuthorities().contains(new SimpleGrantedAuthority("USER_ADMIN"))) {
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
			// 게시글 번호를 쿼리로 받지 않은 경우(=신규작성 글) 수정이 아닌 새 글 작성 페이지 보여주기
			render = "board/save";
		}
		
		return render;
	}

	@Override
	public int save(BoardVO boardVO) {
		
		int ret = 0;
		
		// 신규작성 글인 경우(컨트롤러에서 넘겨준 boardVO에 게시글번호가 없는 경우)
		// 작성자, 날짜, 시간 세팅 후 INSERT
		if(boardVO.getBoard_no() == 0) {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			boardVO.setBoard_writer(username);
			
			LocalDateTime ldt = LocalDateTime.now();
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter dt = DateTimeFormatter.ofPattern("HH:mm:ss");
			boardVO.setBoard_date(ldt.format(df).toString());
			boardVO.setBoard_time(ldt.format(dt).toString());
			ret = boardDao.insert(boardVO);
		} else {
			// 글 수정인 경우(컨트롤러에서 넘겨준 boardVO에 게시글번호가 있는 경우)
			ret = boardDao.update(boardVO);
		}
		
		return ret;
	}
	
	@Override
	public String delete(long board_no) {
		String render = "";
		BoardVO boardVO = this.findByNo(board_no);
		// DB에 게시글번호로 검색한 데이터가 있으면(이미 있는 글이면) 삭제하기
		if(boardVO != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			// 로그인한 사용자와 게시글 작성자가 같거나 로그인한 사용자가 관리자면 글 삭제하기
			if(auth.getName().equals(boardVO.getBoard_writer()) || auth.getAuthorities().contains(new SimpleGrantedAuthority("USER_ADMIN"))) {
				boardVO.setBoard_delete(1);
				boardDao.delete(boardVO);
				
				String board_name = boardVO.getBoard_name();
				render = "redirect:/board/list?board_name=" + board_name;
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
