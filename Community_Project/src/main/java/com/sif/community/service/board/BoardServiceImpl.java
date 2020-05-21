package com.sif.community.service.board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
	public int save(BoardVO boardVO) {
		
		int ret = 0;
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boardVO.setBoard_writer(username);
		
		// 신규작성 글인 경우(컨트롤러에서 넘겨준 boardVO에 게시글번호가 없는 경우) 날짜, 시간 세팅 후 INSERT
		if(boardVO.getBoard_no() == 0) {
			LocalDateTime ldt = LocalDateTime.now();
			DateTimeFormatter df
				= DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter dt
				= DateTimeFormatter.ofPattern("HH:mm:ss");
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
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BoardVO findByNo(Long no) {
		
		return boardDao.findByNo(no);
	}
	
	
	

}
