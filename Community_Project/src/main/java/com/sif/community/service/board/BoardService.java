package com.sif.community.service.board;

import java.util.List;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;

public interface BoardService {
	
	public List<BoardVO> selectAllByPage(BoardVO boardVO, PaginationVO pageVO);

	public long countAll(BoardVO boardVO);

	public int save(BoardVO boardVO);

	public void delete(Long id);

	public BoardVO findByNo(Long no);
	

}
