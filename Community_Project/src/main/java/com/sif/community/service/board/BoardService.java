package com.sif.community.service.board;

import java.util.List;

import org.springframework.ui.Model;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;

public interface BoardService {
	
	public List<BoardVO> selectAllByPage(BoardVO boardVO, PaginationVO pageVO);

	public long countAll(BoardVO boardVO);

	public int save(BoardVO boardVO);

	public String delete(long board_no);

	public BoardVO findByNo(long board_no);

	public String saveView(long board_no, Model model);

}
