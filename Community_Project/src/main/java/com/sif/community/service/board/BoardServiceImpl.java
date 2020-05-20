package com.sif.community.service.board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
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
		" board_count BIGINT DEFAULT 0, " +
		" board_filename VARCHAR(256), " +
		" board_delete TINYINT NOT NULL DEFAULT 0, " +
		" board_recommend TINYINT NOT NULL DEFAULT 0, " +
		" board_category VARCHAR(20) " +
		" ) "
		;
		
		/*
		String create_function_depth
		= " DROP FUNCTION IF EXISTS fnc_depth; "
		+ " DELIMITER $$ "
		+ " CREATE FUNCTION fnc_depth() RETURNS INT "
		+ " NOT DETERMINISTIC "
		+ " READS SQL DATA "
		+ " BEGIN "
		+ " DECLARE v_no INT; "
		+ " DECLARE v_p_no INT; "
		+ " DECLARE CONTINUE HANDLER FOR NOT FOUND SET @board_no = NULL; "
		+ " SET v_p_no = @board_no; "
		+ " SET v_no = -1; "
		+ " IF @board_no IS NULL THEN "
		+ " RETURN NULL; "
		+ " END IF; "
		+ " LOOP "
		+ " SELECT MIN(board_no) "
		+ " INTO @board_no "
		+ " FROM tbl_board "
		+ " WHERE board_p_no = v_p_no "
		+ " AND board_p_no > v_no; "
		+ " IF (@board_no IS NOT NULL) OR (v_p_no = @start_with) THEN "
		+ " SET @level = @level + 1; "
		+ " RETURN @board_no; "
		+ " END IF; "
		+ " SET @level := @level - 1; "
		+ " SELECT board_no, board_p_no "
		+ " INTO v_no , v_p_no "
		+ " FROM tbl_board "
		+ " WHERE board_no = v_p_no; "
		+ " END LOOP; "
		+ " END "
		+ " $$ "
		+ " DELIMITER ; "
		;
		
		ddlDao.create_table(create_function_depth);
		*/
		ddlDao.create_table(create_table_board);
	}

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
