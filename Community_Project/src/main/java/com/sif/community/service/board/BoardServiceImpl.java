package com.sif.community.service.board;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.sif.community.dao.BoardDao;
import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.BoardVO;
import com.sif.community.model.CategoryVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.model.UserDetailsVO;
import com.sif.community.service.board.itf.BoardService;
import com.sif.community.service.user.UserService;
import com.sif.util.CookieUtil;
import com.sif.util.SpringSecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service(value = "boardSvc")
public class BoardServiceImpl implements BoardService {
	
	private final BoardDao boardDao;
	private final UserService userSvc;
	
	@Override
	public long countAll(BoardVO boardVO) {
		long totalCount = 0;
		
		List<UserDetailsVO> usernameList = null;
		if(boardVO.getSearch_type().equals("nickname")) usernameList = userSvc.findByNickname(boardVO.getSearch_txt());
		
		boolean isAdmin = false;
		// 현재 사용자가 관리자 권한을 가지고 있을 때 delete = 1인 게시물도 count하기
		if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			isAdmin = true;
		}
		totalCount = boardDao.countAll(boardVO, usernameList, isAdmin);
		
		return totalCount;
	}

	@Override
	public List<BoardVO> selectAllByPage(BoardVO boardVO, PaginationVO pageVO) {
		List<BoardVO> boardList = null;
		
		List<UserDetailsVO> usernameList = null;
		// 닉네임 검색인 경우
		if(boardVO.getSearch_type().equals("nickname")) usernameList = userSvc.findByNickname(boardVO.getSearch_txt());
		
		boolean isAdmin = false;
		// 현재 사용자가 관리자 권한을 가지고 있을 때 delete = 1인 게시물도 리스트에 보여주기
		if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			isAdmin = true;
		}
		boardList = boardDao.selectAllByPage(boardVO, pageVO, usernameList, isAdmin);
		
		return boardList;
	}
	
	@Override
	public BoardVO findByBoardNo(long board_no) {
		return boardDao.findByBoardNo(board_no);
	}
	
	@Override
	public List<CategoryVO> selectCategoryByBoard(BoardVO boardOptionVO) {
		return boardDao.selectCategoryByBoard(boardOptionVO);
	}
	
	@Override
	public List<BoardInfoVO> selectMainPage(int limit_value) {
		return boardDao.selectMainPage(limit_value);
	}
	
	@Override
	public List<BoardInfoVO> selectAllBoardInfo() {
		return boardDao.selectAllBoardInfo();
	}
	
	@Override
	public BoardInfoVO findByBoardInfo(long board_info) {
		return boardDao.findByBoardInfo(board_info);
	}
	
	@Transactional
	@Override
	public int save(BoardVO boardVO) {
		
		// DB 입력 성공 여부에 따라 컨트롤러로 리턴할 값
		int result = 0;
		
		// 글 작성 시 카테고리를 선택하지 않은 경우 받은 값 0을 null로 바꿔서 DB에 넣기
		if(boardVO.getBoard_category() == 0) {
			boardVO.setBoard_category(null);
		}
		
		long board_no = boardVO.getBoard_no();
		
		// 1. boardVO에 board_no가 있는 경우(=> 글 수정)
		if(board_no != 0) {
			BoardVO dbBoardVO = this.findByBoardNo(board_no);
			if(dbBoardVO == null) {
				// 1-1. 게시글번호로 검색한 데이터가 DB에 없는 경우 에러페이지 보여주기
				return -100;
			}
			
			// 1-2. 게시글번호로 검색한 데이터가 DB에 있는 경우
			// 로그인한 사용자가 게시글 작성자거나 관리자면 글 수정
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth.getName().equals(boardVO.getBoard_writer()) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
				dbBoardVO.setBoard_category(boardVO.getBoard_category());
				dbBoardVO.setBoard_subject(boardVO.getBoard_subject());
				dbBoardVO.setBoard_content(boardVO.getBoard_content());
				result = boardDao.update(dbBoardVO);
			}
		} else if(boardVO.getBoard_p_no() != 0) {
			// 2. 답글인 경우(컨트롤러에서 넘겨준 boardVO에 board_no가 있고 board_p_no가 없는 경우
			// 답글인 경우는 GET URL 쿼리에 board_p_no가 있기 때문에 boardVO에 값이 세팅되어 있다
			
			// 2-1. 부모글의 board_group 가져와서 group 세팅하기
			BoardVO parentBoardVO = this.findByBoardNo(boardVO.getBoard_p_no());
			long board_group = parentBoardVO.getBoard_group();
			boardVO.setBoard_group(board_group);
			// 2-2. 부모글의 board_group 가져와서 order를 maxOrder + 1로 세팅하기
			int maxOrder = boardDao.maxOrderByBoardGroup(board_group);
			boardVO.setBoard_order(maxOrder + 1);
			// 2-3. 부모글의 board_depth 가져와서 depth를 +1로 세팅하기
			boardVO.setBoard_depth(parentBoardVO.getBoard_depth() + 1);
			// 2-4. boardVO에 작성자, 날짜+시간 세팅 후 INSERT
			saveSetting(boardVO);
			result = boardDao.insert(boardVO);
		} else {
			// 3. 신규작성 글인 경우(컨트롤러에서 넘겨준 boardVO에 board_no와 board_p_no가 없는 경우)
			// 3-1. boardVO에 작성자, 날짜+시간 세팅 후 INSERT
			saveSetting(boardVO);
			result = boardDao.insert(boardVO);
			// 3-2. 방금 작성한 글을 다시 DB에서 가져와서 글 그룹, 글 순서, 글 깊이 업데이트
			// boardVO에는 insert 후 새로운 board_no 값이 저장되어 있다(MyBatis의 selectKey 이용)
			BoardVO insertedBoardVO = boardDao.findByBoardNo(boardVO.getBoard_no());
			// (1) 글 그룹 = 작성한 글 번호
			insertedBoardVO.setBoard_group(insertedBoardVO.getBoard_no());
			// (2) 글 순서 = 0(업데이트 할 필요 없음)
			// (3) 글 깊이 = 0(업데이트 할 필요 없음)
			result = boardDao.update(insertedBoardVO);
		}
		
		return result;
		
	}
	
	protected BoardVO saveSetting(BoardVO boardVO) {
		// 로그인한 경우 작성자 이름을 로그인한 사용자 이름으로 세팅
		boolean isLoggedIn = SpringSecurityUtil.isLoggedIn();
		if(isLoggedIn) {
			boardVO.setBoard_writer(SecurityContextHolder.getContext().getAuthentication().getName());
		}
		
		// 날짜+시간 세팅
		LocalDateTime ldt = LocalDateTime.now();
		Date date = Date.from( ldt.atZone( ZoneId.systemDefault()).toInstant() );
		boardVO.setBoard_datetime(date);
		
		return boardVO;
	}
	
	@Override
	public int delete(long board_no) {
		int result = 0;
		
		// DB에 게시글번호로 검색한 데이터가 있으면(이미 있는 글이면) 삭제하기
		BoardVO boardVO = this.findByBoardNo(board_no);
		// DB에 board_no로 검색한 데이터가 없으면 -100 return
		if(boardVO == null) {
			return -100;
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// 현재 삭제 버튼을 누른 사용자(로그인한 사용자)가 게시글 작성자가 아니고 관리자도 아니면 에러 페이지 보여주기
		if(!auth.getName().equals(boardVO.getBoard_writer()) && !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return -200;
		}
		
		// 로그인한 사용자가 게시글 작성자거나 관리자면 글 삭제
		boardVO.setBoard_delete(1);
		result = boardDao.update_delete(boardVO);
		
		return result;
	}
	
	@Override
	public int admin(long board_no, String command) {
		int result = 0;
		
		// DB에 게시글번호로 검색한 데이터가 있으면(이미 있는 글이면) 삭제하기
		BoardVO boardVO = this.findByBoardNo(board_no);
		// DB에 board_no로 검색한 데이터가 없으면 -100 return
		if(boardVO == null) {
			return -100;
		}
		
		// 관리자가 아니면 -200 return
		if(!SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return -200;
		}
		
		if(command.equals("restore")) {
			// 글 복구 클릭
			boardVO.setBoard_delete(0);
			result = boardDao.update_delete(boardVO);
		} else if(command.equals("delete")) {
			// 글 완전삭제 클릭
			result = boardDao.delete(board_no);
		}
		
		return result;
	}

	@Override
	public int updateBoardCount(BoardVO boardOptionVO, HttpServletRequest request, HttpServletResponse response) {
		int result = 0;
		result = CookieUtil.boardCookie(request, response, boardOptionVO, boardDao);
		
		return result;
	}

	@Override
	public int updateBoardRecommend(BoardVO boardOptionVO, HttpServletRequest request, HttpServletResponse response) {
		int result = 0;
		result = CookieUtil.boardCookie(request, response, boardOptionVO, boardDao, "recommend");
				
		return result;
	}
	
}
