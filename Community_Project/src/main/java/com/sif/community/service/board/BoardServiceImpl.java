package com.sif.community.service.board;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sif.community.dao.BoardDao;
import com.sif.community.dao.DdlDao;
import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;

@Service(value = "boardSvc")
public class BoardServiceImpl implements BoardService {
	
	private final BoardDao boardDao;
	private final DdlDao ddlDao;
	
	public BoardServiceImpl(BoardDao boardDao, DdlDao ddlDao) {
		this.boardDao = boardDao;
		this.ddlDao = ddlDao;
		
		String create_table_board
		= " CREATE TABLE IF NOT EXISTS tbl_board ( " + 
		" board_no BIGINT PRIMARY KEY AUTO_INCREMENT, " + 
		" board_p_no BIGINT NOT NULL DEFAULT 0, " + 
		" board_name VARCHAR(20) NOT NULL, " + 
		" board_writer VARCHAR(50) NOT NULL, " +
		" board_date DATE, " +
		" board_time VARCHAR(10), " +
		" board_subject VARCHAR(125) NOT NULL, " +
		" board_content VARCHAR(1000) NOT NULL, " +
		" board_count BIGINT, " +
		" board_filename VARCHAR(256), " +
		" board_delete TINYINT NOT NULL, " +
		" board_recommend TINYINT NOT NULL, " +
		" board_category VARCHAR(20) " +
		" ) "
		;
		
		ddlDao.create_table(create_table_board);
		
	}

	@Override
	public long countAll(BoardVO boardVO) {
		// TODO Auto-generated method stub
		return boardDao.countAll(boardVO);
	}

	@Override
	public List<BoardVO> selectAllByPage(BoardVO boardVO, PaginationVO pageVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(BoardVO boardVO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	
	
	

}
