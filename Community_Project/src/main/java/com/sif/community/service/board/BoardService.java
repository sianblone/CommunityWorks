package com.sif.community.service.board;

import java.util.List;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;

public interface BoardService {
	
	public List<BoardVO> selectAllByPage(String boardName, String searchTxt, PaginationVO pageDTO);

	public long countAll();

	public void save(BoardVO boardVO);

	public void delete(Long id);
	

}
