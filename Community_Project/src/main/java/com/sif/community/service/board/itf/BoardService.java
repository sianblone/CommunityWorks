package com.sif.community.service.board.itf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationDTO;

public interface BoardService {
	
	public long countAll(BoardVO boardOptionVO);
	public List<BoardVO> selectAllByPage(BoardVO boardOptionVO, PaginationDTO pageDTO);
	public BoardVO findByBoardNo(long board_no);
	
	public int save(BoardVO boardVO);
	public int delete(long board_no);
	public int admin(long board_no, String order);
	public int updateBoardCount(BoardVO boardOptionVO, HttpServletRequest request, HttpServletResponse response);
	public int updateBoardRecommend(BoardVO boardOptionVO, HttpServletRequest request, HttpServletResponse response);
}
